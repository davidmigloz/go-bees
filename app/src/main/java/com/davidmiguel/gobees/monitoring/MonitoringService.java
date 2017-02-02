/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

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
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.SaveRecordingCallback;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
import com.davidmiguel.gobees.monitoring.algorithm.AreaBeesCounter;
import com.davidmiguel.gobees.monitoring.algorithm.BeesCounter;
import com.davidmiguel.gobees.monitoring.camera.AndroidCamera;
import com.davidmiguel.gobees.monitoring.camera.AndroidCameraImpl;
import com.davidmiguel.gobees.monitoring.camera.AndroidCameraListener;
import com.davidmiguel.gobees.monitoring.camera.CameraFrame;
import com.davidmiguel.gobees.utils.DateTimeUtils;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Monitoring service.
 * It reads the camera feed and run the bee counter algorithm in background.
 * It also gets weather information periodically.
 * Notes:
 * - Intent with START action: starts monitoring.
 * - Intent with STOP action: stops monitoring.
 * - There is a delay of INITIAL_DELAY before the camera is opened (to avoid trepidations when the
 * user manipulates the phone).
 * - The first INITIAL_NUM_FRAMES frames are used to create a background model (they are not used
 * to count bees). During this time, the frame rate is INITIAL_FRAME_RATE.
 * - After the background model is created, the frame rate is set to the one configured by the user.
 * - The recording must have more than 5 records, if not, it is ignored.
 * - The first and last record of a recording always have numBees = -1 (this is used to know
 * when the recording starts and ends).
 * - The service gets and saves apiary weather data every WEATHER_REFRESH_RATE.
 * - When the service ends, it calls the appropriate callback:
 */
@SuppressWarnings("deprecation")
public class MonitoringService extends Service implements AndroidCameraListener {

    // Intent argument
    public static final String ARGUMENT_MON_SETTINGS = "MONITORING_SETTINGS";
    // Intent actions
    public static final String START_ACTION = "start_action";
    public static final String STOP_ACTION = "stop_action";
    // Notification id
    private static final int NOTIFICATION_ID = 101;

    // Delay before start recording
    private static final long INITIAL_DELAY = DateTimeUtils.T_5_SECONDS;
    // Frame rate while creating background model
    private static final long INITIAL_FRAME_RATE = 300;
    // Number of frames to create background model
    private static final long INITIAL_NUM_FRAMES = 10;
    // Number of last recording seconds to delete (they usually contains noise)
    private static final long NUM_LAST_SEC_TO_DELETE = DateTimeUtils.T_5_SECONDS;
    // Weather refresh rate
    private static final long WEATHER_REFRESH_RATE = DateTimeUtils.T_15_MINUTES;

    // Service stuff
    private static MonitoringService instance = null;
    private final IBinder binder = new MonitoringBinder();

    // Persistence
    private GoBeesRepository goBeesRepository;
    private SaveRecordingCallback callback;
    private LinkedList<Record> records;

    // Camera and algorithm
    private AndroidCamera androidCamera;
    private boolean openCvLoaded = false;
    private BeesCounter bc;
    private int initialNumFrames;
    private long startTime;

    // Weather
    private Timer timer;
    private FetchWeatherTask fetchWeatherTask;

    // Model info
    private Apiary apiary;
    private MonitoringSettings monitoringSettings;

