package com.davidmiguel.gobees.monitoring;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.davidmiguel.gobees.camera.CameraFrame;
import com.davidmiguel.gobees.camera.ICameraAccess;
import com.davidmiguel.gobees.camera.HardwareCamera;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

/**
 * Monitoring service.
 * It reads the camera feed and run the bee counter algorithm in background.
 */
@SuppressWarnings("deprecation")
public class MonitoringService extends Service implements ICameraAccess {

    private final IBinder mBinder = new MonitoringBinder();
    private HardwareCamera hardwareCamera;
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
    public void onDestroy() {
        super.onDestroy();
        // Release camera
        if (hardwareCamera != null && hardwareCamera.isConnected()) {
                hardwareCamera.release();
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
    public void onPreviewFrame(CameraFrame CameraFrame) {

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
