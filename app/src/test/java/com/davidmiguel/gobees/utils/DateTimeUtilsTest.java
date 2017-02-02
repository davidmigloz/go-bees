/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.davidmiguel.gobees.utils;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void getDateOnly() throws Exception {
        Date actualDate = df.parse("20/11/2016 20:10:10");
        Date newDate, resultDate;
        // Test seconds (add 30 sec)
        newDate = df.parse("20/11/2016 00:00:00");
        resultDate = DateTimeUtils.getDateOnly(actualDate);
        assertEquals(newDate.getTime(), resultDate.getTime());
    }

    @Test
    public void getNextDay() throws Exception {
        Date actualDate = df.parse("20/11/2016 20:10:10");
        Date newDate, resultDate;
        // Test seconds (add 30 sec)
        newDate = df.parse("21/11/2016 00:00:00");
        resultDate = DateTimeUtils.getNextDay(actualDate);
        assertEquals(newDate.getTime(), resultDate.getTime());
    }
}