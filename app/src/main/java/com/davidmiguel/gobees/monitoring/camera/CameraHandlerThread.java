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

package com.davidmiguel.gobees.monitoring.camera;

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
    private boolean running;

    CameraHandlerThread(AndroidCameraImpl owner) {
        super("CameraHandlerThread");
        this.owner = owner;
        start();
        mHandler = new Handler(getLooper());
    }

    /**
     * Starts
     */
    synchronized void openCamera() {
        running = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                owner.initCamera();
                notifyCameraOpened();
            }
        });

        try {
            while (running) {
                wait();
            }
        } catch (InterruptedException e) {
            Log.d(TAG, "Thread was interrupted.", e);
            Thread.currentThread().interrupt();
        }
    }

    private synchronized void notifyCameraOpened() {
        running = false;
        notifyAll();
    }
}
