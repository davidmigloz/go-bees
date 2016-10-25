package com.davidmiguel.gobees;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.junit.Assert.assertTrue;

/**
 * Utility testing methods.
 */
public class TestUtils {

    public static void assertMatEqual(Mat expected, Mat actual) {
        assertTrue(equals(expected, actual));
    }

    public static void assertMatNotEqual(Mat expected, Mat actual) {
        assertTrue(!equals(expected, actual));
    }

    private static boolean equals(Mat expected, Mat actual) {
        if (expected.type() != actual.type() || expected.cols() != actual.cols() || expected.rows() != actual.rows()) {
            throw new UnsupportedOperationException("Can not compare " + expected + " and " + actual);
        }else if (expected.depth() == CvType.CV_32F || expected.depth() == CvType.CV_64F) {
            throw new UnsupportedOperationException("Floating-point mats must not be checked for exact match.");
        }
        // Subtract matrices
        Mat diff = new Mat();
        Core.absdiff(expected, actual, diff);
        // Count non zero pixels
        Mat reshaped = diff.reshape(1); // One channel
        int mistakes = Core.countNonZero(reshaped);
        // Free
        reshaped.release();
        diff.release();
        // Check mistakes
        return 0 == mistakes;
    }
}
