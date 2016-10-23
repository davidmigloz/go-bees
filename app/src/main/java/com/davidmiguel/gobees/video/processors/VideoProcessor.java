package com.davidmiguel.gobees.video.processors;

import org.opencv.core.Mat;

/**
 * A VideoProcessor process a frame in accordance with some algorithm.
 */
interface VideoProcessor {

    /**
     * Process a frame in accordance with some algorithm.
     * @param frame input frame
     * @return processed frame
     */
    Mat process(Mat frame);

}
