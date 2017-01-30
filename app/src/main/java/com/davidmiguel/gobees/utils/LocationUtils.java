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

package com.davidmiguel.gobees.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

/**
 * Location utility methods.
 */
public final class LocationUtils {

    // Latitude and longitude regex patterns (http://stackoverflow.com/a/31408260/6305235)
    private static final Pattern LATITUDE_PATTERN = Pattern.compile(
            "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$");
    private static final Pattern LONGITUDE_PATTERN = Pattern.compile(
            "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$");

    private LocationUtils() {
    }

    /**
     * Checks if a location is valid.
     * The latitude must be a number between -90 and 90 and the longitude between -180 and 180.
     *
     * @param latitude  latitude coordinate.
     * @param longitude longitude coordinate.
     * @return if it is valid.
     */
    public static boolean isValidLocation(double latitude, double longitude) {
        boolean validLat = LATITUDE_PATTERN.matcher(formatCoordinate(latitude)).find();
        boolean validLon = LONGITUDE_PATTERN.matcher(formatCoordinate(longitude)).find();
        return validLat && validLon;
    }

    /**
     * Format coordinate like #.######.
     *
     * @param coord coordinate.
     * @return formatted coordinate.
     */
    private static String formatCoordinate(double coord) {
        DecimalFormatSymbols separator = new DecimalFormatSymbols();
        separator.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.######", separator);
        df.setRoundingMode(RoundingMode.UP);
        return df.format(coord);
    }
}
