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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Log configuration for debug. It logs class names and lines.
 */
class DebugTree extends Timber.DebugTree {

    private static final int CALL_STACK_INDEX = 7;
    private static final int MAX_CLASS_NAME_LENGTH = 17;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        // Get right stack trace element
        StackTraceElement stackTraceElement = stackTrace[CALL_STACK_INDEX];
        // Get class name
        String tag = stackTraceElement.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1);
        // Cast to max length
        tag = tag.length() > MAX_CLASS_NAME_LENGTH ? tag.substring(0, MAX_CLASS_NAME_LENGTH) : tag;
        // Get line
        int lineNum = stackTraceElement.getLineNumber();
        return LoggingUtils.formatLog(tag, lineNum);
    }
}