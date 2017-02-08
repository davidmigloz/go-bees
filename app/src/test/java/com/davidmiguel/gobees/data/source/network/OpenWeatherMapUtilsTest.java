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

package com.davidmiguel.gobees.data.source.network;

import com.davidmiguel.gobees.data.model.MeteoRecord;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * OpenWeatherMapUtilsTest unit tests.
 */
public class OpenWeatherMapUtilsTest {
    @Test
    public void parseCurrentWeatherJson() throws Exception {
        String jsonResponse = "{\n"
                + "   \"coord\":{\n"
                + "      \"lon\":139,\n"
                + "      \"lat\":35\n"
                + "   },\n"
                + "   \"sys\":{\n"
                + "      \"country\":\"JP\",\n"
                + "      \"sunrise\":1369769524,\n"
                + "      \"sunset\":1369821049\n"
                + "   },\n"
                + "   \"weather\":[\n"
                + "      {\n"
                + "         \"id\":804,\n"
                + "         \"main\":\"clouds\",\n"
                + "         \"description\":\"overcast clouds\",\n"
                + "         \"icon\":\"04n\"\n"
                + "      }\n"
                + "   ],\n"
                + "   \"main\":{\n"
                + "      \"temp\":289.5,\n"
                + "      \"humidity\":89,\n"
                + "      \"pressure\":1013,\n"
                + "      \"temp_min\":287.04,\n"
                + "      \"temp_max\":292.04\n"
                + "   },\n"
                + "   \"wind\":{\n"
                + "      \"speed\":7.31,\n"
                + "      \"deg\":187.002\n"
                + "   },\n"
                + "   \"rain\":{\n"
                + "      \"3h\":0\n"
                + "   },\n"
                + "   \"clouds\":{\n"
                + "      \"all\":92\n"
                + "   },\n"
                + "   \"snow\":{\n"
                + "      \"3h\":0\n"
                + "   },\n"
                + "   \"dt\":1369824698,\n"
                + "   \"id\":1851632,\n"
                + "   \"name\":\"Shuzenji\",\n"
                + "   \"cod\":200\n"
                + "}";
        MeteoRecord mr = OpenWeatherMapUtils.parseCurrentWeatherJson(jsonResponse);
        assertNotNull(mr);
        assertEquals("city", "Shuzenji", mr.getCityName());
        assertEquals("weatherCondition", 804, mr.getWeatherCondition());
        assertEquals("weatherConditionIcon", "04n", mr.getWeatherConditionIcon());
        assertEquals("temperature", 289.5, mr.getTemperature(), 0);
        assertEquals("temperatureMin", 287.04, mr.getTemperatureMin(), 0);
        assertEquals("temperatureMax", 292.04, mr.getTemperatureMax(), 0);
        assertEquals("pressure", 1013, mr.getPressure());
        assertEquals("humidity", 89, mr.getHumidity());
        assertEquals("windSpeed", 7.31, mr.getWindSpeed(), 0);
        assertEquals("windDegrees", 187.002, mr.getWindDegrees(), 0);
        assertEquals("clouds", 92, mr.getClouds());
        assertEquals("rain", 0, mr.getRain(), 0);
        assertEquals("snow", 0, mr.getSnow(), 0);
    }

}