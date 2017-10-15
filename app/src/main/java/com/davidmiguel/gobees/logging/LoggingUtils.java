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

package com.davidmiguel.gobees.logging;

import android.annotation.SuppressLint;
import android.util.Log;

public class LoggingUtils {

    private LoggingUtils() {
        // Utility class
    }

    /**
     * Formats log.
     */
    static String formatLog(int priority, String tag, String message) {
        return String.format("%s/%s:%s", formatPriority(priority),
                formatTag(tag), formatMessage(message));
    }

    /**
     * Formats log.
     */
    @SuppressLint("DefaultLocale")
    public static String formatLog(String tag, int lineNum) {
        return String.format(">%17s:%04d", tag, lineNum);
    }

    /**
     * Formats the priority of the log.
     */
    private static String formatPriority(int priority) {
        switch (priority) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.INFO:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            case Log.ASSERT:
                return "A";
            default:
                return "?";
        }
    }

    /**
     * Formats tag with Android's maximum length of 23.
     */
    private static String formatTag(String tag) {
        return tag != null ? tag : "";
    }

    /**
     * Formats message. Empty string if message is null.
     */
    private static String formatMessage(String message) {
        return message != null ? message : "";
    }
}
