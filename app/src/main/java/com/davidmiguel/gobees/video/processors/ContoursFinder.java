package com.davidmiguel.gobees.video.processors;

import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Contours are connected curves in an image or boundaries of connected components in an image.
 * ContoursFinder finds the contours of the given frame and filters them by area.
 */
public class ContoursFinder implements VideoProcessor {

    private static final String TAG = "ContoursFinder";

    private static final Scalar RED = new Scalar(255, 0, 0);
    private static final Scalar GREEN = new Scalar(0, 255, 0);
    private static final double MIN_AREA = 15;
    private static final double MAX_AREA = 600;

    private List<MatOfPoint> contourList;
    private Mat hierarchy;
    private double minArea;
    private double maxArea;
    private int numBees;

    /**
     * Default ContoursFinder constructor.
     * minArea is initialized to 15 and maxArea to 600.
     */
    public ContoursFinder() {
        contourList = new ArrayList<>();
        hierarchy = new Mat();
        this.minArea = MIN_AREA;
        this.maxArea = MAX_AREA;
    }

    /**
     * ContoursFinder constructor.
     *
     * @param minArea the min area to consider a contour a bee.
     * @param maxArea the max area to consider a contour a bee.
     */
    public ContoursFinder(double minArea, double maxArea) {
        contourList = new ArrayList<>();
        hierarchy = new Mat();
        this.minArea = minArea;
        this.maxArea = maxArea;
    }

    @Override
    public Mat process(Mat frame) {
        if (frame == null || frame.empty()) {
            Log.e(TAG, "Invalid input frame.");
            return null;
        }
        Mat tmp = frame.clone();
        // Finding outer contours
        contourList.clear();
        Imgproc.findContours(tmp, contourList, hierarchy,
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        // Filter bees
        Mat contours = new Mat(tmp.rows(), tmp.cols(), CvType.CV_8UC3);
        tmp.release();
        double area;
        Scalar color;
        numBees = 0;
        for (int i = 0; i < contourList.size(); i++) {
            area = Imgproc.contourArea(contourList.get(i));
            if (area > minArea && area < maxArea) {
                color = GREEN;
                numBees++;
            } else {
                color = RED;
            }
            // Draw contour
            Imgproc.drawContours(contours, contourList, i, color, -1);
        }
        return contours;
    }

    /**
     * Get number of bees counted in the processed frame.
     *
     * @return number of bees.
     */
    public int getNumBees() {
        return numBees;
    }
}
