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

import android.hardware.Camera;

import java.util.Comparator;

/**
 * Comparator for camera preview sizes.
 */
@SuppressWarnings("deprecation")
class PreviewSizeComparator implements Comparator<Camera.Size> {
    @Override
    public int compare(Camera.Size size1, Camera.Size size2) {
        // Check nulls
        if (size1 == null && size2 == null) {
            return 0;
        } else if (size1 == null) {
            return 1;
        } else if (size2 == null) {
            return -1;
        }
        // Check size
        if (size1.width < size2.width) {
            return -1;
        } else if (size1.width > size2.width) {
            return 1;
        } else {
            return 0;
        }
    }
}
