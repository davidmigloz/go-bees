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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Java Date utils.
 */
@SuppressWarnings("WeakerAccess")
public final class DateTimeUtils {

    public static final long T_5_SECONDS = 5000;
    public static final long T_15_MINUTES = 900000;

    private static final long ONE_HOUR_IN_MS = 3600000;
    private static final long ONE_MIN_IN_MS = 60000;
    private static final long ONE_SEC_IN_MS = 1000;

    private DateTimeUtils() {
    }

    /**
     * Get actual date with hours, min, sec and millisec set to 0.
     *
     * @return trimmed date.
     */
    public static Date getActualDate() {
        return setTime(new Date(), 0, 0, 0, 0);
    }


    /**
     * Get date with hours, min, sec and millisec set to 0.
     *
     * @param date date to trim.
     * @return trimmed date.
     */
    public static Date getDateOnly(Date date) {
        return setTime(date, 0, 0, 0, 0);
    }

    /**
     * Set date with given time.
     *
     * @return modified date.
     */
    public static Date setTime(final Date date,
                               final int hour, final int min, final int sec, final int msec) {
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(Calendar.HOUR_OF_DAY, hour);
        gc.set(Calendar.MINUTE, min);
        gc.set(Calendar.SECOND, sec);
        gc.set(Calendar.MILLISECOND, msec);
        return gc.getTime();
    }

    /**
     * Add given number of days to one date.
     *
     * @param date    date to modify.
     * @param numDays number of days to add.
     * @return modified date.
     */
    public static Date addDays(Date date, int numDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, numDays);
        return c.getTime();
    }

    /**
     * Get next date at 00:00:00.
     *
     * @param date actual date.
     * @return next day relative to the actual day.
     */
    public static Date getNextDay(Date date) {
        return addDays(getDateOnly(date), 1);
    }

    /**
     * Sum time to given date.
     *
     * @param date  actual date.
     * @param hours hours to add.
     * @param mins  mins to add.
     * @param secs  secs to add.
     * @return new date.
     */
    public static Date sumTimeToDate(Date date, int hours, int mins, int secs) {
        long hoursToAddInMs = hours * ONE_HOUR_IN_MS;
        long minsToAddInMs = mins * ONE_MIN_IN_MS;
        long secsToAddInMs = secs * ONE_SEC_IN_MS;
        return new Date(date.getTime() + hoursToAddInMs + minsToAddInMs + secsToAddInMs);
    }
}
