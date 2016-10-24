package com.davidmiguel.gobees.video.processors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import static org.junit.Assert.*;

/**
 * Created by davidmigloz on 24/10/2016.
 */
public class BlurTest {

    private Mat source;
    private Mat target;
    @Before
    public void setUp() throws Exception {
        source = new Mat(new Size(640,480), CvType.CV_8U, new Scalar(127));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void process() throws Exception {

    }

}