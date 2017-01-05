package com.davidmiguel.gobees.camera;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Models a camera frame.
 * Based on JavaCameraFrame from OpenCV.
 */
public class CameraFrame {

    private Mat yuvFrameData;
    private Mat rgba;
    private int width;
    private int height;
    private boolean rgbaConverted;

    /**
     * CameraFrame constructor.
     *
     * @param frame  frame Mat where to store the frame data.
     * @param width  frame width.
     * @param height frame height.
     */
    CameraFrame(Mat frame, int width, int height) {
        super();
        this.width = width;
        this.height = height;
        yuvFrameData = frame;
        rgba = new Mat();
    }

    /**
     * Stores the frame data from a byte array.
     *
     * @param frameData byte array with the data.
     */
    synchronized void putFrameData(byte[] frameData) {
        yuvFrameData.put(0, 0, frameData);
        invalidate();
    }

    /**
     * Returns single channel gray scale Mat with the frame.
     *
     * @return gray Mat.
     */
    public Mat gray() {
        return yuvFrameData.submat(0, height, 0, width);
    }

    /**
     * Invalidates cached mat.
     */
    private void invalidate() {
        rgbaConverted = false;
    }

    /**
     * Returns RGBA Mat with the frame.
     *
     * @return RGBA Mat.
     */
    public Mat rgba() {
        if (!rgbaConverted) {
            Imgproc.cvtColor(yuvFrameData, rgba, Imgproc.COLOR_YUV2BGR_NV12, 4);
            rgbaConverted = true;
        }
        return rgba;
    }

    /**
     * Deallocates frame data.
     */
    public void release() {
        rgba.release();
    }
}
