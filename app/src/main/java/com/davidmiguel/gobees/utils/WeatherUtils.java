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

package com.davidmiguel.gobees.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.preferences.GoBeesPreferences;

/**
 * Contains useful utilities for a weather app. Such as conversion between Celsius and Fahrenheit,
 * from kph to mph, and from degrees to NSEW.  It also contains the mapping of weather condition
 * codes in OpenWeatherMap to strings.
 */
public final class WeatherUtils {

    private WeatherUtils() {
    }

    /**
     * This method will convert a temperature from Celsius to Fahrenheit.
     *
     * @param temperatureInCelsius Temperature in degrees Celsius(°C).
     * @return Temperature in degrees Fahrenheit (°F).
     */
    static double celsiusToFahrenheit(double temperatureInCelsius) {
        return (temperatureInCelsius * 1.8) + 32;
    }

    /**
     * Temperature data is stored in Celsius. Depending on the user's preference,
     * the app may need to display the temperature in Fahrenheit. This method will perform that
     * temperature conversion if necessary. It will also format the temperature so that no
     * decimal points show. Temperatures will be formatted to the following form: "21°C".
     *
     * @param context     Android Context to access preferences and resources.
     * @param temperature Temperature in degrees Celsius (°C).
     * @return Formatted temperature String in the following form: "21°C"
     */
    public static String formatTemperature(Context context, double temperature) {
        int temperatureFormatResourceId = R.string.format_temperature_celsius;
        double value;
        if (!GoBeesPreferences.isMetric(context)) {
            value = celsiusToFahrenheit(temperature);
            temperatureFormatResourceId = R.string.format_temperature_fahrenheit;
        } else {
            value = temperature;
        }
        return String.format(context.getString(temperatureFormatResourceId), value);
    }

    /**
     * Format humidity.
     *
     * @param context  Android Context to access preferences and resources.
     * @param humidity Percentage of humidity, e.g: 18.
     * @return Formatted humidity String in the following form: "18%"
     */
    public static String formatHumidity(Context context, double humidity) {
        int humidityFormatResourceId = R.string.format_humidity_percentage;
        return String.format(context.getString(humidityFormatResourceId), humidity);
    }

    /**
     * Format pressure.
     *
     * @param context  Android Context to access preferences and resources.
     * @param pressure Percentage of pressure, e.g: 1024.
     * @return Formatted pressure String in the following form: "18 hPa"
     */
    public static String formatPressure(Context context, double pressure) {
        int pressureFormatResourceId = R.string.format_pressure_hpa;
        return String.format(context.getString(pressureFormatResourceId), pressure);
    }

    /**
     * This method uses the wind direction in degrees to determine compass direction as a
     * String. (eg NW) The method will return the wind String in the following form: "2 km/h SW"
     *
     * @param context   Android Context to access preferences and resources
     * @param windSpeed Wind speed in kilometers / hour
     * @param deg       Degrees as measured on a compass, NOT temperature degrees!
     *                  See https://www.mathsisfun.com/geometry/degrees.html
     * @return Wind String in the following form: "2 km/h SW"
     */
    public static String formatWind(Context context, double windSpeed, double deg) {
        int windFormat = R.string.format_wind_kmh;

        double value;
        if (!GoBeesPreferences.isMetric(context)) {
            windFormat = R.string.format_wind_mph;
            value = .621371192237334f * windSpeed;
        } else {
            value = windSpeed;
        }

        String dir = "";
        if (deg >= 337.5 || deg < 22.5) {
            dir = "N";
        } else if (deg >= 22.5 && deg < 67.5) {
            dir = "NE";
        } else if (deg >= 67.5 && deg < 112.5) {
            dir = "E";
        } else if (deg >= 112.5 && deg < 157.5) {
            dir = "SE";
        } else if (deg >= 157.5 && deg < 202.5) {
            dir = "S";
        } else if (deg >= 202.5 && deg < 247.5) {
            dir = "SW";
        } else if (deg >= 247.5 && deg < 292.5) {
            dir = "W";
        } else if (deg >= 292.5 && deg < 337.5) {
            dir = "NW";
        }

        return String.format(context.getString(windFormat), value, dir);
    }

