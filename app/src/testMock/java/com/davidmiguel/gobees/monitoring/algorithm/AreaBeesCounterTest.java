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

package com.davidmiguel.gobees.monitoring.algorithm;

import android.annotation.SuppressLint;

import com.davidmiguel.gobees.TestUtils;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;

import static junit.framework.Assert.assertTrue;

/**
 * Integration test of the bee counter algorithm.
 * It calculates the relative error of the algorithm by using the manually counted frames.
 * This relative error is asserted with a max. error threshold.
 */
@SuppressLint("DefaultLocale")
public class AreaBeesCounterTest extends OpenCvBaseTest {

    private static final String LOGGER_PROP = "src/testMock/res/log4j.properties";
    private static final double MAX_ERROR_THRESHOLD = 0.1;
    private static final int NUM_FRAMES_SKIP = 10;
    private final Logger logger = LoggerFactory.getLogger(AreaBeesCounterTest.class);
    private DecimalFormat df;

    @BeforeClass
    public static void setLogger() {
        PropertyConfigurator.configure(LOGGER_PROP);
    }

    @Before
    public void setUp() throws Exception {
        df = new DecimalFormat("##0.00%");
    }

    /**
     * Dataset c14: 100 frames, average fly activity, some flys, light shadows.
     */
    @Test
    public void case1() throws Exception {
        logger.debug("Case1:");
        BeesCounter bc = AreaBeesCounter.getInstance();
        bc.updateBlobSize(BeesCounter.BlobSize.NORMAL);
        double error = calculateRelativeError(bc, "c14");
        System.out.println("Error case1: " + df.format(error));
        assertTrue(error < MAX_ERROR_THRESHOLD);
    }

    /**
     * Dataset c17: 200 frames, average fly activity, some flys, light shadows.
     */
    @Test
    public void case2() throws Exception {
        logger.debug("Case2:");
        BeesCounter bc = AreaBeesCounter.getInstance();
        bc.updateBlobSize(BeesCounter.BlobSize.SMALL);
        bc.updateMinArea(10.0);
        bc.updateMaxArea(800.0);
        double error = calculateRelativeError(bc, "c17");
        System.out.println("Error case2: " + df.format(error));
        assertTrue(error < MAX_ERROR_THRESHOLD);
    }

    /**
     * Dataset c5: 100 frames, high fly activity, not optimum recorded.
     */
    @Test
    public void case3() throws Exception {
        logger.debug("Case3:");
        BeesCounter bc = AreaBeesCounter.getInstance();
        bc.updateBlobSize(BeesCounter.BlobSize.BIG);
        bc.updateMinArea(30.0);
        bc.updateMaxArea(2000.0);
        double error = calculateRelativeError(bc, "c5");
        System.out.println("Error case3: " + df.format(error));
        assertTrue(error < MAX_ERROR_THRESHOLD);
    }

    /**
     * Calculate the relative error from the output of the algorithm and the expected values
     * obtained manually.
     *
     * @param bc      bees counter instance.
     * @param dataset dataset of frames to process.
     * @return relative error.
     */
    @SuppressWarnings({"UnusedAssignment", "unused"})
    private double calculateRelativeError(BeesCounter bc, String dataset) throws Exception {
        int i = 1;
        long totalAbsoluteError = 0;
        long expectedNumBeesTotal = 0;

        File expectedOutputs =
                TestUtils.getFileFromPath(this, "res/img/" + dataset + "/numBees.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(expectedOutputs))) {
            // Process NUM_FRAMES_SKIP frames to create background model
            for (String line; i <= NUM_FRAMES_SKIP && (line = br.readLine()) != null; i++) {
                bc.countBees(readFreame(i, dataset));
            }
            // Compare beesCounter output with the expected output
            int expectedNumBees;
            int numBees;
            for (String line; (line = br.readLine()) != null; i++) {
                // Get number of bees in the frame (expected and output)
                expectedNumBeesTotal += expectedNumBees = Integer.parseInt(line);
                numBees = bc.countBees(readFreame(i, dataset));
                // Calculate and log absolute error
                int absoluteError = expectedNumBees - numBees;
                logger.debug("{}:{}", i, absoluteError);
                totalAbsoluteError += Math.abs(absoluteError);
                // If they are not equal -> save frame to revise
                if (expectedNumBees != numBees) {
                    saveFrames(bc.getProcessedFrame(), i, expectedNumBees, numBees);
                }
            }
        }
        // Calculate relative error
        return totalAbsoluteError / (double) expectedNumBeesTotal;
    }

    /**
     * Saves a mat to jpg file with the following name:
     * {id}_e{expectedNumBees}_o{numBees}.jpg
     * Ex: 001_e5_o4.jpg
     *
     * @param frame           mat to save.
     * @param id              id of the frame.
     * @param expectedNumBees expected number of bees.
     * @param numBees         output number of bees.
     */
    private void saveFrames(Mat frame, int id, int expectedNumBees, int numBees) {
        TestUtils.saveMatToFile(frame,
                String.format("/img/%03d_e%d_o%d", id, expectedNumBees, numBees));
    }

    /**
     * Reads an image from test resources and returns it as a OpenCV Mat.
     *
     * @param i       index of the image.
     * @param dataset dataset that belongs the image.
     * @return mat of the image.
     */
    private Mat readFreame(int i, String dataset) {
        return TestUtils.loadGrayImage("res/img/" + dataset + String.format("/%03d", i) + ".jpg");
    }
}