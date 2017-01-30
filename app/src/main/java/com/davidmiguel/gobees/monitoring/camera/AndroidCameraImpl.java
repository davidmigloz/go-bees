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

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of the Android camera that retrieves the frames in OpenCV Mat format.
 * Based on:
 * - http://stackoverflow.com/a/24206165/6305235.
 * - http://stackoverflow.com/q/2386025/6305235.
 * Notes:
 * OpenCV JavaCameraView cannot be used in a service because it is a view. That's why we have
 * to implement our own camera to retrieve the frames in Mat format.
 * AndroidCamera on android platform can't stream video until it given valid preview surface.
 * We use an invisible SurfaceTexture as preview.
 * The camera is handled in a different thread.
 */
@SuppressWarnings("deprecation")
public class AndroidCameraImpl implements AndroidCamera, Camera.PreviewCallback {

    private static final String TAG = "AndroidCameraImpl";

    private final AndroidCameraListener user;
    private final CameraHandlerThread cameraHandlerThread;
    private final int cameraIndex;
    private android.hardware.Camera camera;
    private int maxframewidth;
    private int maxFrameHeight;
    private int zoomRatio;
    private long initialDelay;
    private long frameRate;
    private TakePhotoTask takePhotoTask;
    private Timer timer;
    private CameraFrame cameraFrame;
    private SurfaceTexture texture = new SurfaceTexture(0);

    /**
     * AndroidCameraImpl constructor.
     *
     * @param user           camera client.
     * @param cameraIndex    desired camera
     *                       (CameraInfo.CAMERA_FACING_FRONT or CameraInfo.CAMERA_FACING_BACK...).
     * @param maxFrameWidth  max. frame width.
     * @param maxFrameHeight max. frame height.
     * @param zoomRatio      zoom ratio of the desired zoom value.
     */
    public AndroidCameraImpl(AndroidCameraListener user, int cameraIndex,
                             int maxFrameWidth, int maxFrameHeight, int zoomRatio,
                             long initialDelay, long frameRate) {
        this.user = user;
        this.cameraIndex = cameraIndex;
        this.maxframewidth = maxFrameWidth;
        this.maxFrameHeight = maxFrameHeight;
        this.zoomRatio = zoomRatio;
        this.initialDelay = initialDelay;
        this.frameRate = frameRate;
        this.cameraHandlerThread = new CameraHandlerThread(this);
        this.takePhotoTask = new TakePhotoTask();
        this.timer = new Timer();
    }

    @Override
    public void onPreviewFrame(byte[] frame, android.hardware.Camera camera) {
        cameraFrame.putFrameData(frame);
        user.onPreviewFrame(cameraFrame);
    }

    @Override
    public synchronized void connect() {
        if (!user.isOpenCvLoaded()) {
            return;
        }
        // Start camera
        synchronized (cameraHandlerThread) {
            cameraHandlerThread.openCamera();
        }
    }

