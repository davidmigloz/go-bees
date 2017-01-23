/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees.monitoring;

import com.davidmiguel.gobees.monitoring.algorithm.BeesCounter;

import java.io.Serializable;

/**
 * Model class to store the monitoring settings.
 */
class MonitoringSettings implements Serializable {

    /**
     * Apiary id.
     */
    private long apiaryId;

    /**
     * Hive id.
     */
    private long hiveId;

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

    /**
     * Frame rate (1000 = 1 frame per second...).
     */
    private long frameRate;

    public long getApiaryId() {
        return apiaryId;
    }

    public void setApiaryId(long apiaryId) {
        this.apiaryId = apiaryId;
    }

    long getHiveId() {
        return hiveId;
    }

    void setHiveId(long hiveId) {
        this.hiveId = hiveId;
    }

    BeesCounter.BlobSize getBlobSize() {
        return blobSize;
    }

    void setBlobSize(BeesCounter.BlobSize blobSize) {
        this.blobSize = blobSize;
    }

    double getMinArea() {
        return minArea;
    }

    void setMinArea(double minArea) {
        this.minArea = minArea;
    }

    double getMaxArea() {
        return maxArea;
    }

    void setMaxArea(double maxArea) {
        this.maxArea = maxArea;
    }

    int getMaxFrameWidth() {
        return maxFrameWidth;
    }

    void setMaxFrameWidth(int maxFrameWidth) {
        this.maxFrameWidth = maxFrameWidth;
    }

    int getMaxFrameHeight() {
        return maxFrameHeight;
    }

    void setMaxFrameHeight(int maxFrameHeight) {
        this.maxFrameHeight = maxFrameHeight;
    }

    int getZoomRatio() {
        return zoomRatio;
    }

    void setZoomRatio(int zoomRatio) {
        this.zoomRatio = zoomRatio;
    }

    long getFrameRate() {
        return frameRate;
    }

    void setFrameRate(long frameRate) {
        this.frameRate = frameRate;
    }
}
