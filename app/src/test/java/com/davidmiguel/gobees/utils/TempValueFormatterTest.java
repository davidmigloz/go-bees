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

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for TempValueFormatter.
 */
public class TempValueFormatterTest {
    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getFormattedValueCelsius() throws Exception {
        TempValueFormatter tempValueFormatter =
                new TempValueFormatter(TempValueFormatter.Unit.CELSIUS);
        String res = tempValueFormatter.getFormattedValue(10, null);
        assertEquals("10°C", res);
        res = tempValueFormatter.getFormattedValue(10, null, -1, null);
        assertEquals("10°C", res);
    }

    @Test
    public void getFormattedValueFahrenheit() throws Exception {
        TempValueFormatter tempValueFormatter =
                new TempValueFormatter(TempValueFormatter.Unit.FAHRENHEIT);
        String res = tempValueFormatter.getFormattedValue(10, null);
        assertEquals("50°F", res);
    }

}