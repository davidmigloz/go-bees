package com.davidmiguel.gobees.video.processors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import static com.davidmiguel.gobees.TestUtils.assertMatEqual;
import static com.davidmiguel.gobees.TestUtils.assertMatNotEqual;
import static org.junit.Assert.assertNull;

/**
 * Test for Blur, BacakgroundSubtractor, Morphology and ContourFinder classes.
 * OpenCV 3.1.0 native lib must be on PATH environment variable.
 */
public class ProcessorsTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private Mat source;
    private Mat targetBlur;
    private Mat result;
    private Mat black;

    private Blur blur;
    private BackgroundSubtractor bs;
    private Morphology morf;

    @Before
    public void setUp() throws Exception {
        source = new Mat(4, 4, CvType.CV_8U) {
            {
                put(0, 0, 0, 0, 0, 0);
                put(1, 0, 0, 255, 255, 0);
                put(2, 0, 0, 255, 255, 0);
                put(3, 0, 0, 0, 0, 0);
            }
        };
        targetBlur = new Mat(4, 4, CvType.CV_8U) {
            {
                put(0, 0, 100, 110, 110, 100);
                put(1, 0, 110, 120, 120, 110);
                put(2, 0, 110, 120, 120, 110);
                put(3, 0, 100, 110, 110, 100);
            }
        };
        black = new Mat(4, 4, CvType.CV_8U, new Scalar(0));

        blur = new Blur();
        bs = new BackgroundSubtractor();
        morf = new Morphology();
    }

    @After
    public void tearDown() throws Exception {
        source.release();
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
        result = morf.process(source);
        assertMatEqual(black, result);
    }

    @Test
    public void testNullMat() throws Exception {
        result = blur.process(null);
        assertNull(result);
        result = bs.process(null);
        assertNull(result);
        result = morf.process(null);
        assertNull(result);
    }

    @Test
    public void testeEmptyMat() throws Exception {
        result = blur.process(new Mat());
        assertNull(result);
        result = bs.process(new Mat());
        assertNull(result);
        result = morf.process(new Mat());
        assertNull(result);
    }
}
