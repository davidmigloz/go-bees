package com.davidmiguel.gobees.video;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * A BeeCounter has to count the number of bees present in a given frame.
 */
public interface BeesCounter {

    /**
     * Count the number of bees in a frame.
     * @param   frame the target frame in CV_8UC1 Mat format.
     * @return  number of bees counted.
     */
    int countBees(Mat frame);


    /**
     * Get a processed frame with the bees highlighted in green and the rest of contours in red.
     * @return CV_8UC3 Mat with bees highlighted
     */
    Mat getProcessedFrame();
}