    /**
     * Checks whether the service is running.
     *
     * @return status.
     */
    public static boolean isRunning() {
        return instance != null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Init record list
        records = new LinkedList<>();
        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();
        // Create fetch weather task
        fetchWeatherTask = new FetchWeatherTask();
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // START action
        if (intent.getAction().equals(START_ACTION)) {
            // Calculate start time (to be use in chronometer)
            Date now = new Date();
            long elapsedRealTimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
            startTime = now.getTime() - elapsedRealTimeOffset;
            // Get monitoring config
            monitoringSettings =
                    (MonitoringSettings) intent.getSerializableExtra(ARGUMENT_MON_SETTINGS);
            // Get apiary
            apiary = goBeesRepository.getApiaryBlocking(monitoringSettings.getApiaryId());
            // Configurations
            configBeeCounter();
            configCamera();
            Notification not = configNotification();
            configOpenCv();
            // Start service in foreground
            startForeground(NOTIFICATION_ID, not);

            // STOP action
        } else if (intent.getAction().equals(STOP_ACTION)) {
            // Release camera
            androidCamera.release();
            // Save records
            if (!records.isEmpty()) {
                // Clean records
                cleanRecords();
                // Save records on db
                goBeesRepository.saveRecords(monitoringSettings.getHiveId(), records,
                        new SaveRecordingCallback() {
                            @Override
                            public void onRecordingTooShort() {
                                stopService();
                                callback.onRecordingTooShort();
                            }

                            @Override
                            public void onSuccess() {
                                stopService();
                                callback.onSuccess();
                            }

                            @Override
                            public void onFailure() {
                                stopService();
                                callback.onFailure();
                            }
                        });
            } else {
                stopService();
                callback.onRecordingTooShort();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        // Release camera
        if (androidCamera != null && androidCamera.isConnected()) {
            androidCamera.release();
            androidCamera = null;
        }
        // Stop fetching weather data
        if (timer != null) {
            timer.cancel();
            timer = null;
            fetchWeatherTask = null;
        }
        // Close database
        goBeesRepository.closeDb();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean isOpenCvLoaded() {
        return openCvLoaded;
    }

    @Override
    public void onCameraStarted(int width, int height) {
        // Counter for creating background model with the first frames
        initialNumFrames = 0;
    }

    @Override
    public void onPreviewFrame(CameraFrame cameraFrame) {
        if (initialNumFrames < INITIAL_NUM_FRAMES) {
            // To create background model
            bc.countBees(cameraFrame.gray());
            bc.getProcessedFrame().release();
            initialNumFrames++;
            return;
        } else if (initialNumFrames == INITIAL_NUM_FRAMES) {
            // After creating background model, set real configuration
            androidCamera.updateFrameRate(0, monitoringSettings.getFrameRate());
            initialNumFrames++;
        }
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
        return startTime + INITIAL_DELAY;
    }

    /**
     * Config bee counter with settings.
     */
    private void configBeeCounter() {
        bc = AreaBeesCounter.getInstance();
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
                monitoringSettings.getZoomRatio(),
                INITIAL_DELAY,
                INITIAL_FRAME_RATE);
    }

    /**
     * Config notification.
     */
    private Notification configNotification() {
        // Intent to the monitoring activity (when the notification is clicked)
        Intent monitoringIntent = new Intent(this, MonitoringActivity.class);
        monitoringIntent.putExtra(MonitoringFragment.ARGUMENT_APIARY_ID,
                monitoringSettings.getApiaryId());
        monitoringIntent.putExtra(MonitoringFragment.ARGUMENT_HIVE_ID,
                monitoringSettings.getHiveId());
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
     * Config OpenCV (config callback and init OpenCV).
     * When OpenCV is ready, it starts monitoring.
     */
    private void configOpenCv() {
        // OpenCV callback
        BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(final int status) {
                if (status == LoaderCallbackInterface.SUCCESS) {
                    openCvLoaded = true;
                    startMonitoring();
                } else {
                    super.onManagerConnected(status);
                }
            }
        };
        // Init openCV
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, loaderCallback);
    }

    /**
     * Start monitoring (frames will be received via onPreviewFrame()).
     */
    private void startMonitoring() {
        // If apiary has location -> Start fetching weather data (each WEATHER_REFRESH_RATE)
        if (apiary.hasLocation()) {
            timer.scheduleAtFixedRate(fetchWeatherTask,
                    getTotalInitialDelay(), WEATHER_REFRESH_RATE);
        }
        // Start camera
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
     * Delete last records that usually contain noise and add two special records
     * at the beginning and ending to know the limits of the recording.
     */
    private void cleanRecords() {
        // Delete last seconds
        long endTime = records.getLast().getTimestamp().getTime();
        while (!records.isEmpty()
                && endTime - records.getLast().getTimestamp().getTime() < NUM_LAST_SEC_TO_DELETE) {
            records.removeLast();
        }
        // Save initial and last record (to know the beginning and ending of the recording)
        if (!records.isEmpty()) {
            records.getFirst().setNumBees(-1);
            records.getLast().setNumBees(-1);
        }
    }

    /**
     * Get total initial delay: camera delay + creation of background model + 5 sec (of margin).
     *
     * @return total initial delay
     */
    private long getTotalInitialDelay() {
        return INITIAL_DELAY + INITIAL_FRAME_RATE * INITIAL_NUM_FRAMES + DateTimeUtils.T_5_SECONDS;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class MonitoringBinder extends Binder {
        MonitoringService getService(SaveRecordingCallback c) {
            callback = c;
            // Return this instance of MonitoringService so clients can call public methods
            return MonitoringService.this;
        }
    }

    /**
     * Task that makes a request to weather server and stores the received weather data.
     */
    private class FetchWeatherTask extends TimerTask {
        @Override
        public void run() {
            goBeesRepository.getAndSaveMeteoRecord(apiary, new GoBeesDataSource.TaskCallback() {
                @Override
                public void onSuccess() {
                    // Don't do anything
                }

                @Override
                public void onFailure() {
                    // Don't do anything
                }
            });
        }
    }
}
