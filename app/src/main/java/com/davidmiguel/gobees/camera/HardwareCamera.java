package com.davidmiguel.gobees.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the Android camera to be used in a service.
 * Based on:
 * - http://stackoverflow.com/a/24206165/6305235.
 * - http://stackoverflow.com/q/2386025/6305235.
 * Notes:
 * Camera on android platform can't stream video until it given valid preview surface.
 * We use an invisible SurfaceTexture as preview.
 */
@SuppressWarnings("deprecation")
public class HardwareCamera implements ICamera, Camera.PreviewCallback {

    private static final boolean USE_THREAD = true;

    private final static String TAG = "HardwareCamera";
    private final Context context;
    private final int cameraIndex;

    private final ICameraAccess user;
    private final CameraHandlerThread mThread;
    private Camera mCamera;
    private int mFrameWidth;
    private int mFrameHeight;
    private CameraFrame mCameraFrame;
    private SurfaceTexture texture = new SurfaceTexture(0);

    private byte[] mBuffer;

    public HardwareCamera(Context context, ICameraAccess user, int cameraIndex) {
        this.context = context;
        this.cameraIndex = cameraIndex;
        this.user = user;
        this.mThread = USE_THREAD ? new CameraHandlerThread(this) : null;
    }

    /**
     * Get an instance of the camera that meets the requirements.
     *
     * @param facing CameraInfo.CAMERA_FACING_FRONT or CameraInfo.CAMERA_FACING_BACK.
     * @return camera instance or null if it is not available.
     */
    public static Camera getCameraInstance(int facing) {
        Camera camera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        int index = -1;
        // Get desired camera
        for (int camIndex = 0; camIndex < cameraCount; camIndex++) {
            Camera.getCameraInfo(camIndex, cameraInfo);
            if (cameraInfo.facing == facing) {
                try {
                    camera = Camera.open(camIndex);
                    index = camIndex;
                    break;
                } catch (RuntimeException e) {
                    Log.e(TAG, String.format(
                            "Camera is not available (in use or does not exist). " +
                                    "Facing: %s Index: %s Error: %s",
                            facing, camIndex, e.getMessage()));
                }
            }
        }
        // Check if the camera is opened
        if (camera != null) {
            Log.d(TAG, String.format("Camera opened. Facing: %s Index: %s", facing, index));
        } else {
            Log.e(TAG, "Could not find any camera matching facing: " + facing);
        }

        return camera;
    }

    private synchronized void connectLocalCamera() {
        if (!user.isOpenCVLoaded())
            return;

        if (USE_THREAD) {
            synchronized (mThread) {
                mThread.openCamera();
            }
        } else {
            oldConnectCamera();
        }
    }

    @SuppressWarnings("ConstantConditions")
    void oldConnectCamera() {
        // Get camera instance
        mCamera = getCameraInstance(cameraIndex);
        if (mCamera == null) {
            return;
        }

        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();

        // Config preview size
        Collections.sort(sizes, new PreviewSizeComparer());
        Camera.Size previewSize = null;
        for (Camera.Size s : sizes) {
            if (s == null) {
                break;
            }
            previewSize = s;
        }
        params.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setParameters(params);

        // Config frame size
        params = mCamera.getParameters();
        mFrameWidth = params.getPreviewSize().width;
        mFrameHeight = params.getPreviewSize().height;

        // Get frame size in bytes
        int size = mFrameWidth * mFrameHeight
                * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;

        // Create frame buffer
        this.mBuffer = new byte[size];
        Log.d(TAG, "Created callback buffer of size (bytes): " + size);

        // Create frame mat
        Mat mFrame = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);
        mCameraFrame = new CameraFrame(mFrame, mFrameWidth, mFrameHeight);

        // Config texture
        if (this.texture != null) {
            this.texture.release();
        }
        this.texture = new SurfaceTexture(0);

        // Set camera config
        try {
            mCamera.setPreviewTexture(texture);
            mCamera.addCallbackBuffer(mBuffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.startPreview();
            Log.d(TAG, String.format(
                    "Camera preview started with %sx%s. " +
                            "Rendering to SurfaceTexture dummy while receiving preview frames.",
                    mFrameWidth, mFrameHeight));
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void onPreviewFrame(byte[] frame, Camera camera) {
        mCameraFrame.put(frame);
        user.onPreviewFrame(mCameraFrame);
        if (mCamera != null) {
            mCamera.addCallbackBuffer(mBuffer);
        }
    }

    @Override
    public void connect() {
        connectLocalCamera();
    }

    @Override
    public void release() {
        synchronized (this) {
            // Release thread
            if (USE_THREAD) {
                if (mThread != null) {
                    mThread.interrupt();
                }
            }
            // Release camera
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                try {
                    mCamera.setPreviewTexture(null);
                } catch (IOException e) {
                    Log.e(TAG, "Could not release preview-texture from camera.");
                }
                mCamera.release();
                mCamera = null;
                Log.d(TAG, "Preview stopped and camera released");
            }
            // Release camera frame
            if (mCameraFrame != null) {
                mCameraFrame.release();
            }
            // Release texture
            if (texture != null)
                texture.release();
        }
    }

    @Override
    public boolean isConnected() {
        return mCamera != null;
    }
}
