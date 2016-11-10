package com.davidmiguel.gobees.video;

import android.support.annotation.NonNull;
import android.util.Log;

import com.davidmiguel.gobees.video.processors.BackgroundSubtractor;
import com.davidmiguel.gobees.video.processors.Blur;
import com.davidmiguel.gobees.video.processors.ContoursFinder;
import com.davidmiguel.gobees.video.processors.Morphology;

import org.opencv.core.Mat;

/**
 * Counts the number of bees based on the area of detected moving contours.
 */
public class ContourBeesCounter implements BeesCounter {

    private static final String TAG = "ContourBeesCounter";

    private Blur blur;
    private BackgroundSubtractor bs;
    private Morphology morphology;
    private ContoursFinder cf;
    private Mat processedFrame;

    /**
     * Default ContourBeesCounter constructor.
     * History is initialized to 10 and shadows threshold to 0.7.
     * minArea is initialized to 15 and maxArea to 800.
     */
    public ContourBeesCounter() {
        blur = new Blur();
        bs = new BackgroundSubtractor();
        morphology = new Morphology();
        cf = new ContoursFinder();
    }

    /**
     * ContourBeesCounter constructor.
     *
     * @param history         the number of frames to consider in the background model.
     * @param shadowThreshold the threshold to consider a pixel as shadow or not.
     * @param minArea         the min area to consider a contour a bee.
     * @param maxArea         the max area to consider a contour a bee.
     */
    public ContourBeesCounter(int history, double shadowThreshold, double minArea, double maxArea) {
        blur = new Blur();
        bs = new BackgroundSubtractor(history, shadowThreshold);
        morphology = new Morphology();
        cf = new ContoursFinder(minArea, maxArea);
    }

    @Override
    public int countBees(@NonNull Mat frame) {
        final long t0 = System.nanoTime();
        Mat r0 = blur.process(frame);
        Mat r1 = bs.process(r0);
        Mat r2 = morphology.process(r1);
        processedFrame = cf.process(r2);
        r0.release();
        r1.release();
        r2.release();
        Log.d(TAG, "countBees time: " + (System.nanoTime() - t0) / 1000000 );
        return cf.getNumBees();
    }

    @Override
    public Mat getProcessedFrame() {
        return processedFrame;
    }
}
