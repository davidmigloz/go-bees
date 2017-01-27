/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees.data.source.network;

import com.davidmiguel.gobees.data.model.MeteoRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Date;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
class OpenWeatherMapUtils {

    /* Result code */
    private static final String OWM_MESSAGE_CODE = "cod";

    /* Location information */
    private static final String OWM_CITY = "name";

    /* Weather condition */
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_ID = "id";
    private static final String OWM_WEATHER_ICON = "icon";

    /* Weather main information */
    private static final String OWM_MAIN = "main";
    private static final String OWM_MAIN_TEMPERATURE = "temp";
    private static final String OWM_MAIN_TEMPERATURE_MIN = "temp_min";
    private static final String OWM_MAIN_TEMPERATURE_MAX = "temp_max";
    private static final String OWM_MAIN_PRESSURE = "pressure";
    private static final String OWM_MAIN_HUMIDITY = "humidity";

    /* Wind */
    private static final String OWM_WIND = "wind";
    private static final String OWM_WIND_SPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";

    /* Clouds */
    private static final String OWM_CLOUDS = "clouds";
    private static final String OWM_CLOUDS_CLOUDINESS = "all";

    /* Rain */
    private static final String OWM_RAIN = "rain";
    private static final String OWM_RAIN_3H = "3h";

    /* Snow */
    private static final String OWM_SNOW = "snow";
    private static final String OWM_SNOW_3H = "3h";

    private OpenWeatherMapUtils() {
    }

    static MeteoRecord parseCurrentWeatherJson(String weatherJson) throws JSONException {
        // Get JSON
        JSONObject jsonObject = new JSONObject(weatherJson);

        // Check errors
        if (jsonObject.has(OWM_MESSAGE_CODE)) {
            int errorCode = jsonObject.getInt(OWM_MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND: // Location invalid
                default: // Server probably down
                    return null;
            }
        }

        // Parse JSON
        Date timestamp = new Date();
        String cityName = null;
        int weatherCondition = 0;
        String weatherConditionIcon = null;
        double temperature = 0;
        double temperatureMin = 0;
        double temperatureMax = 0;
        int pressure = 0;
        int humidity = 0;
        double windSpeed = 0;
        double windDegrees = 0;
        int clouds = 0;
        double rain = 0;
        double snow = 0;

        // Get city
        if (jsonObject.has(OWM_CITY)) {
            cityName = jsonObject.getString(OWM_CITY);
        }

        // Get weather condition
        if (jsonObject.has(OWM_WEATHER)) {
            JSONArray jsonWeatherArray = jsonObject.getJSONArray(OWM_WEATHER);
            JSONObject jsonWeatherObject = jsonWeatherArray.getJSONObject(0);
            weatherCondition = jsonWeatherObject.has(OWM_WEATHER_ID) ?
                    jsonWeatherObject.getInt(OWM_WEATHER_ID) : -1;
            weatherConditionIcon = jsonWeatherObject.has(OWM_WEATHER_ICON) ?
                    jsonWeatherObject.getString(OWM_WEATHER_ICON) : "";
        }

        // Get main info
        if (jsonObject.has(OWM_MAIN)) {
            JSONObject jsonMainObject = jsonObject.getJSONObject(OWM_MAIN);
            temperature = jsonMainObject.has(OWM_MAIN_TEMPERATURE) ?
                    jsonMainObject.getDouble(OWM_MAIN_TEMPERATURE) : 0;
            temperatureMin = jsonMainObject.has(OWM_MAIN_TEMPERATURE_MIN) ?
                    jsonMainObject.getDouble(OWM_MAIN_TEMPERATURE_MIN) : 0;
            temperatureMax = jsonMainObject.has(OWM_MAIN_TEMPERATURE_MAX) ?
                    jsonMainObject.getDouble(OWM_MAIN_TEMPERATURE_MAX) : 0;
            pressure = jsonMainObject.has(OWM_MAIN_PRESSURE) ?
                    jsonMainObject.getInt(OWM_MAIN_PRESSURE) : 0;
            humidity = jsonMainObject.has(OWM_MAIN_HUMIDITY) ?
                    jsonMainObject.getInt(OWM_MAIN_HUMIDITY) : 0;
        }

        // Get wind
        if (jsonObject.has(OWM_WIND)) {
            JSONObject jsonWindObject = jsonObject.getJSONObject(OWM_WIND);
            windSpeed = jsonWindObject.has(OWM_WIND_SPEED) ?
                    jsonWindObject.getDouble(OWM_WIND_SPEED) : 0;
            windDegrees = jsonWindObject.has(OWM_WIND_DIRECTION) ?
                    jsonWindObject.getDouble(OWM_WIND_DIRECTION) : 0;
        }

        // Get clouds
        if (jsonObject.has(OWM_CLOUDS)) {
            JSONObject jsonCloudsObject = jsonObject.getJSONObject(OWM_CLOUDS);
            clouds = jsonCloudsObject.has(OWM_CLOUDS_CLOUDINESS) ?
                    jsonCloudsObject.getInt(OWM_CLOUDS_CLOUDINESS) : 0;
        }

        // Get rain
        if (jsonObject.has(OWM_RAIN)) {
            JSONObject jsonRainObject = jsonObject.getJSONObject(OWM_RAIN);
            rain = jsonRainObject.has(OWM_RAIN_3H) ?
                    jsonRainObject.getDouble(OWM_RAIN_3H) : 0;
        }

        // Get snow
        if (jsonObject.has(OWM_SNOW)) {
            JSONObject jsonSnowObject = jsonObject.getJSONObject(OWM_SNOW);
            snow = jsonSnowObject.has(OWM_SNOW_3H) ?
                    jsonSnowObject.getDouble(OWM_SNOW_3H) : 0;
        }

        return new MeteoRecord(timestamp, cityName, weatherCondition, weatherConditionIcon,
                temperature, temperatureMin, temperatureMax, pressure, humidity, windSpeed,
                windDegrees, clouds, rain, snow);
    }
}
