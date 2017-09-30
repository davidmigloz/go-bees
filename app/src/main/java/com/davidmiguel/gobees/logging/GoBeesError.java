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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;

/**
 * Records stack trace without Logger code.
 */
class GoBeesError extends Throwable {

    GoBeesError(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        super.fillInStackTrace();
        StackTraceElement[] original = getStackTrace();
        List<StackTraceElement> filtered = new ArrayList<>();
        Iterator<StackTraceElement> iterator = Arrays.asList(original).iterator();
        // Heading to top of Timber stack trace
        while (iterator.hasNext()) {
            StackTraceElement stackTraceElement = iterator.next();
            if (isLoggerStackTrace(stackTraceElement)) {
                break;
            }
        }
        // Copy all
        boolean isReachedApp = false;
        while (iterator.hasNext()) {
            StackTraceElement stackTraceElement = iterator.next();
            // Skip Timber
            if (!isReachedApp && isLoggerStackTrace(stackTraceElement)) {
                continue;
            }
            isReachedApp = true;
            filtered.add(stackTraceElement);
        }
        setStackTrace(filtered.toArray(new StackTraceElement[filtered.size()]));
        return this;
    }

    /**
     * Checks whether is a Timber or Log stack trace or not.
     */
    private boolean isLoggerStackTrace(StackTraceElement stackTraceElement) {
        return stackTraceElement.getClassName().equals(Timber.class.getName())
                || stackTraceElement.getClassName().equals(Log.class.getName());
    }
}
