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
 * AndroidCamera contract.
 */
public interface AndroidCamera {

    /**
     * Connects to the camera.
     */
    void connect();

    /**
     * Releases the camera.
     */
    void release();

    /**
     * Checks whether the camera is connected.
     *
     * @return camera status.
     */
    boolean isConnected();

    /**
     * Update the frame rate.
     *
     * @param delay  time to wait before issue the first frame (in milliseconds).
     * @param period period between frames (in milliseconds).
     */
    void updateFrameRate(long delay, long period);
}
