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

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

/**
 * Test for HourAxisValueFormatter.
 */
public class HourAxisValueFormatterTest {
    @Test
    public void getFormattedValue() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss", Locale.getDefault());
        Date t0 = df.parse("20/11/2016 20:00:00");
        Date t1 = df.parse("20/11/2016 21:00:00");

        HourAxisValueFormatter havf = new HourAxisValueFormatter(t0.getTime() / 1000);
        String result = havf.getFormattedValue((t1.getTime() - t0.getTime()) / 1000, null);
        assertTrue(result.equals("21:00"));
    }
}