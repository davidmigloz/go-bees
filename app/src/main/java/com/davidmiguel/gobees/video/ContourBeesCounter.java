package com.davidmiguel.gobees.video;

import com.davidmiguel.gobees.video.processors.BackgroundSubtractor;
import com.davidmiguel.gobees.video.processors.Blur;
import com.davidmiguel.gobees.video.processors.ContoursFinder;
import com.davidmiguel.gobees.video.processors.Morphology;

import org.opencv.core.Mat;

/**
 * Counts the number of bees based on the area of detected moving contours.
 */
public class ContourBeesCounter implements BeesCounter {

    private Blur blur;
    private BackgroundSubtractor bs;
    private Morphology morphology;
    private ContoursFinder cf;
    private Mat processedFrame;

    /**
     * Default ContourBeesCounter constructor.
     * History is initialized to 50 and shadows threshold to 0.7.
     * minArea is initialized to 15 and maxArea to 600.
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
    public int countBees(Mat frame) {
        processedFrame = cf.process(morphology.process(bs.process(blur.process(frame))));
        return cf.getNumBees();
    }

    @Override
    public Mat getProcessedFrame() {
        return processedFrame;
    }
}
