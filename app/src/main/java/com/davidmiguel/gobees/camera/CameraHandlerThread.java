package com.davidmiguel.gobees.camera;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

/**
 * Camera handler thread.
 */
class CameraHandlerThread extends HandlerThread {
    private static final String TAG = CameraHandlerThread.class.getSimpleName();

    private Handler mHandler;
    private HardwareCamera owner;

    CameraHandlerThread(HardwareCamera owner) {
        super("CameraHandlerThread");
        this.owner = owner;
        start();
        mHandler = new Handler(getLooper());
    }

    void openCamera() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                owner.oldConnectCamera();
                notifyCameraOpened();
            }
        });

        try {
            wait();
        } catch (InterruptedException e) {
            Log.w(TAG, "wait was interrupted");
        }
    }

    private synchronized void notifyCameraOpened() {
        notify();
    }
}