    /**
     * Format rain/snow.
     *
     * @param context Android Context to access preferences and resources.
     * @param volume  Rain volume (mm), e.g: 3.25.
     * @return Formatted rain/snow String in the following form: "0.1 mm"
     */
    public static String formatRainSnow(Context context, double volume) {
        int rainSnowFormatResourceId = R.string.format_rain_snow_mm;
        return String.format(context.getString(rainSnowFormatResourceId), volume);
    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param weatherIconId from OpenWeatherMap API response.
     *                      See http://openweathermap.org/weather-conditions for a list of all IDs.
     * @return resource id for the corresponding icon. day_clear_sky if no relation is found.
     */
    public static int getWeatherIconResourceId(String weatherIconId) {
        switch (weatherIconId) {
            case "01d": // clear sky day
                return R.drawable.ic_weather_day_clear_sky;
            case "01n": // clear sky night
                return R.drawable.ic_weather_night_clear_sky;
            case "02d": // few clouds day
                return R.drawable.ic_weather_day_few_clouds;
            case "02n": // clear sky night
                return R.drawable.ic_weather_night_few_clouds;
            case "03d": // scattered clouds day
            case "03n": // scattered clouds night
                return R.drawable.ic_weather_day_night_scattered_clouds;
            case "04d": // broken clouds day
            case "04n": // broken clouds night
                return R.drawable.ic_weather_day_night_broken_clouds;
            case "09d": // shower rain day
            case "09n": // shower rain night
                return R.drawable.ic_weather_day_night_shower_rain;
            case "10d": // rain day
                return R.drawable.ic_weather_day_rain;
            case "10n": // rain night
                return R.drawable.ic_weather_night_rain;
            case "11d": // thunderstorm day
            case "11n": // thunderstorm night
                return R.drawable.ic_weather_day_night_thunderstorm;
            case "13d": // snow day
            case "13n": // snow night
                return R.drawable.ic_weather_day_night_snow;
            case "50d": // mist day
            case "50n": // mist night
                return R.drawable.ic_weather_day_night_mist;
            default: // Not found
                return R.drawable.ic_help;
        }
    }

    /**
     * Helper method to provide the string according to the weather
     * condition id returned by the OpenWeatherMap call.
     *
     * @param context   Android context.
     * @param weatherId from OpenWeatherMap API response.
     *                  See http://openweathermap.org/weather-conditions for a list of all IDs
     * @return String for the weather condition, condition_unknown if no relation is found.
     */
    public static String getStringForWeatherCondition(Context context, int weatherId) {
        int stringId;
        switch (weatherId) {
            case 200:
                stringId = R.string.condition_200;
                break;
            case 201:
                stringId = R.string.condition_201;
                break;
            case 202:
                stringId = R.string.condition_202;
                break;
            case 210:
                stringId = R.string.condition_210;
                break;
            case 211:
                stringId = R.string.condition_211;
                break;
            case 212:
                stringId = R.string.condition_212;
                break;
            case 221:
                stringId = R.string.condition_221;
                break;
            case 230:
                stringId = R.string.condition_230;
                break;
            case 231:
                stringId = R.string.condition_231;
                break;
            case 232:
                stringId = R.string.condition_232;
                break;
            case 300:
                stringId = R.string.condition_300;
                break;
            case 301:
                stringId = R.string.condition_301;
                break;
            case 302:
                stringId = R.string.condition_302;
                break;
            case 310:
                stringId = R.string.condition_310;
                break;
            case 311:
                stringId = R.string.condition_311;
                break;
            case 312:
                stringId = R.string.condition_312;
                break;
            case 313:
                stringId = R.string.condition_313;
                break;
            case 314:
                stringId = R.string.condition_314;
                break;
            case 321:
                stringId = R.string.condition_321;
                break;
            case 500:
                stringId = R.string.condition_500;
                break;
            case 501:
                stringId = R.string.condition_501;
                break;
            case 502:
                stringId = R.string.condition_502;
                break;
            case 503:
                stringId = R.string.condition_503;
                break;
            case 504:
                stringId = R.string.condition_504;
                break;
            case 511:
                stringId = R.string.condition_511;
                break;
            case 520:
                stringId = R.string.condition_520;
                break;
            case 531:
                stringId = R.string.condition_531;
                break;
            case 600:
                stringId = R.string.condition_600;
                break;
            case 601:
                stringId = R.string.condition_601;
                break;
            case 602:
                stringId = R.string.condition_602;
                break;
            case 611:
                stringId = R.string.condition_611;
                break;
            case 612:
                stringId = R.string.condition_612;
                break;
            case 615:
                stringId = R.string.condition_615;
                break;
            case 616:
                stringId = R.string.condition_616;
                break;
            case 620:
                stringId = R.string.condition_620;
                break;
            case 621:
                stringId = R.string.condition_621;
                break;
            case 622:
                stringId = R.string.condition_622;
                break;
            case 701:
                stringId = R.string.condition_701;
                break;
            case 711:
                stringId = R.string.condition_711;
                break;
            case 721:
                stringId = R.string.condition_721;
                break;
            case 731:
                stringId = R.string.condition_731;
                break;
            case 741:
                stringId = R.string.condition_741;
                break;
            case 751:
                stringId = R.string.condition_751;
                break;
            case 761:
                stringId = R.string.condition_761;
                break;
            case 762:
                stringId = R.string.condition_762;
                break;
            case 771:
                stringId = R.string.condition_771;
                break;
            case 781:
                stringId = R.string.condition_781;
                break;
            case 800:
                stringId = R.string.condition_800;
                break;
            case 801:
                stringId = R.string.condition_801;
                break;
            case 802:
                stringId = R.string.condition_802;
                break;
            case 803:
                stringId = R.string.condition_803;
                break;
            case 804:
                stringId = R.string.condition_804;
                break;
            case 900:
                stringId = R.string.condition_900;
                break;
            case 901:
                stringId = R.string.condition_901;
                break;
            case 902:
                stringId = R.string.condition_902;
                break;
            case 903:
                stringId = R.string.condition_903;
                break;
            case 904:
                stringId = R.string.condition_904;
                break;
            case 905:
                stringId = R.string.condition_905;
                break;
            case 906:
                stringId = R.string.condition_906;
                break;
            case 951:
                stringId = R.string.condition_951;
                break;
            case 952:
                stringId = R.string.condition_952;
                break;
            case 953:
                stringId = R.string.condition_953;
                break;
            case 954:
                stringId = R.string.condition_954;
                break;
            case 955:
                stringId = R.string.condition_955;
                break;
            case 956:
                stringId = R.string.condition_956;
                break;
            case 957:
                stringId = R.string.condition_957;
                break;
            case 958:
                stringId = R.string.condition_958;
                break;
            case 959:
                stringId = R.string.condition_959;
                break;
            case 960:
                stringId = R.string.condition_960;
                break;
            case 961:
                stringId = R.string.condition_961;
                break;
            case 962:
                stringId = R.string.condition_962;
                break;
            default:
                return context.getString(R.string.condition_unknown, weatherId);
        }
        return context.getString(stringId);
    }

    /**
     * Convert dp to pixels.
     *
     * @param resources android resources.
     * @param dp        dps to convert.
     * @return pixels.
     */
    public static float convertDpToPixel(Resources resources, float dp) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
