package com.davidmiguel.gobees.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Java Date utils.
 */
@SuppressWarnings("WeakerAccess")
public class DateTimeUtil {

    /**
     * Get actual date with hours, min, sec and milisec set to 0.
     *
     * @return trimmed date.
     */
    public static Date getActualDate() {
        return setTime(new Date(), 0, 0, 0, 0);
    }


    /**
     * Get date with hours, min, sec and milisec set to 0.
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
        c.set(Calendar.DATE, c.get(Calendar.DATE) + numDays);
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
}
