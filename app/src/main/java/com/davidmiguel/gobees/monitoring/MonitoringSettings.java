package com.davidmiguel.gobees.monitoring;

import com.davidmiguel.gobees.video.BeesCounter;

import java.io.Serializable;

/**
 * Model class to store the monitoring settings.
 */
class MonitoringSettings implements Serializable {

    /**
     * Blob blobSize. This causes regions within an image get "thicker" or "thinner".
     */
    private BeesCounter.BlobSize blobSize;

    /**
     * Min area. Smaller areas are not consider to be a bee.
     */
    private double minArea;

    /**
     * Max area. Greater areas are not consider to be a bee.
     */
    private double maxArea;

    /**
     * Max width frame.
     */
    private int maxFrameWidth;

    /**
     * Max height frame.
     */
    private int maxFrameHeight;

    /**
     * Zoom ratio (100 = x1, 200 = x2…).
     */
    private int zoomRatio;

    public BeesCounter.BlobSize getBlobSize() {
        return blobSize;
    }

    void setBlobSize(BeesCounter.BlobSize blobSize) {
        this.blobSize = blobSize;
    }

    public double getMinArea() {
        return minArea;
    }

    void setMinArea(double minArea) {
        this.minArea = minArea;
    }

    public double getMaxArea() {
        return maxArea;
    }

    void setMaxArea(double maxArea) {
        this.maxArea = maxArea;
    }

    public int getMaxFrameWidth() {
        return maxFrameWidth;
    }

    void setMaxFrameWidth(int maxFrameWidth) {
        this.maxFrameWidth = maxFrameWidth;
    }

    public int getMaxFrameHeight() {
        return maxFrameHeight;
    }

    void setMaxFrameHeight(int maxFrameHeight) {
        this.maxFrameHeight = maxFrameHeight;
    }

    public int getZoomRatio() {
        return zoomRatio;
    }

    void setZoomRatio(int zoomRatio) {
        this.zoomRatio = zoomRatio;
    }
}
