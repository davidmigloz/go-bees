package com.davidmiguel.gobees.utils;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * DateTimeUtils tests.
 */
public class DateTimeUtilsTest {

    private DateFormat df;

    @Before
    public void setUp() throws Exception {
        // Date format: 20/11/2016 20:00:00
        df = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss", Locale.getDefault());
    }

    @Test
    public void setTime() throws Exception {
        Date actualDate = df.parse("20/11/2016 20:00:00");
        Date newDate, resultDate;
        // Add one day
        newDate = df.parse("20/11/2016 01:01:01");
        resultDate = DateTimeUtils.setTime(actualDate, 1, 1, 1, 0);
        assertTrue(newDate.equals(resultDate));
    }

    @Test
    public void addDays() throws Exception {
        Date actualDate = df.parse("20/11/2016 20:00:00");
        Date newDate, resultDate;
        // Add one day
        newDate = df.parse("21/11/2016 20:00:00");
        resultDate = DateTimeUtils.addDays(actualDate, 1);
        assertTrue(newDate.equals(resultDate));
    }

    @Test
    public void sumTimeToDate() throws Exception {
        Date actualDate = df.parse("20/11/2016 20:00:00");
        Date newDate, resultDate;
        // Test seconds (add 30 sec)
        newDate = df.parse("20/11/2016 20:00:30");
        resultDate = DateTimeUtils.sumTimeToDate(actualDate, 0, 0, 30);
        assertEquals(newDate.getTime(), resultDate.getTime());
        // Test minutes (add 30min)
        newDate = df.parse("20/11/2016 20:30:00");
        resultDate = DateTimeUtils.sumTimeToDate(actualDate, 0, 30, 0);
        assertEquals(newDate.getTime(), resultDate.getTime());
        // Test hours (add 2 hours)
        newDate = df.parse("20/11/2016 22:00:00");
        resultDate = DateTimeUtils.sumTimeToDate(actualDate, 2, 0, 0);
        assertEquals(newDate.getTime(), resultDate.getTime());
    }
}