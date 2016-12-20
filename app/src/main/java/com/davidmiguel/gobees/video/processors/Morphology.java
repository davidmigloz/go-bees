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
    private static final Mat KERNEL1 = Imgproc.getStructuringElement(ELEMENT_SHAPE, new Size(1, 1));
    private static final Mat KERNEL2 = Imgproc.getStructuringElement(ELEMENT_SHAPE, new Size(2, 2));
    private static final Mat KERNEL3 = Imgproc.getStructuringElement(ELEMENT_SHAPE, new Size(3, 3));

    private Mat kernelErode;
    private Mat kernelDilate;

    /**
     * Default constructor. Initializes the kernels.
     */
    public Morphology() {
        kernelErode = KERNEL3;
        kernelDilate = KERNEL3;
    }

    @Override
    public Mat process(@NonNull Mat frame) {
        if (frame.empty()) {
            Log.e(TAG, "Invalid input frame.");
            return null;
        }
        Mat tmp = frame.clone();
        // Step 1: erode to remove legs
        Imgproc.erode(tmp, tmp, KERNEL3);
        // Step 2: dilate to join bodies and heads
        Imgproc.dilate(tmp, tmp, KERNEL2);
        for (int i = 0; i < REPETITIONS_DILATE; i++) {
            Imgproc.dilate(tmp, tmp, kernelDilate);
        }
        // Step 3: erode to recover original size
        Imgproc.erode(tmp, tmp, KERNEL1);
        for (int i = 0; i < REPETITIONS_ERODE; i++) {
            Imgproc.erode(tmp, tmp, kernelErode);
        }
        return tmp;
    }

    /**
     * Set erode kernel size.
     *
     * @param size size (1, 2 or 3. Default 3).
     */
    public void setErodeKernel(int size) {
        switch (size) {
            case 1:
                kernelErode = KERNEL1;
                break;
            case 2:
                kernelErode = KERNEL2;
                break;
            case 3:
            default:
                kernelErode = KERNEL3;
        }
    }

    /**
     * Set dilate kernel size.
     *
     * @param size size (1, 2 or 3. Default 3).
     */
    public void setDilateKernel(int size) {
        switch (size) {
            case 1:
                kernelDilate = KERNEL1;
                break;
            case 2:
                kernelDilate = KERNEL2;
                break;
            case 3:
            default:
                kernelDilate = KERNEL3;
        }
    }
}
