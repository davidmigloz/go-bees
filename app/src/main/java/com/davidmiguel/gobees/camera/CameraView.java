package com.davidmiguel.gobees.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

import java.util.List;

/**
 * Improved version of OpenCV JavaCameraView.
 * It allows to control the camera zoom.
 * The frame size is set to 640x480.
 */
@SuppressWarnings("deprecation")
public class CameraView extends JavaCameraView {

    private static final int MAX_HEIGHT = 480;
    private static final int MAX_WIDTH = 640;

    private Camera.Parameters params;

    public CameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean initializeCamera(int width, int height) {
        // Set back camera
        this.setCameraIndex(0);
        // Config size
        this.setMaxFrameSize(MAX_WIDTH, MAX_HEIGHT);
        // Initialize camera
        boolean ok = super.initializeCamera(width, height);
        // Get camera parameters
        params = mCamera.getParameters();
        return ok;
    }

    public void setZoom(int ratio) {
        if (params.isZoomSupported()) {
            // Get supported ratios
            List<Integer> zoomRatios = params.getZoomRatios();
            // Chose closest ratio
            int i;
            for (i = 0; i < zoomRatios.size(); i++) {
                if (ratio <= zoomRatios.get(i)) {
                    break;
                }
            }
            // Set zoom
            params.setZoom(i <= params.getMaxZoom() ? i : params.getMaxZoom());
            mCamera.setParameters(params);
        }
    }
}
