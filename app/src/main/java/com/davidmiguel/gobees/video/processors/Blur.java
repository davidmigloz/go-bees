package com.davidmiguel.gobees.video.processors;

import android.support.annotation.NonNull;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Implements a Blur algorithm (in particular, Gaussian Blur).
 */
public class Blur implements VideoProcessor {

    private static final String TAG = "Blur";

    private static final int REPETITIONS = 2;
    private static final int KERNEL_SIZE = 3;

    @Override
    public Mat process(@NonNull Mat frame) {
        if (frame.empty()) {
            Log.e(TAG, "Invalid input frame.");
            return null;
        }
        Mat tmp = frame.clone();
        // Apply gaussian blur
        for (int i = 0; i < REPETITIONS; i++) {
            Imgproc.GaussianBlur(tmp, tmp, new Size(KERNEL_SIZE, KERNEL_SIZE), 0);
        }
        return tmp;
    }
}
