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

package com.davidmiguel.gobees.monitoring.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

import java.util.List;

/**
 * Improved version of OpenCV JavaCameraView.
 * It allows to control the camera zoom.
 * The frame size is set to 640x480.
 */
@SuppressWarnings("deprecation")
public class CameraView extends JavaCameraView {

    private Camera.Parameters params;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean initializeCamera(int width, int height) {
        // Initialize camera
        boolean ok = super.initializeCamera(width, height);
        // Get camera parameters
        params = mCamera.getParameters();
        return ok;
    }

    public void setZoom(int ratio) {
        if (params.isZoomSupported()) {
            // Get supported ratios
            List<Integer> zoomRatios = params.getZoomRatios();
            // Chose closest ratio
            int i;
            for (i = 0; i < zoomRatios.size(); i++) {
                if (ratio <= zoomRatios.get(i)) {
                    break;
                }
            }
            // Set zoom
            params.setZoom(i <= params.getMaxZoom() ? i : params.getMaxZoom());
            mCamera.setParameters(params);
        }
    }
}
