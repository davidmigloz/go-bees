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

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

/**
 * Implementation of Timber.Tree which sends exceptions and logs to Crashlytics.
 * Logged messages are associated with the crash data and are visible in the Crashlytics
 * dashboard if you look at the specific crash itself.
 * In addition to writing to the next crash report, it will also write to the LogCat.
 * To make sure that sending crash reports has the smallest impact on your user’s devices,
 * Crashlytics logs have a maximum size of 64 KB. When a log exceeds 64 KB, the earliest
 * logged values will be dropped in order to maintain this threshold.
 * All logged exceptions will appear as “non-fatal” issues in the Fabric dashboard.
 * For any individual app session, only the most recent 8 logged exceptions are stored.
 * https://docs.fabric.io/android/crashlytics/caught-exceptions.html
 */
class CrashlyticsTree extends Timber.Tree {

    @Override
    protected boolean isLoggable(String tag, int priority) {
        return priority >= Log.INFO;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable throwable) {
        String formattedMsg = LoggingUtils.formatLog(priority, tag, message);
        // Log the message to Crashlytics, so we can see it in crash reports
        Crashlytics.log(formattedMsg);
        // Log the exception in Crashlytics
        if (throwable != null) {
            Crashlytics.logException(throwable);
        } else if (priority >= Log.WARN) {
            Crashlytics.logException(new GoBeesError(formattedMsg));
        }
    }
}
