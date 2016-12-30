package com.davidmiguel.gobees.data.source.network;

import android.net.Uri;

import com.davidmiguel.gobees.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
class NetworkUtils {

    /* Current weather API (http://openweathermap.org/current) */
    private static final String CURRENT_WEATHER_URL =
            "http://api.openweathermap.org/data/2.5/weather";

    /* Query parameters */
    private static final String LAT_PARAM = "lat";
    private static final String LON_PARAM = "lon";
    private static final String UNITS_PARAM = "units";
    private static final String APPID_PARAM = "appid";

    /* The units we want our API to return */
    private static final String UNITS = "metric";

    /**
     * Builds the URL to get current weather data.
     *
     * @param latitude  the latitude of the location.
     * @param longitude the longitude of the location.
     * @return url to use to query the weather server.
     */
    static URL getCurrentWeatherUrl(double latitude, double longitude) {
        Uri weatherQueryUri = Uri.parse(CURRENT_WEATHER_URL).buildUpon()
                .appendQueryParameter(LAT_PARAM, String.valueOf(latitude))
                .appendQueryParameter(LON_PARAM, String.valueOf(longitude))
                .appendQueryParameter(UNITS_PARAM, UNITS)
                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

        try {
            return new URL(weatherQueryUri.toString());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url URL to fetch the HTTP response from.
     * @return contents of the HTTP response, null if no response.
     * @throws IOException related to network and stream reading.
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        // Make the call to the api
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
