package com.davidmiguel.gobees.camera;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Models a camera frame.
 * Based on JavaCameraFrame from OpenCV.
 */
public class CameraFrame {

    private Mat mYuvFrameData;
    private Mat mRgba;
    private int mWidth;
    private int mHeight;
    private boolean mRgbaConverted;

    /**
     * CameraFrame constructor.
     *
     * @param frame  frame Mat where to store the frame data.
     * @param width  frame width.
     * @param height frame height.
     */
    CameraFrame(Mat frame, int width, int height) {
        super();
        mWidth = width;
        mHeight = height;
        mYuvFrameData = frame;
        mRgba = new Mat();
    }

    /**
     * Stores the frame data from a byte array.
     *
     * @param frameData byte array with the data.
     */
    synchronized void putFrameData(byte[] frameData) {
        mYuvFrameData.put(0, 0, frameData);
        invalidate();
    }

    /**
     * Returns single channel gray scale Mat with the frame.
     *
     * @return gray Mat.
     */
    public Mat gray() {
        return mYuvFrameData.submat(0, mHeight, 0, mWidth);
    }

    /**
     * Invalidates cached mat.
     */
    private void invalidate() {
        mRgbaConverted = false;
    }

    /**
     * Returns RGBA Mat with the frame.
     *
     * @return RGBA Mat.
     */
    public Mat rgba() {
        if (!mRgbaConverted) {
            Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2BGR_NV12, 4);
            mRgbaConverted = true;
        }
        return mRgba;
    }

    /**
     * Deallocates frame data.
     */
    public void release() {
        mRgba.release();
    }
}
