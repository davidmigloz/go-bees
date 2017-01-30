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

package com.davidmiguel.gobees.monitoring.algorithm.processors;

import android.support.annotation.NonNull;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Implements a Blur algorithm (in particular, Gaussian Blur).
 */
public class Blur implements VideoProcessor {

    private static final String TAG = "Blur";

    private static final int REPETITIONS = 2;
    private static final int KERNEL_SIZE = 3;

    @Override
    public Mat process(@NonNull Mat frame) {
        if (frame.empty()) {
            Log.e(TAG, "Invalid input frame.");
            return null;
        }
        Mat tmp = frame.clone();
        // Apply gaussian blur
        for (int i = 0; i < REPETITIONS; i++) {
            Imgproc.GaussianBlur(tmp, tmp, new Size(KERNEL_SIZE, KERNEL_SIZE), 0);
        }
        return tmp;
    }
}
