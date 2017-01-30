/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.davidmiguel.gobees;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertTrue;

/**
 * Utility testing methods.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class TestUtils {

    private static final String DIRECTORY_TEST = "./build/intermediates/classes/test/mock/debug/";

    /**
     * Asserts that the matrices are equal.
     *
     * @param expected expected mat.
     * @param actual   actual mat.
     */
    public static void assertMatEqual(Mat expected, Mat actual) {
        assertTrue(equals(expected, actual));
    }

    /**
     * Asserts that the matrices are not equal.
     *
     * @param expected expected mat.
     * @param actual   actual mat.
     */
    public static void assertMatNotEqual(Mat expected, Mat actual) {
        assertTrue(!equals(expected, actual));
    }

    /**
     * Checks if two OpenCV Mats are equal.
     * The matrices must be equal size and type.
     * Floating-point mats are not supported.
     *
     * @param expected expected mat.
     * @param actual   actual mat.
     * @return true if they are equal.
     */
    private static boolean equals(Mat expected, Mat actual) {
        if (expected.type() != actual.type() || expected.cols() != actual.cols()
                || expected.rows() != actual.rows()) {
            throw new UnsupportedOperationException(
                    "Can not compare " + expected + " and " + actual);
        } else if (expected.depth() == CvType.CV_32F || expected.depth() == CvType.CV_64F) {
            throw new UnsupportedOperationException(
                    "Floating-point mats must not be checked for exact match.");
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

    /**
     * Get a file from a path.
     * Ex: TestUtils.getFileFromPath(this, "res/img/expected.txt");
     *
     * @param obj      actual instance of the class (this).
     * @param fileName path to the file.
     * @return file.
     */
    public static File getFileFromPath(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getPath());
    }

    /**
     * Load image from test directory into OpenCV mat.
     * Ex: TestUtils.loadImage("res/img/frame.jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
     *
     * @param imgPath path to the image (relative from test directory).
     * @param flags   CV_LOAD_IMAGE_ANYDEPTH, CV_LOAD_IMAGE_COLOR, CV_LOAD_IMAGE_GRAYSCALE.
     * @return mat of the image.
     */
    public static Mat loadImage(String imgPath, int flags) {
        return Imgcodecs.imread(DIRECTORY_TEST + imgPath, flags);
    }

    /**
     * Load image from test directory into gray scale OpenCV mat.
     * Ex: TestUtils.loadImage("res/img/frame.jpg");
     *
     * @param imgPath path to the image (relative from test directory).
     * @return mat of the image in gray scale.
     */
    public static Mat loadGrayImage(String imgPath) {
        return loadImage(imgPath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    }

    /**
     * Save mat to a jpg file.
     * Ex: TestUtils.saveMatToFile(frame, "c14/001");
     *
     * @param mat     mat of the image.
     * @param imgPath path where to save the image (relative from test resources directory
     *                and the name without extension).
     */
    public static void saveMatToFile(Mat mat, String imgPath) {
        Imgcodecs.imwrite(DIRECTORY_TEST + "res/" + imgPath + ".jpg", mat);
    }
}
