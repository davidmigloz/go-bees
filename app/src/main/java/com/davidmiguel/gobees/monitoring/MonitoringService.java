package com.davidmiguel.gobees.monitoring;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.camera.AndroidCamera;
import com.davidmiguel.gobees.camera.AndroidCameraImpl;
import com.davidmiguel.gobees.camera.AndroidCameraListener;
import com.davidmiguel.gobees.camera.CameraFrame;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.video.BeesCounter;
import com.davidmiguel.gobees.video.ContourBeesCounter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.Date;
import java.util.LinkedList;

/**
 * Monitoring service.
 * It reads the camera feed and run the bee counter algorithm in background.
 */
@SuppressWarnings("deprecation")
public class MonitoringService extends Service implements AndroidCameraListener {

    public static final String ARGUMENT_MON_SETTINGS = "MONITORING_SETTINGS";
    public static final String START_ACTION = "start_action";
    public static final String STOP_ACTION = "stop_action";

    private static final int NOTIFICATION_ID = 101;
    private static final int T_5_SECONDS = 5000;

    private static MonitoringService INSTANCE = null;
    private final IBinder mBinder = new MonitoringBinder();
    private GoBeesRepository goBeesRepository;
    private LinkedList<Record> records;
    private AndroidCamera androidCamera;
    private BeesCounter bc;
    private MonitoringSettings monitoringSettings;
    private boolean openCVLoaded = false;
    private long startTime;

    /**
     * Checks whether the service is running.
     *
     * @return status.
     */
    public static boolean isRunning() {
        return INSTANCE != null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        // Init record list
        records = new LinkedList<>();
        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // START action
        if (intent.getAction().equals(START_ACTION)) {
            // Get monitoring config
            monitoringSettings = (MonitoringSettings) intent.getSerializableExtra(ARGUMENT_MON_SETTINGS);
            // Configurations
            configOpenCV();
            configBeeCounter();
            configCamera();
            Notification n = configNotification();
            // Start service in foreground
            startForeground(NOTIFICATION_ID, n);

        // STOP action
        } else if (intent.getAction().equals(STOP_ACTION)) {
            // Release camera
            androidCamera.release();
            // Clean records
            cleanRecords();
            // Save records
            goBeesRepository.saveRecords(monitoringSettings.getHiveId(), records, new GoBeesDataSource.TaskCallback() {
                @Override
                public void onSuccess() {
                    stopService();
                }

                @Override
                public void onFailure() {
                    stopService();
                }
            });
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        INSTANCE = null;
        // Release camera
        if (androidCamera != null && androidCamera.isConnected()) {
            androidCamera.release();
            androidCamera = null;
        }
        // Close database
        goBeesRepository.closeDb();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean isOpenCVLoaded() {
        return openCVLoaded;
    }

    @Override
    public void onCameraStarted(int width, int height) {
        // Calculate start time (to be use in chronometer)
        Date now = new Date();
        long elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        startTime = now.getTime() - elapsedRealtimeOffset;
    }

    @Override
    public void onPreviewFrame(CameraFrame cameraFrame) {
        // Process frame
        int numBees = bc.countBees(cameraFrame.gray());
        bc.getProcessedFrame().release();
        // Save record
        records.add(new Record(new Date(), numBees));
    }

    /**
     * Get monitoring start time.
     *
     * @return start time.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Config OpenCV (config callback and init OpenCV).
     */
    private void configOpenCV() {
        // OpenCV callback
        BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(final int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                        openCVLoaded = true;
                        startRecording();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };
        // Init openCV
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, loaderCallback);
    }

    /**
     * Config bee counter with settings.
     */
    private void configBeeCounter() {
        bc = ContourBeesCounter.getInstance();
        bc.updateBlobSize(monitoringSettings.getBlobSize());
        bc.updateMinArea(monitoringSettings.getMinArea());
        bc.updateMaxArea(monitoringSettings.getMaxArea());
    }

    /**
     * Config camera with settings.
     */
    private void configCamera() {
        androidCamera = new AndroidCameraImpl(MonitoringService.this,
                Camera.CameraInfo.CAMERA_FACING_BACK,
                monitoringSettings.getMaxFrameWidth(),
                monitoringSettings.getMaxFrameHeight(),
                monitoringSettings.getZoomRatio());
    }

    /**
     * Config notification.
     */
    private Notification configNotification() {
        // Intent to the monitoring activity (when the notification is clicked)
        Intent monitoringIntent = new Intent(this, MonitoringActivity.class);
        monitoringIntent.putExtra(MonitoringFragment.ARGUMENT_HIVE_ID, monitoringSettings.getHiveId());
        PendingIntent pMonitoringIntent = PendingIntent.getActivity(this, 0,
                monitoringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Create notification
        return new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setTicker(getString(R.string.app_name))
                .setContentText(getString(R.string.monitoring_notification_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pMonitoringIntent)
                .setOngoing(true).build();
    }

    /**
     * Start recording (frames will be received via onPreviewFrame()).
     */
    private void startRecording() {
        if (!androidCamera.isConnected()) {
            androidCamera.connect();
        }
    }

    /**
     * Stop service.
     */
    private void stopService() {
        stopForeground(true);
        stopSelf();
    }

    /**
     * Delete first and last records that usually contain noise and add two special records
     * at the beginning and ending to know the limits of the recording.
     */
    private void cleanRecords() {
        long initTime = records.getFirst().getTimestamp().getTime();
        long endTime = records.getLast().getTimestamp().getTime();
        // Delete first seconds
        while (records.size() > 0 && records.getFirst().getTimestamp().getTime() - initTime < T_5_SECONDS) {
            records.removeFirst();
        }
        // Delete last seconds
        while (records.size() > 0 && endTime - records.getLast().getTimestamp().getTime() < T_5_SECONDS) {
            records.removeLast();
        }
        // Save initial and last record (to know the beginning and ending of the recording)
        if (records.size() > 0) {
            records.getFirst().setNumBees(-1);
            records.getLast().setNumBees(-1);
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class MonitoringBinder extends Binder {
        MonitoringService getService() {
            // Return this INSTANCE of MonitoringService so clients can call public methods
            return MonitoringService.this;
        }
    }
}
