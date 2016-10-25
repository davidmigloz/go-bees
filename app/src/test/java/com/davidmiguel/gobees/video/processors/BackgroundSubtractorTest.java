package com.davidmiguel.gobees.video.processors;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.davidmiguel.gobees.TestUtils.assertMatEqual;
import static com.davidmiguel.gobees.TestUtils.assertMatNotEqual;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test for BackgroundSubtractor class.
 * The folder with the OpenCV binaries must be on PATH variable.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class BackgroundSubtractorTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private Mat source;
    private Mat foreground;
    private Mat black;
    private BackgroundSubtractor bs;

    @Before
    public void setUp() throws Exception {
        withStaticallyMockedLogApi();
        source = new Mat(4, 4, CvType.CV_8U) {
            {
                put(0, 0, 127, 127, 127, 127);
                put(1, 0, 127, 255, 255, 127);
                put(2, 0, 127, 255, 255, 127);
                put(3, 0, 127, 127, 127, 127);
            }
        };
        black = new Mat(4, 4, CvType.CV_8U, new Scalar(0));
        bs = new BackgroundSubtractor();
    }

    @After
    public void tearDown() throws Exception {
        source.release();
        black.release();
        if (foreground != null) {
            foreground.release();
        }
    }

    @Test
    public void processMat() throws Exception {
        // Input 50 equal frames
        for (int i = 0; i < 100; i++) {
            foreground = bs.process(source);
        }
        // Foreground must be all black (no moving elements)
        assertMatEqual(black, foreground);
        // Modify one pixel
        source.put(2, 2, 0);
        foreground = bs.process(source);
        assertMatNotEqual(black, foreground);-
    }

    @Test
    public void processNullMat() throws Exception {
        foreground = bs.process(null);
        assertNull(foreground);
    }

    @Test
    public void processEmptyMat() throws Exception {
        foreground = bs.process(new Mat());
        assertNull(foreground);
    }

    private void withStaticallyMockedLogApi() {
        mockStatic(Log.class);
        when(Log.e(anyString(), anyString())).thenReturn(0);
    }
}