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

package com.davidmiguel.gobees.data.source;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.MeteoRecord;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;

import java.util.Date;
import java.util.List;

/**
 * Main entry point for accessing apiaries data.
 */
public interface GoBeesDataSource {

    /**
     * Opens database.
     */
    void openDb();

    /**
     * Closes database.
     */
    void closeDb();

    /**
     * Clean database.
     */
    void deleteAll(@NonNull TaskCallback callback);

    /**
     * Gets all apiaries.
     *
     * @param callback GetApiariesCallback.
     */
    void getApiaries(@NonNull GetApiariesCallback callback);

    /**
     * Gets apiary with given id.
     *
     * @param apiaryId apiary id.
     * @param callback GetApiaryCallback
     */
    void getApiary(long apiaryId, @NonNull GetApiaryCallback callback);

    /**
     * Gets apiary with given id. Blocking function.
     *
     * @param apiaryId apiary id.
     * @return requested apiary.
     */
    Apiary getApiaryBlocking(long apiaryId);

    /**
     * Saves given apiary. If it already exists, is updated.
     * Note: apiary must be a new unmanaged object (don't modify managed objects).
     *
     * @param apiary   apiary unmanaged object.
     * @param callback TaskCallback.
     */
    void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback);

    /**
     * Force to update cache.
     */
    void refreshApiaries();

    /**
     * Delete apiary.
     *
     * @param apiaryId apiary id.
     * @param callback TaskCallback.
     */
    void deleteApiary(long apiaryId, @NonNull TaskCallback callback);

    /**
     * Delete all apiaries.
     *
     * @param callback TaskCallback.
     */
    void deleteAllApiaries(@NonNull TaskCallback callback);

    /**
     * Returns the next apiary id.
     * (Realm does not support auto-increment at the moment).
     *
     * @param callback GetNextApiaryIdCallback.
     */
    void getNextApiaryId(@NonNull GetNextApiaryIdCallback callback);

    /**
     * Gets the last date when apiary info was updated.
     *
     * @param apiaryId apiary id.
     * @return apiary last revision date.
     */
    Date getApiaryLastRevision(long apiaryId);

    /**
     * Gets all hives.
     * Note: don't modify the Hive objects.
     *
     * @param apiaryId apiary id.
     * @param callback GetHivesCallback.
     */
    void getHives(long apiaryId, @NonNull GetHivesCallback callback);

    /**
     * Gets hive with given id.
     * Note: don't modify the Hive object.
     *
     * @param hiveId   hive id.
     * @param callback GetHiveCallback
     */
    void getHive(long hiveId, @NonNull GetHiveCallback callback);

    /**
     * Returns a hive with all its recordings (but no meteo data).
     *
     * @param hiveId   hive id.
     * @param callback GetHiveCallback.
     */
    void getHiveWithRecordings(long hiveId, @NonNull GetHiveCallback callback);

    /**
     * Force to update hives cache.
     */
    void refreshHives(long apiaryId);

    /**
     * Saves given hive. If it already exists, is updated.
     * Note: hive must be a new unmanaged object (don't modify managed objects).
     *
     * @param apiaryId apiary id.
     * @param hive     hive unmanaged object.
     * @param callback TaskCallback.
     */
    void saveHive(long apiaryId, @NonNull Hive hive, @NonNull TaskCallback callback);

    /**
     * Deletes given hive.
     *
     * @param hiveId   hive id.
     * @param callback TaskCallback.
     */
    void deleteHive(long hiveId, @NonNull TaskCallback callback);

    /**
     * Returns the next hive id.
     * (Realm does not support auto-increment at the moment).
     *
     * @param callback GetNextHiveIdCallback.
     */
    void getNextHiveId(@NonNull GetNextHiveIdCallback callback);

    /**
     * Saves given record. If it already exists, is updated.
     * Note: record must be a new unmanaged object (don't modify managed objects).
     * Actual id record will be used.
     *
     * @param hiveId   hive id.
     * @param record   record unmanaged object.
     * @param callback TaskCallback.
     */
    void saveRecord(long hiveId, @NonNull Record record, @NonNull TaskCallback callback);

    /**
     * Saves given list of records.
     * Note: record must be a new unmanaged object (don't modify managed objects).
     * The record id will be assigned (actual id will be ignored).
     *
     * @param records  list of record unmanaged objects.
     * @param callback SaveRecordingCallback.
     */
    void saveRecords(long hiveId, @NonNull List<Record> records, @NonNull SaveRecordingCallback callback);

    /**
     * Gets recording with records and weather data of given period.
     *
     * @param apiaryId apiary id.
     * @param hiveId   hive id.
     * @param start    start of the period (00:00 of that date).
     * @param end      end of the period (23:59 of that date).
     * @param callback GetRecordingCallback.
     */
    void getRecording(long apiaryId, long hiveId, Date start, Date end, @NonNull GetRecordingCallback callback);

    /**
     * Deletes the records contained in the given recording.
     *
     * @param hiveId    hive id.
     * @param recording recording to delete.
     * @param callback  TaskCallback.
     */
    void deleteRecording(long hiveId, @NonNull Recording recording, @NonNull TaskCallback callback);

    /**
     * Updates the current weather of the apiaries in the list.
     *
     * @param apiariesToUpdate apiaries to update weather.
     * @param callback         TaskCallback.
     */
    void updateApiariesCurrentWeather(List<Apiary> apiariesToUpdate, @NonNull TaskCallback callback);

    /**
     * Gets and saves a meteo record from an apiary.
     *
     * @param apiary   corresponding apiary.
     * @param callback TaskCallback.
     */
    void getAndSaveMeteoRecord(Apiary apiary, @NonNull TaskCallback callback);

    /**
     * Save a list of meteo records from an apiary.
     *
     * @param apiaryId     apiary id.
     * @param meteoRecords list of meteo records.
     */
    void saveMeteoRecords(long apiaryId, @NonNull List<MeteoRecord> meteoRecords);

    /**
     * Force to update recordings cache.
     */
    void refreshRecordings(long hiveId);

    interface GetApiariesCallback {
        void onApiariesLoaded(List<Apiary> apiaries);

        void onDataNotAvailable();
    }

    interface GetApiaryCallback {
        void onApiaryLoaded(Apiary apiary);

        void onDataNotAvailable();
    }

    interface GetNextApiaryIdCallback {
        void onNextApiaryIdLoaded(long apiaryId);
    }

    interface GetHivesCallback {
        void onHivesLoaded(List<Hive> hives);

        void onDataNotAvailable();
    }

    interface GetHiveCallback {
        void onHiveLoaded(Hive hive);

        void onDataNotAvailable();
    }

    interface GetNextHiveIdCallback {
        void onNextHiveIdLoaded(long hiveId);
    }

    interface GetRecordingCallback {
        void onRecordingLoaded(Recording recording);

        void onDataNotAvailable();
    }

    interface SaveRecordingCallback extends TaskCallback {
        void onRecordingTooShort();
    }

    interface TaskCallback {
        void onSuccess();

        void onFailure();
    }
}
