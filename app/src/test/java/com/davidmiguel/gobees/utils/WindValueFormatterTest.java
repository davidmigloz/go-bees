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

import static org.junit.Assert.*;

/**
 * Unit tests for WindValueFormatter.
 */
public class WindValueFormatterTest {

    @Test
    public void getFormattedValue() throws Exception {
        WindValueFormatter windValueFormatter = new WindValueFormatter(WindValueFormatter.Unit.MS);
        String res = windValueFormatter.getFormattedValue(10, null);
        assertEquals("10m/s", res);
        res = windValueFormatter.getFormattedValue(10, null, -1, null);
        assertEquals("10m/s", res);
    }
}