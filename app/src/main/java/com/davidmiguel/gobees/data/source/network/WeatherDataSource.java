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

import android.os.AsyncTask;
import android.util.Log;

import com.davidmiguel.gobees.data.model.MeteoRecord;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Provides access to the weather server.
 */
public class WeatherDataSource {

    private static final String TAG = WeatherDataSource.class.getSimpleName();
    private static WeatherDataSource instance;

    private WeatherDataSource() {
    }

    public static WeatherDataSource getInstance() {
        if (instance == null) {
            instance = new WeatherDataSource();
        }
        return instance;
    }

    /**
     * Get current weather data.
     *
     * @param id        identifier of the operation.
     * @param latitude  the latitude of the location.
     * @param longitude the longitude of the location.
     */
    public void getCurrentWeather(int id, double latitude, double longitude,
                                  GetWeatherCallback getWeatherCallback) {
        URL weatherRequestUrl = NetworkUtils.getCurrentWeatherUrl(latitude, longitude);
        DataHolder data = new DataHolder(id, weatherRequestUrl, getWeatherCallback);
        new GetWeatherTask().execute(data);
    }

    public interface GetWeatherCallback {
        void onWeatherLoaded(int id, MeteoRecord meteoRecord);

        void onDataNotAvailable();
    }

    /**
     * Background task to connect to the weather api, get the data and parse it.
     */
    private class GetWeatherTask extends AsyncTask<DataHolder, Void, DataHolder> {
        @Override
        protected DataHolder doInBackground(DataHolder... dataArray) {
            DataHolder data = dataArray[0];
            URL weatherRequestUrl = data.getUrl();
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                MeteoRecord meteoRecord = OpenWeatherMapUtils.parseCurrentWeatherJson(json);
                data.setMeteoRecord(meteoRecord);
                return data;
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error: GetWeatherTask()", e);
                return data;
            }
        }

        @Override
        protected void onPostExecute(DataHolder data) {
            if (data.getMeteoRecord() == null) {
                data.getGetWeatherCallback().onDataNotAvailable();
            }
            data.getGetWeatherCallback().onWeatherLoaded(data.getId(), data.getMeteoRecord());
        }
    }

    /**
     * Class to pass data to the async task.
     */
    private class DataHolder {
        GetWeatherCallback getWeatherCallback;
        private int id;
        private URL url;
        private MeteoRecord meteoRecord;

        DataHolder(int id, URL url, GetWeatherCallback getWeatherCallback) {
            this.id = id;
            this.url = url;
            this.getWeatherCallback = getWeatherCallback;
        }

        int getId() {
            return id;
        }

        URL getUrl() {
            return url;
        }

        GetWeatherCallback getGetWeatherCallback() {
            return getWeatherCallback;
        }

        MeteoRecord getMeteoRecord() {
            return meteoRecord;
        }

        void setMeteoRecord(MeteoRecord meteoRecord) {
            this.meteoRecord = meteoRecord;
        }
    }
}
