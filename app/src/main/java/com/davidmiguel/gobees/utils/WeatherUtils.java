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
public class WeatherUtils {

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
        if (!GoBeesPreferences.isMetric(context)) {
            temperature = celsiusToFahrenheit(temperature);
            temperatureFormatResourceId = R.string.format_temperature_fahrenheit;
        }
        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param weatherIconId from OpenWeatherMap API response
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
                return R.drawable.ic_weather_day_clear_sky;
        }
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
