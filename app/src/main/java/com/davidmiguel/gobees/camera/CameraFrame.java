package com.davidmiguel.gobees.camera;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Model a camera frame.
 * Based on JavaCameraFrame from OpenCV.
 */
public class CameraFrame implements ICameraFrame {

    private Mat mYuvFrameData;
    private Mat mRgba;
    private int mWidth;
    private int mHeight;
    private boolean mRgbaConverted;

    public CameraFrame(Mat Yuv420sp, int width, int height) {
        super();
        mWidth = width;
        mHeight = height;
        mYuvFrameData = Yuv420sp;
        mRgba = new Mat();
    }

    @Override
    public Mat gray() {
        return mYuvFrameData.submat(0, mHeight, 0, mWidth);
    }

    @Override
    public Mat rgba() {
        if (!mRgbaConverted) {
            Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2BGR_NV12, 4);
            mRgbaConverted = true;
        }
        return mRgba;
    }

    public synchronized void put(byte[] frame) {
        mYuvFrameData.put(0, 0, frame);
        invalidate();
    }

    public void release() {
        mRgba.release();
    }

    public void invalidate() {
        mRgbaConverted = false;
    }
}
