package com.davidmiguel.gobees.data.source;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;

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
     * Gets all apiaries.
     * Note: don't modify the Apiary objects.
     * @param callback GetApiariesCallback.
     */
    void getApiaries(@NonNull GetApiariesCallback callback);

    /**
     * Gets apiary with given id.
     * Note: don't modify the Apiary object.
     * @param apiaryId apiary id.
     * @param callback GetApiaryCallback
     */
    void getApiary(long apiaryId, @NonNull GetApiaryCallback callback);

    /**
     * Saves given apiary. If it already exists, is updated.
     * Note: apiary must be a new unmanaged object (don't modify managed objects).
     * @param apiary apiary unmanaged object.
     * @param callback TaskCallback.
     */
    void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback);

    /**
     * Force to update cache.
     */
    void refreshApiaries();

    /**
     * Delete apiary.
     * @param apiaryId apiary id.
     * @param callback TaskCallback.
     */
    void deleteApiary(long apiaryId, @NonNull TaskCallback callback);

    /**
     * Delete all apiaries.
     * @param callback TaskCallback.
     */
    void deleteAllApiaries(@NonNull TaskCallback callback);

    /**
     * Returns the next apiary id.
     * (Realm does not support auto-increment at the moment).
     * @param callback GetNextApiaryIdCallback.
     */
    void getNextApiaryId(@NonNull GetNextApiaryIdCallback callback);

    /**
     * Gets all hives.
     * Note: don't modify the Hive objects.
     * @param apiaryId apiary id.
     * @param callback GetHivesCallback.
     */
    void getHives(long apiaryId, @NonNull GetHivesCallback callback);

    /**
     * Force to update hives cache.
     */
    void refreshHives(long apiaryId);

    /**
     * Saves given hive. If it already exists, is updated.
     * Note: hive must be a new unmanaged object (don't modify managed objects).
     * @param hive hive unmanaged object.
     * @param callback TaskCallback.
     */
    void saveHive(@NonNull Hive hive, @NonNull TaskCallback callback);

    /**
     * Returns the next hive id.
     * (Realm does not support auto-increment at the moment).
     * @param callback GetNextHiveIdCallback.
     */
    void getNextHiveId(@NonNull GetNextHiveIdCallback callback);

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

    interface GetNextHiveIdCallback {
        void onNextHiveIdLoaded(long hiveId);
    }

    interface TaskCallback {
        void onSuccess();

        void onFailure();
    }
}
