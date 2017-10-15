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

package com.davidmiguel.gobees.monitoring.algorithm;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.logging.Log;
import com.davidmiguel.gobees.monitoring.algorithm.processors.BackgroundSubtractor;
import com.davidmiguel.gobees.monitoring.algorithm.processors.Blur;
import com.davidmiguel.gobees.monitoring.algorithm.processors.ContoursFinder;
import com.davidmiguel.gobees.monitoring.algorithm.processors.Morphology;

import org.opencv.core.Mat;

/**
 * Counts the number of bees based on the area of detected moving contours.
 */
public class AreaBeesCounter implements BeesCounter {

    private static AreaBeesCounter instance;

    private Blur blur;
    private BackgroundSubtractor bs;
    private Morphology morphology;
    private ContoursFinder cf;
    private Mat processedFrame;

    /**
     * Default ContourBeesCounter constructor.
     * History is initialized to 10 and shadows threshold to 0.7.
     * minArea is initialized to 15 and maxArea to 800.
     */
    private AreaBeesCounter() {
        blur = new Blur();
        bs = new BackgroundSubtractor();
        morphology = new Morphology();
        cf = new ContoursFinder();
    }

    public static AreaBeesCounter getInstance() {
        if (instance == null) {
            instance = new AreaBeesCounter();
        }
        return instance;
    }

    @Override
    public int countBees(@NonNull Mat frame) {
        final long t0 = System.nanoTime();
        Mat r0 = blur.process(frame);
        Mat r1 = bs.process(r0);
        Mat r2 = morphology.process(r1);
        processedFrame = cf.process(r2);
        r0.release();
        r1.release();
        r2.release();
        Log.d("countBees time: %d", (System.nanoTime() - t0) / 1000000);
        return cf.getNumBees();
    }

    @Override
    public Mat getProcessedFrame() {
        return processedFrame;
    }

    @Override
    public void updateBlobSize(BlobSize size) {
        switch (size) {
            case SMALL:
                morphology.setDilateKernel(2);
                morphology.setErodeKernel(3);
                break;
            case NORMAL:
                morphology.setDilateKernel(3);
                morphology.setErodeKernel(3);
                break;
            case BIG:
            default:
                morphology.setDilateKernel(3);
                morphology.setErodeKernel(2);
        }
    }

    @Override
    public void updateMinArea(Double minArea) {
        cf.setMinArea(minArea);
    }

    @Override
    public void updateMaxArea(Double maxArea) {
        cf.setMaxArea(maxArea);
    }
}
