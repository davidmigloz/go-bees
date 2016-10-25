package com.davidmiguel.gobees.video.processors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static com.davidmiguel.gobees.TestUtils.assertMatEqual;

/**
 * Test for Blur class.
 * The folder with the OpenCV binaries must be on PATH variable.
 */
public class BlurTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private Mat source;
    private Mat target;

    @Before
    public void setUp() throws Exception {
        source = new Mat(4, 4, CvType.CV_8U) {
            {
                put(0, 0, 127, 127, 127, 127);
                put(1, 0, 127, 255, 255, 127);
                put(2, 0, 127, 255, 255, 127);
                put(3, 0, 127, 127, 127, 127);
            }
        };

        target = new Mat(4, 4, CvType.CV_8U) {
            {
                put(0, 0, 177, 182, 182, 177);
                put(1, 0, 182, 188, 188, 182);
                put(2, 0, 182, 188, 188, 182);
                put(3, 0, 177, 182, 182, 177);
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        source.release();
        target.release();
    }

    @Test
    public void process() throws Exception {
        Blur blur = new Blur();
        Mat result = blur.process(source);
        assertMatEqual(target, result);
    }
}