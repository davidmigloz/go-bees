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

package com.davidmiguel.gobees.data.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for MeteoRecord class.
 */
public class MeteoRecordTest {

    @Test
    public void meteoRecordTest() {
        MeteoRecord meteoRecord = new MeteoRecord();
        // Id
        meteoRecord.setId(1);
        assertEquals(1, meteoRecord.getId());
        // CityName
        meteoRecord.setCityName("asdf");
        assertEquals("asdf", meteoRecord.getCityName());
        // WeatherCondition
        meteoRecord.setWeatherCondition(1);
        assertEquals(1, meteoRecord.getWeatherCondition());
        // WeatherConditionIcon
        meteoRecord.setWeatherConditionIcon("asdf");
        assertEquals("asdf", meteoRecord.getWeatherConditionIcon());
        // Temperature
        meteoRecord.setTemperature(0);
        meteoRecord.setTemperatureMax(0);
        meteoRecord.setTemperatureMin(0);
        assertEquals(0, meteoRecord.getTemperature(), 0);
        // Pressure
        meteoRecord.setPressure(0);
        assertEquals(0, meteoRecord.getPressure());
        // Humidity
        meteoRecord.setHumidity(0);
        assertEquals(0, meteoRecord.getHumidity());
        // Wind
        meteoRecord.setWindSpeed(0);
        meteoRecord.setWindDegrees(0);
        assertEquals(0, meteoRecord.getWindSpeed(), 0);
        // Clouds
        meteoRecord.setClouds(0);
        assertEquals(0, meteoRecord.getClouds());
        // Rain
        meteoRecord.setRain(0);
        assertEquals(0, meteoRecord.getRain(), 0);
        // Snow
        meteoRecord.setSnow(0);
        assertEquals(0, meteoRecord.getSnow(), 0);
    }
}