    @Override
    public void release() {
        synchronized (this) {
            // Stop task
            timer.cancel();
            timer = null;
            takePhotoTask = null;
            // Release thread
            cameraHandlerThread.interrupt();
            // Release camera
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                try {
                    camera.setPreviewTexture(null);
                } catch (IOException e) {
                    Log.e(TAG, "Could not release preview-texture from camera.", e);
                }
                camera.release();
                camera = null;
            }
            // Release camera frame
            if (cameraFrame != null) {
                cameraFrame.release();
            }
            // Release texture
            if (texture != null) {
                texture.release();
            }
        }
    }

    @Override
    public boolean isConnected() {
        return camera != null;
    }

    @Override
    public void updateFrameRate(long delay, long period) {
        this.timer.cancel();
        this.timer = new Timer();
        this.takePhotoTask = new TakePhotoTask();
        this.initialDelay = delay;
        this.frameRate = period;
        timer.scheduleAtFixedRate(takePhotoTask, delay, period);
    }

    /**
     * Starts capturing and converting preview frames.
     */
    @SuppressWarnings("ConstantConditions")
    void initCamera() {
        // Get camera instance
        camera = getCameraInstance(cameraIndex, maxframewidth, maxFrameHeight, zoomRatio);
        if (camera == null) {
            return;
        }
        // Save frame size
        Camera.Parameters params = camera.getParameters();
        int mFrameWidth = params.getPreviewSize().width;
        int mFrameHeight = params.getPreviewSize().height;
        // Create frame mat
        Mat mFrame = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);
        cameraFrame = new CameraFrame(mFrame, mFrameWidth, mFrameHeight);
        // Config texture
        if (this.texture != null) {
            this.texture.release();
        }
        this.texture = new SurfaceTexture(0);
        // Call onCameraStart
        user.onCameraStarted(mFrameWidth, mFrameHeight);
        // Set camera callbacks and start capturing
        try {
            camera.setPreviewTexture(texture);
            camera.startPreview();
            timer.scheduleAtFixedRate(takePhotoTask, initialDelay, frameRate);
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage(), e);
        }
    }

    /**
     * Get an instance of the camera that meets the requirements (facing, size, zoom).
     *
     * @param facing CameraInfo.CAMERA_FACING_FRONT or CameraInfo.CAMERA_FACING_BACK.
     * @return camera instance or null if it is not available.
     */
    private Camera getCameraInstance(int facing, int maxFrameWidth,
                                     int maxFrameHeight, int zoomRatio) {
        // Get desired camera
        Camera cam = getCamera(facing);
        if (cam == null) {
            Log.e(TAG, "Could not find any camera matching facing: " + facing);
            return null;
        }
        // Set frame size
        setFrameSize(cam, maxFrameWidth, maxFrameHeight);
        // Set zoom ratio
        setZoomRatio(cam, zoomRatio);
        // Set autofocus
        setAutofocus(cam);
        return cam;
    }

    /**
     * Gets and opens the desired camera if it exists.
     *
     * @param facing CameraInfo.CAMERA_FACING_FRONT or CameraInfo.CAMERA_FACING_BACK.
     * @return Camera (or null if it does not exist).
     */
    private Camera getCamera(int facing) {
        Camera cam = null;
        CameraInfo cameraInfo = new CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camIndex = 0; camIndex < cameraCount; camIndex++) {
            Camera.getCameraInfo(camIndex, cameraInfo);
            if (cameraInfo.facing == facing) {
                try {
                    // Open camera
                    cam = Camera.open(camIndex);
                    break;
                } catch (RuntimeException e) {
                    Log.e(TAG, "AndroidCamera is not available (in use or does not exist).", e);
                }
            }
        }
        return cam;
    }

    /**
     * Sets the frame size with the given values.
     *
     * @param camera         camera to configure.
     * @param maxFrameWidth  max frame width (the closest smaller width will be chosen).
     * @param maxFrameHeight max frame height (the closest smaller height will be chosen).
     */
    private void setFrameSize(Camera camera, int maxFrameWidth, int maxFrameHeight) {
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Collections.sort(sizes, new PreviewSizeComparator());
        Camera.Size previewSize = null;
        for (Camera.Size size : sizes) {
            if (size == null || size.width > maxFrameWidth || size.height > maxFrameHeight) {
                break;
            }
            previewSize = size;
        }
        if (previewSize == null) {
            Log.e(TAG, "Could not find any camera matching sizes: "
                    + maxFrameWidth + "x" + maxFrameHeight);
            return;
        }
        params.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(params);
    }

    /**
     * Sets the zoom ratio with the given value.
     *
     * @param camera    camera to configure.
     * @param zoomRatio zoom ratio.
     */
    private void setZoomRatio(Camera camera, int zoomRatio) {
        Camera.Parameters params = camera.getParameters();
        if (params.isZoomSupported()) {
            // Get supported ratios
            List<Integer> zoomRatios = params.getZoomRatios();
            // Chose closest ratio
            int i;
            for (i = 0; i < zoomRatios.size(); i++) {
                if (zoomRatio <= zoomRatios.get(i)) {
                    break;
                }
            }
            // Set zoom
            params.setZoom(i <= params.getMaxZoom() ? i : params.getMaxZoom());
            camera.setParameters(params);
        }
    }

    /**
     * Set focus mode.
     *
     * @param camera camera to configure.
     */
    private void setAutofocus(Camera camera) {
        Camera.Parameters params = camera.getParameters();
        if (params.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        camera.setParameters(params);
    }

    // Timer task for continuous triggering of preview callbacks
    private class TakePhotoTask extends TimerTask {
        @Override
        public void run() {
            camera.setOneShotPreviewCallback(AndroidCameraImpl.this);
        }
    }
}
