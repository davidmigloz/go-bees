package com.davidmiguel.gobees.video;

import android.annotation.SuppressLint;

import com.davidmiguel.gobees.OpenCvBaseTest;
import com.davidmiguel.gobees.TestUtils;

import org.junit.Test;
import org.opencv.core.Mat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static junit.framework.Assert.assertTrue;

/**
 * Integration test of the bee counter algorithm.
 * It calculates the relative error of the algorithm by using the manually counted frames.
 * This relative error is asserted with a max. error threshold.
 */
@SuppressLint("DefaultLocale")
public class ContourBeesCounterTest extends OpenCvBaseTest {

    private static final double MAX_ERROR_THRESHOLD = 0.1;

    /**
     * Case1: average fly activity, some flys, light shadows.
     */
    @Test
    public void case1() throws Exception {
        BeesCounter bc = new ContourBeesCounter(50, 0.7, 30, 600);
        double error = calculateRelativeError(bc, "c14");
        System.out.println("Error c1: " + error);
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
        int expectedNumBeesTotal = 0;
        int numBeesTotal = 0;

        File expectedOutputs = TestUtils.getFileFromPath(this, "res/img/" + dataset + "/expected.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(expectedOutputs))) {
            // Process 10 frames to create background model
            for (String line; i <= 10 && (line = br.readLine()) != null; i++) {
                bc.countBees(readFreame(i, dataset));
            }
            // Compare beesCounter output with the expected output
            int expectedNumBees, numBees;
            for (String line; (line = br.readLine()) != null; i++) {
                // Get number of bees in the frame (expected and output)
                expectedNumBeesTotal += expectedNumBees = Integer.parseInt(line);
                numBeesTotal += numBees = bc.countBees(readFreame(i, dataset));
                // If they are not equal -> save frame to revise
                if (expectedNumBeesTotal != numBeesTotal) {
                    saveFrames(bc.getProcessedFrame(), i, expectedNumBees, numBees);
                }
            }
        }
        // Calculate relative error
        return Math.abs((expectedNumBeesTotal - numBeesTotal) / (double) expectedNumBeesTotal);
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
        TestUtils.saveMatToFile(frame, String.format("%03d_e%d_o%d", id, expectedNumBees, numBees));
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