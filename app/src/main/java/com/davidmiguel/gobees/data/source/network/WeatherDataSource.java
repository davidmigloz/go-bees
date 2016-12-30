package com.davidmiguel.gobees.data.source.network;

import com.davidmiguel.gobees.data.model.MeteoRecord;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by davidmigloz on 30/12/2016.
 */
public class WeatherDataSource {

    /**
     * Get current weather data.
     *
     * @param latitude the latitude of the location.
     * @param longitude the longitude of the location.
     * @return current weather data.
     */
    MeteoRecord getCurrentWeather(double latitude, double longitude) {
        try {
            URL weatherRequestUrl = NetworkUtils.getCurrentWeatherUrl(latitude, longitude);
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
            return OpenWeatherMapUtils.parseCurrentWeatherJson(jsonWeatherResponse);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
