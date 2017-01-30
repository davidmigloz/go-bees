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

/**
 * Defines the methods that the camera can use to interact with the client.
 */
public interface AndroidCameraListener {

    /**
     * Checks if openCV is loaded.
     *
     * @return true / false.
     */
    boolean isOpenCvLoaded();

    /**
     * This method is invoked when camera preview has started. After this method is invoked
     * the frames will start to be delivered to client via the onPreviewFrame() callback.
     *
     * @param width  width of the frames that will be delivered.
     * @param height height of the frames that will be delivered.
     */
    void onCameraStarted(int width, int height);

    /**
     * Callback when a frame is captured.
     *
     * @param cameraFrame frame.
     */
    void onPreviewFrame(CameraFrame cameraFrame);
}
