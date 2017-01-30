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

import android.content.Context;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.preferences.GoBeesPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Test class for Weather utilities.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GoBeesPreferences.class})
public class WeatherUtilsTest {

    @Mock
    private Context context;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);
        mockStrings();

        // Mock static classes
        mockStatic(GoBeesPreferences.class);
    }

    @Test
    public void celsiusToFahrenheit() throws Exception {
        double result = WeatherUtils.celsiusToFahrenheit(10);
        assertEquals(50, result, 0);
    }

    @Test
    public void formatTemperatureCelsius() throws Exception {
        // Set metric system
        when(GoBeesPreferences.isMetric(Matchers.<Context>any()))
                .thenReturn(true);
        String result = WeatherUtils.formatTemperature(context, 10);
        assertEquals("<xliff:g id=\"temp\">10</xliff:g>\\u00B0C", result);

    }

    @Test
    public void formatTemperatureFahrenheit() throws Exception {
        // Set imperial system
        when(GoBeesPreferences.isMetric(Matchers.<Context>any()))
                .thenReturn(false);
        String result = WeatherUtils.formatTemperature(context, 10);
        assertEquals("<xliff:g id=\"temp\">50</xliff:g>\\u00B0F", result);
    }

    @Test
    public void formatHumidity() throws Exception {
        String result = WeatherUtils.formatHumidity(context, 10);
        assertEquals("<xliff:g id=\"hum\">10</xliff:g>%", result);
    }

    @Test
    public void formatPressure() throws Exception {
        String result = WeatherUtils.formatPressure(context, 1000);
        assertEquals("<xliff:g id=\"pre\">1000</xliff:g> hPa", result);
    }

    @Test
    public void formatWindKmh() throws Exception {
        // Set metric system
        when(GoBeesPreferences.isMetric(Matchers.<Context>any()))
                .thenReturn(true);
        String result = WeatherUtils.formatWind(context, 5, -1);
        assertTrue(result.startsWith("<xliff:g id=\"speed\">5</xliff:g> km/h"));
    }

    @Test
    public void formatWindMph() throws Exception {
        // Set metric system
        when(GoBeesPreferences.isMetric(Matchers.<Context>any()))
                .thenReturn(false);
        String result = WeatherUtils.formatWind(context, 5, -1);
        assertTrue(result.startsWith("<xliff:g id=\"speed\">3</xliff:g> mph"));
    }

    @Test
    public void formatWindSpeed() throws Exception {
        // Set metric system
        when(GoBeesPreferences.isMetric(Matchers.<Context>any()))
                .thenReturn(true);
        String result = WeatherUtils.formatWind(context, 5, 340);
        assertTrue(result.endsWith("<xliff:g id=\"direction\">N</xliff:g>"));
        result = WeatherUtils.formatWind(context, 5, 25);
        assertTrue(result.endsWith("<xliff:g id=\"direction\">NE</xliff:g>"));
        result = WeatherUtils.formatWind(context, 5, 70);
        assertTrue(result.endsWith("<xliff:g id=\"direction\">E</xliff:g>"));
        result = WeatherUtils.formatWind(context, 5, 120);
        assertTrue(result.endsWith("<xliff:g id=\"direction\">SE</xliff:g>"));
        result = WeatherUtils.formatWind(context, 5, 160);
        assertTrue(result.endsWith("<xliff:g id=\"direction\">S</xliff:g>"));
        result = WeatherUtils.formatWind(context, 5, 230);
        assertTrue(result.endsWith("<xliff:g id=\"direction\">SW</xliff:g>"));
        result = WeatherUtils.formatWind(context, 5, 250);
        assertTrue(result.endsWith("<xliff:g id=\"direction\">W</xliff:g>"));
        result = WeatherUtils.formatWind(context, 5, 300);
        assertTrue(result.endsWith("<xliff:g id=\"direction\">NW</xliff:g>"));
    }

    @Test
    public void formatRainSnow() throws Exception {
        String result = WeatherUtils.formatRainSnow(context, 10);
        assertEquals("<xliff:g id=\"temp\">" + String.format(Locale.getDefault(), "%1.2f", 10.0)
                + "</xliff:g> mm", result);
    }

    @Test
    public void getWeatherIconResourceId() throws Exception {
        String[] ids = {"01d", "01n", "02d", "02n", "03d", "03n", "04d", "04n", "09d", "09n", "10d",
                "10n", "11d", "11n", "13d", "13n", "50d", "50n"};
        for (String id : ids) {
            int res = WeatherUtils.getWeatherIconResourceId(id);
            assertNotEquals(R.drawable.ic_help, res);
        }
    }

    @Test
    public void getStringForWeatherCondition() throws Exception {
        when(context.getString(anyInt())).thenReturn("good");
        when(context.getString(anyInt(), anyInt())).thenReturn("unknown");

        int[] conditionsIds = {200, 201, 202, 210, 211, 212, 221, 230, 231, 232, 300, 301, 302, 310,
                311, 312, 313, 314, 321, 500, 501, 502, 503, 504, 511, 520, 531, 600, 601, 602, 611,
                612, 615, 616, 620, 621, 622, 701, 711, 721, 731, 741, 751, 761, 762, 771, 781, 800,
                801, 802, 803, 804, 900, 901, 902, 903, 904, 905, 906, 951, 952, 953, 954, 955, 956,
                957, 958, 959, 960, 961, 962};
        for (int id : conditionsIds) {
            String res = WeatherUtils.getStringForWeatherCondition(context, id);
            assertNotEquals("unknown", res);
        }
    }

    private void mockStrings() {
        when(context.getString(R.string.pref_weather_units_key)).thenReturn("units");
        when(context.getString(R.string.pref_weather_units_metric)).thenReturn("metric");
        when(context.getString(R.string.format_temperature_celsius))
                .thenReturn("<xliff:g id=\"temp\">%1.0f</xliff:g>\\u00B0C");
        when(context.getString(R.string.format_temperature_fahrenheit))
                .thenReturn("<xliff:g id=\"temp\">%1.0f</xliff:g>\\u00B0F");
        when(context.getString(R.string.format_humidity_percentage))
                .thenReturn("<xliff:g id=\"hum\">%1.0f</xliff:g>%%");
        when(context.getString(R.string.format_pressure_hpa))
                .thenReturn("<xliff:g id=\"pre\">%1.0f</xliff:g> hPa");
        when(context.getString(R.string.format_wind_kmh))
                .thenReturn("<xliff:g id=\"speed\">%1$1.0f</xliff:g> km/h " +
                        "- <xliff:g id=\"direction\">%2$s</xliff:g>");
        when(context.getString(R.string.format_wind_mph))
                .thenReturn("<xliff:g id=\"speed\">%1$1.0f</xliff:g> mph " +
                        "- <xliff:g id=\"direction\">%2$s</xliff:g>");
        when(context.getString(R.string.format_rain_snow_mm))
                .thenReturn("<xliff:g id=\"temp\">%1.2f</xliff:g> mm");
    }
}