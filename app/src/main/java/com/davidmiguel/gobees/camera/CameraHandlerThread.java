package com.davidmiguel.gobees.camera;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

/**
 * AndroidCamera handler thread.
 */
class CameraHandlerThread extends HandlerThread {
    private static final String TAG = CameraHandlerThread.class.getSimpleName();

    private Handler mHandler;
    private AndroidCameraImpl owner;

    CameraHandlerThread(AndroidCameraImpl owner) {
        super("CameraHandlerThread");
        this.owner = owner;
        start();
        mHandler = new Handler(getLooper());
    }

    /**
     * Starts
     */
    void openCamera() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                owner.initCamera();
                notifyCameraOpened();
            }
        });

        try {
            wait();
        } catch (InterruptedException e) {
            Log.d(TAG, "Thread was interrupted.");
        }
    }

    private synchronized void notifyCameraOpened() {
        notify();
    }
}
