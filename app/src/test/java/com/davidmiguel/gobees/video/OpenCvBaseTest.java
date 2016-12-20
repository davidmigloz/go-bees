package com.davidmiguel.gobees.video;

import org.opencv.core.Core;

/**
 * Load OpenCV Java lib for running the tests that depend on it.
 */
public class OpenCvBaseTest {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
