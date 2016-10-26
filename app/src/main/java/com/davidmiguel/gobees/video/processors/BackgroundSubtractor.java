package com.davidmiguel.gobees.video.processors;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

/**
 * Implements a background subtraction algorithm (in particular, BackgroundSubtractorMOG2).
 */
public class BackgroundSubtractor implements VideoProcessor {

    private static final String TAG = "BackgroundSubtractor";

    // Number of frames to consider in the background model
    private static final int    HISTORY = 50;
    // Ratio of frames to add a pixel to the bg model if it keeps semi-constant
    private static final double BACKGROUND_RATIO = 0.04;
    // Threshold to decide whether a pixel belongs to the background model or not
    private static final double VAR_THRESHOLD = 40;
    // Initial variance of each gaussian component
    private static final double VAR_INIT = 15;
    // Detect shadows
    private static final boolean DETECT_SHADOWS = true;
    // Threshold to consider a pixel as shadow or not.
    private static final double SHADOWS_THRESHOLD = 0.7;

    private BackgroundSubtractorMOG2 mog;

    /**
     * Default BackgroundSubtractor constructor.
     * History is initialized to 50 and shadows threshold to 0.7.
     */
    public BackgroundSubtractor() {
        mog = getMogInstance(HISTORY, SHADOWS_THRESHOLD);
    }

    /**
     * BackgroundSubtractor constructor.
     *
     * @param history         the number of frames to consider in the background model.
     * @param shadowThreshold the threshold to consider a pixel as shadow or not.
     */
    public BackgroundSubtractor(int history, double shadowThreshold) {
        mog = getMogInstance(history, shadowThreshold);
    }

    /**
     * Get the instance of BackgroundSubtractorMOG2 with the desired configuration.
     *
     * @param history         the number of frames to consider in the background model.
     * @param shadowThreshold the threshold to consider a pixel as shadow or not.
     * @return instance of BackgroundSubtractorMOG2.
     */
    private BackgroundSubtractorMOG2 getMogInstance(int history, double shadowThreshold) {
        BackgroundSubtractorMOG2 instance = Video.createBackgroundSubtractorMOG2(history, VAR_THRESHOLD, DETECT_SHADOWS);
        instance.setBackgroundRatio(BACKGROUND_RATIO);
        instance.setVarInit(VAR_INIT);
        instance.setShadowThreshold(shadowThreshold);
        return instance;
    }

    @Override
    public Mat process(Mat frame) {
        if (frame == null || frame.empty()) {
            Log.e(TAG, "Invalid input frame.");
            return null;
        }
        Mat foreground = new Mat();
        // Apply background substraction
        mog.apply(frame, foreground);
        return foreground;
    }
}
