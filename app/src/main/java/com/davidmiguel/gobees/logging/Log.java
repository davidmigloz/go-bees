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

import com.crashlytics.android.Crashlytics;
import com.davidmiguel.gobees.BuildConfig;

import timber.log.Timber;

/**
 * Logger wrapper.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Log {

    private Log() {
        // Utility class
    }

    /**
     * Initialises logger.
     */
    public static void initLogger() {
        // If Debug log with LogCat
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        }
        // Log to Crashlytics
        Timber.plant(new CrashlyticsTree());
        // Attach build info
        setCommitHash(BuildConfig.COMMIT_HASH);
        setBuildDate(BuildConfig.BUILD_DATE);
    }

    /**
     * Attaches commit hash in crash reports.
     */
    public static void setCommitHash(String hash) {
        Crashlytics.setString("commit_hash", hash);
    }

    /**
     * Attaches build date in crash reports.
     */
    public static void setBuildDate(String date) {
        Crashlytics.setString("build_date", date);
    }

    /**
     * Log a verbose message with optional format args.
     */
    public static void v(String message, Object... args) {
        Timber.v(message, args);
    }

    /**
     * Log a verbose exception and a message with optional format args.
     */
    public static void v(Throwable t, String message, Object... args) {
        Timber.v(t, message, args);
    }

    /**
     * Log a verbose exception.
     */
    public static void v(Throwable t) {
        Timber.v(t);
    }

    /**
     * Log a debug message with optional format args.
     */
    public static void d(String message, Object... args) {
        Timber.d(message, args);
    }

    /**
     * Log a debug exception and a message with optional format args.
     */
    public static void d(Throwable t, String message, Object... args) {
        Timber.d(t, message, args);
    }

    /**
     * Log a debug exception.
     */
    public static void d(Throwable t) {
        Timber.d(t);
    }

    /**
     * Log an info message with optional format args.
     */
    public static void i(String message, Object... args) {
        Timber.i(message, args);
    }

    /**
     * Log an info exception and a message with optional format args.
     */
    public static void i(Throwable t, String message, Object... args) {
        Timber.i(t, message, args);
    }

    /**
     * Log an info exception.
     */
    public static void i(Throwable t) {
        Timber.i(t);
    }

    /**
     * Log a warning message with optional format args.
     */
    public static void w(String message, Object... args) {
        Timber.w(message, args);
    }

    /**
     * Log a warning exception and a message with optional format args.
     */
    public static void w(Throwable t, String message, Object... args) {
        Timber.w(t, message, args);
    }

    /**
     * Log a warning exception.
     */
    public static void w(Throwable t) {
        Timber.w(t);
    }

    /**
     * Log an error message with optional format args.
     */
    public static void e(String message, Object... args) {
        Timber.e(message, args);
    }

    /**
     * Log an error exception and a message with optional format args.
     */
    public static void e(Throwable t, String message, Object... args) {
        Timber.e(t, message, args);
    }

    /**
     * Log an error exception.
     */
    public static void e(Throwable t) {
        Timber.e(t);
    }

    /**
     * Log an assert message with optional format args.
     */
    public static void wtf(String message, Object... args) {
        Timber.wtf(message, args);
    }

    /**
     * Log an assert exception and a message with optional format args.
     */
    public static void wtf(Throwable t, String message, Object... args) {
        Timber.wtf(t, message, args);
    }

    /**
     * Log an assert exception.
     */
    public static void wtf(Throwable t) {
        Timber.wtf(t);
    }

    /**
     * Set a one-time tag for use on the next logging call.
     */
    public static void tag(String tag) {
        Timber.tag(tag);
    }

}
