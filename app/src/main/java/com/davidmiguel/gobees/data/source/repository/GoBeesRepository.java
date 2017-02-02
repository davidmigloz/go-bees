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

package com.davidmiguel.gobees.data.source.repository;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.MeteoRecord;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.network.WeatherDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.RealmList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load apiaries from the data sources into a cache.
 * Here is where the synchronisation between different data sources must be done.
 * In this version, there's just a local data source.
 */
@SuppressWarnings("WeakerAccess")
public class GoBeesRepository implements GoBeesDataSource {

    private static GoBeesRepository instance = null;

    /**
     * Local database.
     */
    private final GoBeesDataSource goBeesDataSource;

    /**
     * Weather server.
     */
    private final WeatherDataSource weatherDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<Long, Apiary> cachedApiaries;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

    private GoBeesRepository(GoBeesDataSource goBeesDataSource,
                             WeatherDataSource weatherDataSource) {
        this.goBeesDataSource = goBeesDataSource;
        this.weatherDataSource = weatherDataSource;
    }

    /**
     * Get GoBeesRepository instance.
     *
     * @param apiariesLocalDataSource local data source.
     * @param weatherDataSource       weather data source.
     * @return GoBeesRepository instace.
     */
    public static GoBeesRepository getInstance(GoBeesDataSource apiariesLocalDataSource,
                                               WeatherDataSource weatherDataSource) {
        if (instance == null) {
            instance = new GoBeesRepository(apiariesLocalDataSource, weatherDataSource);
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    @Override
    public void openDb() {
        goBeesDataSource.openDb();
    }

    @Override
    public void closeDb() {
        goBeesDataSource.closeDb();
    }

    @Override
    public void deleteAll(@NonNull TaskCallback callback) {
        checkNotNull(callback);
        if(cachedApiaries != null) {
            cachedApiaries.clear();
        }
        goBeesDataSource.deleteAll(callback);
    }

    @Override
    public void getApiaries(@NonNull final GetApiariesCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (cachedApiaries != null && !cacheIsDirty) {
            callback.onApiariesLoaded(new ArrayList<>(cachedApiaries.values()));
            return;
        }

        // Query the local storage if available. [If not, query the network]
        goBeesDataSource.getApiaries(new GetApiariesCallback() {
            @Override
            public void onApiariesLoaded(List<Apiary> apiaries) {
                refreshCache(apiaries);
                callback.onApiariesLoaded(apiaries);
            }

            @Override
            public void onDataNotAvailable() {
                // If cacheIsDirty synchronize all data from network
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getApiary(long apiaryId, @NonNull final GetApiaryCallback callback) {
        checkNotNull(callback);
        // Respond immediately with cache if available and not dirty
        if (cachedApiaries != null && !cacheIsDirty) {
            callback.onApiaryLoaded(cachedApiaries.get(apiaryId));
            return;
        }
        // Query the local storage if available
        goBeesDataSource.getApiary(apiaryId, callback);
    }

    @Override
    public Apiary getApiaryBlocking(long apiaryId) {
        return goBeesDataSource.getApiaryBlocking(apiaryId);
    }

    @Override
    public void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback) {
        checkNotNull(apiary);
        checkNotNull(callback);
        // Save apiary
        goBeesDataSource.saveApiary(apiary, callback);
        // Do in memory cache update to keep the app UI up to date
        if (cachedApiaries == null) {
            cachedApiaries = new LinkedHashMap<>();
        }
        cachedApiaries.put(apiary.getId(), apiary);
    }

    @Override
    public void refreshApiaries() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteApiary(long apiaryId, @NonNull TaskCallback callback) {
        checkNotNull(callback);
        // Do in memory cache update to keep the app UI up to date
        if (cachedApiaries == null) {
            cachedApiaries = new LinkedHashMap<>();
        }
        cachedApiaries.remove(apiaryId);
        // Delete apiary
        goBeesDataSource.deleteApiary(apiaryId, callback);
    }

    @Override
    public void deleteAllApiaries(@NonNull TaskCallback callback) {
        checkNotNull(callback);
        // Do in memory cache update to keep the app UI up to date
        if (cachedApiaries == null) {
            cachedApiaries = new LinkedHashMap<>();
        }
        cachedApiaries.clear();
        // Delete all apiaries
        goBeesDataSource.deleteAllApiaries(callback);
    }

    @Override
    public void getNextApiaryId(@NonNull GetNextApiaryIdCallback callback) {
        checkNotNull(callback);
        // Get next id
        goBeesDataSource.getNextApiaryId(callback);
    }

    @Override
    public Date getApiaryLastRevision(long apiaryId) {
        return goBeesDataSource.getApiaryLastRevision(apiaryId);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getHives(long apiaryId, @NonNull GetHivesCallback callback) {
        checkNotNull(callback);
        // Respond immediately with cache if available and not dirty
        if (!cacheIsDirty && cachedApiaries != null && cachedApiaries.containsKey(apiaryId)) {
            callback.onHivesLoaded(new ArrayList<>(cachedApiaries.get(apiaryId).getHives()));
            return;
        }
        // Query the local storage if available
        goBeesDataSource.getHives(apiaryId, callback);
    }

    @Override
    public void getHive(long hiveId, @NonNull GetHiveCallback callback) {
        checkNotNull(callback);
        // Query the local storage if available
        goBeesDataSource.getHive(hiveId, callback);
    }

    @Override
    public void getHiveWithRecordings(long hiveId, @NonNull GetHiveCallback callback) {
        checkNotNull(callback);
        // Query the local storage if available
        goBeesDataSource.getHiveWithRecordings(hiveId, callback);
    }

    @Override
    public void refreshHives(long apiaryId) {
        cacheIsDirty = true;
    }

    @Override
    public void saveHive(long apiaryId, @NonNull Hive hive, @NonNull TaskCallback callback) {
        checkNotNull(hive);
        checkNotNull(callback);
        // Save hive
        goBeesDataSource.saveHive(apiaryId, hive, callback);
        refreshHives(apiaryId);
    }

    @Override
    public void deleteHive(long hiveId, @NonNull TaskCallback callback) {
        checkNotNull(callback);
        // Delete hive
        goBeesDataSource.deleteHive(hiveId, callback);
        refreshHives(-1);
    }

    @Override
    public void getNextHiveId(@NonNull GetNextHiveIdCallback callback) {
        checkNotNull(callback);
        // Get next id
        goBeesDataSource.getNextHiveId(callback);
    }

    @Override
    public void saveRecord(long hiveId, @NonNull Record record, @NonNull TaskCallback callback) {
        checkNotNull(callback);
        // Save record
        goBeesDataSource.saveRecord(hiveId, record, callback);
    }

    @Override
    public void saveRecords(long hiveId, @NonNull List<Record> records,
                            @NonNull SaveRecordingCallback callback) {
        checkNotNull(callback);
        // Save record
        goBeesDataSource.saveRecords(hiveId, records, callback);
    }

    @Override
    public void getRecording(long apiaryId, long hiveId, Date start, Date end,
                             @NonNull GetRecordingCallback callback) {
        checkNotNull(callback);
        // Save record
        goBeesDataSource.getRecording(apiaryId, hiveId, start, end, callback);
    }

    @Override
    public void deleteRecording(long hiveId, @NonNull Recording recording,
                                @NonNull TaskCallback callback) {
        checkNotNull(callback);
        // Delete recording
        goBeesDataSource.deleteRecording(hiveId, recording, callback);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void updateApiariesCurrentWeather(final List<Apiary> apiariesToUpdate,
                                             @NonNull final TaskCallback callback) {
        checkNotNull(callback);
        // Prepare callback
        final List<Apiary> apiaries = Collections.synchronizedList(apiariesToUpdate);
        final AtomicBoolean error = new AtomicBoolean(false);
        final AtomicInteger counter = new AtomicInteger(0);
        WeatherDataSource.GetWeatherCallback getWeatherCallback =
                new WeatherDataSource.GetWeatherCallback() {
                    @Override
                    public void onWeatherLoaded(int id, MeteoRecord meteoRecord) {
                        // Set weather
                        apiaries.get(id).setCurrentWeather(meteoRecord);
                        // Check if all apiaries have finished
                        int value = counter.incrementAndGet();
                        if (value >= apiaries.size()) {
                            if (!error.get()) {
                                // Update weather in database and finish
                                goBeesDataSource.updateApiariesCurrentWeather(
                                        apiariesToUpdate, callback);
                            } else {
                                callback.onFailure();
                            }
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {
                        error.set(true);
                        // Check if all apiaries have finished
                        int value = counter.incrementAndGet();
                        if (value >= apiaries.size()) {
                            callback.onFailure();
                        }
                    }
                };
        // Update weather
        for (int i = 0; i < apiariesToUpdate.size(); i++) {
            weatherDataSource.getCurrentWeather(i, apiariesToUpdate.get(i).getLocationLat(),
                    apiaries.get(i).getLocationLong(), getWeatherCallback);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getAndSaveMeteoRecord(@NonNull final Apiary apiary,
                                      @NonNull final TaskCallback callback) {
        checkNotNull(apiary);
        checkNotNull(callback);
        weatherDataSource.getCurrentWeather(1, apiary.getLocationLat(), apiary.getLocationLong(),
                new WeatherDataSource.GetWeatherCallback() {
                    @Override
                    public void onWeatherLoaded(int id, MeteoRecord meteoRecord) {
                        // Fill apiary with just this meteo record to store it on db
                        RealmList<MeteoRecord> meteoRecords = new RealmList<>();
                        meteoRecords.add(meteoRecord);
                        apiary.setMeteoRecords(meteoRecords);
                        // Save data
                        goBeesDataSource.getAndSaveMeteoRecord(apiary, callback);
                        callback.onSuccess();
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onFailure();
                    }
                });
    }

    @Override
    public void saveMeteoRecords(long apiaryId, @NonNull List<MeteoRecord> meteoRecords) {
        checkNotNull(meteoRecords);
        goBeesDataSource.saveMeteoRecords(apiaryId, meteoRecords);
    }

    @Override
    public void refreshRecordings(long hiveId) {
        // No action needed
    }

    /**
     * Refresh cache with the given list of apiaries.
     *
     * @param apiaries updated list of apiaries.
     */
    private void refreshCache(List<Apiary> apiaries) {
        if (cachedApiaries == null) {
            cachedApiaries = new LinkedHashMap<>();
        }
        cachedApiaries.clear();
        for (Apiary apiary : apiaries) {
            cachedApiaries.put(apiary.getId(), apiary);
        }
        cacheIsDirty = false;
    }
}
