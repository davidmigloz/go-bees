package com.davidmiguel.gobees.data.source.network;

import android.os.AsyncTask;

import com.davidmiguel.gobees.data.model.MeteoRecord;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Provides access to the weather server.
 */
public class WeatherDataSource {

    private GetWeatherCallback callback;

    /**
     * Get current weather data.
     *
     * @param latitude  the latitude of the location.
     * @param longitude the longitude of the location.
     */
    void getCurrentWeather(double latitude, double longitude, GetWeatherCallback getWeatherCallback) {
        this.callback = getWeatherCallback;
        new GetWeatherTask().execute(NetworkUtils.getCurrentWeatherUrl(latitude, longitude));
    }

    interface GetWeatherCallback {
        void onWeatherLoaded(MeteoRecord meteoRecord);

        void onDataNotAvailable();
    }

    /**
     * Background task to connect to the weather api, get the data and parse it.
     */
    private class GetWeatherTask extends AsyncTask<URL, Void, MeteoRecord> {
        @Override
        protected MeteoRecord doInBackground(URL... urls) {
            URL weatherRequestUrl = urls[0];
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                return OpenWeatherMapUtils.parseCurrentWeatherJson(json);
            } catch (IOException | JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(MeteoRecord meteoRecord) {
            if (meteoRecord == null) {
                callback.onDataNotAvailable();
            }
            callback.onWeatherLoaded(meteoRecord);
        }
    }
}
