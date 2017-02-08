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

package com.davidmiguel.gobees.monitoring.algorithm.processors;

import com.davidmiguel.gobees.monitoring.algorithm.OpenCvBaseTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static com.davidmiguel.gobees.TestUtils.assertMatEqual;
import static com.davidmiguel.gobees.TestUtils.assertMatNotEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for Blur, BacakgroundSubtractor, Morphology and ContourFinder classes.
 * OpenCV 3.1.0 native lib must be on PATH environment variable.
 */
public class ProcessorsTest extends OpenCvBaseTest {

    private Mat source;
    private Mat sourceContours;
    private Mat targetBlur;
    private Mat result;
    private Mat black;

    private Blur blur;
    private BackgroundSubtractor bs;
    private Morphology morf;
    private ContoursFinder cf;

    /**
     * Create needed mats and instances.
     */
    @Before
    public void setUp() throws Exception {
        // Image to test
        source = new Mat(4, 4, CvType.CV_8U) {
            {
                put(0, 0, 0, 0, 0, 0);
                put(1, 0, 0, 255, 255, 0);
                put(2, 0, 0, 255, 255, 0);
                put(3, 0, 0, 0, 0, 0);
            }
        };
        // Image with a circle with area=314 (simulating a bee)
        sourceContours = new Mat(480, 640, CvType.CV_8U, new Scalar(0));
        Imgproc.circle(sourceContours, new Point(200, 200), 10, new Scalar(255), -1);
        // Expected result for blur
        targetBlur = new Mat(4, 4, CvType.CV_8U) {
            {
                put(0, 0, 100, 110, 110, 100);
                put(1, 0, 110, 120, 120, 110);
                put(2, 0, 110, 120, 120, 110);
                put(3, 0, 100, 110, 110, 100);
            }
        };
        // Black image
        black = new Mat(4, 4, CvType.CV_8U, new Scalar(0));
        // Instaciate classes
        blur = new Blur();
        bs = new BackgroundSubtractor(); // To test default constructor
        bs = new BackgroundSubtractor(50, 0.7);
        morf = new Morphology();
        cf = new ContoursFinder(); // To test default constructor
        cf = new ContoursFinder(16, 600);
    }

    /**
     * Release mats.
     */
    @After
    public void tearDown() throws Exception {
        source.release();
        sourceContours.release();
        black.release();
        targetBlur.release();
        if (result != null) {
            result.release();
        }
    }

    @Test
    public void testBlur() throws Exception {
        result = blur.process(source);
        assertMatEqual(targetBlur, result);
    }

    @Test
    public void testBackgroundSub() throws Exception {
        // Input 50 equal frames
        for (int i = 0; i < 100; i++) {
            result = bs.process(source);
        }
        // Foreground must be all black (no moving elements)
        assertMatEqual(black, result);
        // Modify one pixel
        Mat mod = source.clone();
        mod.put(2, 2, 0);
        result = bs.process(mod);
        assertMatNotEqual(black, result);
    }

    @Test
    public void testMorphology() throws Exception {
        for (int i = 1; i <= 3; i++) {
            morf.setDilateKernel(i);
            result = morf.process(source);
            assertMatEqual(black, result);
        }
        for (int i = 1; i <= 3; i++) {
            morf.setErodeKernel(i);
            result = morf.process(source);
            assertMatEqual(black, result);
        }
    }

    @Test
    public void testContoursFinder() throws Exception {
        cf.process(sourceContours);
        int num = cf.getNumBees();
        assertEquals(1, num);
        // Add another bee
        Imgproc.circle(sourceContours, new Point(150, 150), 10, new Scalar(255), -1);
        cf.process(sourceContours);
        num = cf.getNumBees();
        assertEquals(2, num);
        // Add an object with area out of rage (max)
        Imgproc.circle(sourceContours, new Point(300, 300), 15, new Scalar(255), -1);
        cf.process(sourceContours);
        num = cf.getNumBees();
        assertEquals(2, num);
        // Add an object with area out of rage (min)
        Imgproc.circle(sourceContours, new Point(50, 50), 2, new Scalar(255), -1);
        cf.process(sourceContours);
        num = cf.getNumBees();
        assertEquals(2, num);
    }

    @Test
    public void testeEmptyMat() throws Exception {
        result = blur.process(new Mat());
        assertNull(result);
        result = bs.process(new Mat());
        assertNull(result);
        result = morf.process(new Mat());
        assertNull(result);
        result = cf.process(new Mat());
        assertNull(result);
    }
}
