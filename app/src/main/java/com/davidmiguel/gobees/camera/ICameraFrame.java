package com.davidmiguel.gobees.camera;

import android.graphics.Bitmap;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

/**
 * Defines how to implement a camera frame.
 */
public interface ICameraFrame extends CameraBridgeViewBase.CvCameraViewFrame {
    @Override
    Mat rgba();

    @Override
    Mat gray();
}
