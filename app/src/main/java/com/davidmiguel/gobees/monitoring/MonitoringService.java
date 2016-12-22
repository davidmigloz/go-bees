package com.davidmiguel.gobees.monitoring;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.camera.CameraFrame;
import com.davidmiguel.gobees.camera.ICameraAccess;
import com.davidmiguel.gobees.camera.HardwareCamera;
import com.davidmiguel.gobees.video.BeesCounter;
import com.davidmiguel.gobees.video.ContourBeesCounter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

/**
 * Monitoring service.
 * It reads the camera feed and run the bee counter algorithm in background.
 */
@SuppressWarnings("deprecation")
public class MonitoringService extends Service implements ICameraAccess {

    private static final String TAG = MonitoringService.class.getSimpleName();
    public static int NOTIFICATION_ID = 101;
    public static String START_ACTION = "start_action";
    public static String STOP_ACTION = "stop_action";

    private final IBinder mBinder = new MonitoringBinder();
    private HardwareCamera hardwareCamera;
    private BeesCounter bc;
    private boolean openCVLoaded = false;

    @Override
    public void onCreate() {
        super.onCreate();
        // Configure OpenCV callback
        BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(final int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                        openCVLoaded = true;
                        bc = ContourBeesCounter.getInstance();
                        hardwareCamera = new HardwareCamera(MonitoringService.this,
                                MonitoringService.this, Camera.CameraInfo.CAMERA_FACING_FRONT);
                        if (!hardwareCamera.isConnected()) {
                            hardwareCamera.connect();
                        }
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(START_ACTION)) {
            Log.i(TAG, "Received Start Foreground Intent ");
            // Config notification
            // Intent to the monitoring activity
            Intent monitoringIntent = new Intent(this, MonitoringActivity.class);
            monitoringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pMonitoringIntent = PendingIntent.getActivity(this, 0,
                    monitoringIntent, 0);
            // Stop monitoring intent
            Intent stopIntent = new Intent(this, MonitoringService.class);
            stopIntent.setAction(STOP_ACTION);
            PendingIntent pStopIntent = PendingIntent.getService(this, 0,
                    stopIntent, 0);
            // Create notification
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("GoBees")
                    .setTicker("GoBees")
                    .setContentText("Monitoring hive...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setLargeIcon(
                    //        Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pMonitoringIntent)
                    .setOngoing(true)
                    .addAction(android.R.drawable.ic_media_pause,
                            "Stop", pStopIntent).build();
            startForeground(NOTIFICATION_ID, notification);
        } else if (intent.getAction().equals(STOP_ACTION)) {
            Log.i(TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release camera
        if (hardwareCamera != null && hardwareCamera.isConnected()) {
            hardwareCamera.release();
            hardwareCamera = null;
        }
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
    public void onPreviewFrame(CameraFrame cameraFrame) {
        int numBees = bc.countBees(cameraFrame.gray());
        Log.i(TAG, "onPreviewFrame called! Bees: " + numBees);
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class MonitoringBinder extends Binder {
        MonitoringService getService() {
            // Return this instance of MonitoringService so clients can call public methods
            return MonitoringService.this;
        }
    }
}
