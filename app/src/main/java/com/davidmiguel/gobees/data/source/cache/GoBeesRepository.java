package com.davidmiguel.gobees.data.source.cache;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load apiaries from the data sources into a cache.
 * Here is where the synchronisation between different data sources must be done.
 * In this version, there's just a local data source.
 */
@SuppressWarnings("WeakerAccess")
public class GoBeesRepository implements GoBeesDataSource {

    private static GoBeesRepository INSTANCE = null;

    /**
     * Local database.
     */
    private final GoBeesDataSource goBeesDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<Long, Apiary> cachedApiaries;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

    private GoBeesRepository(GoBeesDataSource goBeesDataSource) {
        this.goBeesDataSource = goBeesDataSource;
    }

    public static GoBeesRepository getInstance(GoBeesDataSource apiariesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new GoBeesRepository(apiariesLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
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
                // TODO get from network
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
        // Delete apiary
        goBeesDataSource.deleteApiary(apiaryId, callback);
        // Do in memory cache update to keep the app UI up to date
        if (cachedApiaries == null) {
            cachedApiaries = new LinkedHashMap<>();
        }
        cachedApiaries.remove(apiaryId);
    }

    @Override
    public void deleteAllApiaries(@NonNull TaskCallback callback) {
        checkNotNull(callback);
        // Delete all apiaries
        goBeesDataSource.deleteAllApiaries(callback);
        // Do in memory cache update to keep the app UI up to date
        if (cachedApiaries == null) {
            cachedApiaries = new LinkedHashMap<>();
        }
        cachedApiaries.clear();
    }

    @Override
    public void getNextApiaryId(@NonNull GetNextApiaryIdCallback callback) {
        checkNotNull(callback);
        // Get next id
        goBeesDataSource.getNextApiaryId(callback);
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

        // Respond immediately with cache if available and not dirty
        // TODO add apiaryId
//        if (cachedApiaries != null && !cacheIsDirty) {
//            callback.onHiveLoaded(cachedApiaries.get(apiaryId));
//            return;
//        }

        // Query the local storage if available
        goBeesDataSource.getHive(hiveId, callback);
    }

    @Override
    public void refreshHives(long apiaryId) {
        cacheIsDirty = true;
    }

    @Override
    public void saveHive(@NonNull Hive hive, @NonNull TaskCallback callback) {
        checkNotNull(hive);
        checkNotNull(callback);
        // Save hive
        goBeesDataSource.saveHive(hive, callback);
        // Do in memory cache update to keep the app UI up to date
        if (cachedApiaries == null) {
            cachedApiaries = new LinkedHashMap<>();
        }
        // TODO update cache
//        cachedApiaries.put(apiary.getId(), apiary);
    }

    @Override
    public void getNextHiveId(@NonNull GetNextHiveIdCallback callback) {
        checkNotNull(callback);
        // Get next id
        goBeesDataSource.getNextHiveId(callback);
    }

    @Override
    public void getRecordings(long hiveId, @NonNull GetRecordingsCallback callback) {
        // TODO
    }

    @Override
    public void refreshRecordings(long hiveId) {
        // TODO
    }

    /**
     * Refresh cache with the given list of apiaries.
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
