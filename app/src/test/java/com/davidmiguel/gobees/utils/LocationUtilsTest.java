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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for LocationUtils methods.
 */
public class LocationUtilsTest {

    @Test
    public void latitudeTest() throws Exception {
        double step = 0.01;
        Double latitudeToTest = -90.0;

        while (latitudeToTest <= 90.0) {
            boolean result = LocationUtils.isValidLocation(latitudeToTest, 0);
            assertTrue(result);
            latitudeToTest += step;
        }

        latitudeToTest = -90.1;

        while (latitudeToTest >= -200.0) {
            boolean result = LocationUtils.isValidLocation(latitudeToTest, 0);
            assertFalse(result);
            latitudeToTest -= step;
        }

        latitudeToTest = 90.01;

        while (latitudeToTest <= 200.0) {
            boolean result = LocationUtils.isValidLocation(latitudeToTest, 0);
            assertFalse(result);
            latitudeToTest += step;
        }
    }

    @Test
    public void longitudeTest() {
        double step = 0.01;
        Double longitudeToTest = -180.0;

        while (longitudeToTest <= 180.0) {
            boolean result = LocationUtils.isValidLocation(0, longitudeToTest);
            assertTrue(result);
            longitudeToTest += step;
        }

        longitudeToTest = -180.01;

        while (longitudeToTest >= -300.0) {
            boolean result = LocationUtils.isValidLocation(0, longitudeToTest);
            assertFalse(result);
            longitudeToTest -= step;
        }

        longitudeToTest = 180.01;

        while (longitudeToTest <= 300.0) {
            boolean result = LocationUtils.isValidLocation(0, longitudeToTest);
            assertFalse(result);
            longitudeToTest += step;
        }
    }
}