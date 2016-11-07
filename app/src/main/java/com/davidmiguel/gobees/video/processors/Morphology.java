package com.davidmiguel.gobees.video.processors;

import android.support.annotation.NonNull;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Perform morphological transformations (erode/dilate) to improve the bees contours.
 */
public class Morphology implements VideoProcessor {

    private static final String TAG = "Morphology";

    private static final int ELEMENT_SHAPE = Imgproc.CV_SHAPE_ELLIPSE;
    private static final int REPETITIONS_DILATE = 3;
    private static final int REPETITIONS_ERODE = 3;

    private Mat kernel1;
    private Mat kernel2;
    private Mat kernel3;

    /**
     * Default constructor. Initializes the kernels.
     */
    public Morphology() {
        kernel1 = Imgproc.getStructuringElement(ELEMENT_SHAPE, new Size(1, 1));
        kernel2 = Imgproc.getStructuringElement(ELEMENT_SHAPE, new Size(2, 2));
        kernel3 = Imgproc.getStructuringElement(ELEMENT_SHAPE, new Size(3, 3));

    }

    @Override
    public Mat process(@NonNull Mat frame) {
        if (frame.empty()) {
            Log.e(TAG, "Invalid input frame.");
            return null;
        }
        Mat tmp = frame.clone();
        // Step 1: erode to remove legs
        Imgproc.erode(tmp, tmp, kernel3);
        // Step 2: dilate to join bodies and heads
        Imgproc.dilate(tmp, tmp, kernel2);
        for (int i = 0; i < REPETITIONS_DILATE; i++) {
            Imgproc.dilate(tmp, tmp, kernel3);
        }
        // Step 3: erode to recover original size
        Imgproc.erode(tmp, tmp, kernel1);
        for (int i = 0; i < REPETITIONS_ERODE; i++) {
            Imgproc.erode(tmp, tmp, kernel3);
        }
        return tmp;
    }
}
