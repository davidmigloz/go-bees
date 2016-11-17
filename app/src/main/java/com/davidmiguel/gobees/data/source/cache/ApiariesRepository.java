package com.davidmiguel.gobees.data.source.cache;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.ApiariesDataSource;

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
public class ApiariesRepository implements ApiariesDataSource {

    private static ApiariesRepository INSTANCE = null;

    private final ApiariesDataSource apiariesLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<Integer, Apiary> cachedApiaries;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

    private ApiariesRepository(ApiariesDataSource apiariesLocalDataSource) {
        this.apiariesLocalDataSource = apiariesLocalDataSource;
    }

    public static ApiariesRepository getInstance(ApiariesDataSource apiariesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ApiariesRepository(apiariesLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getApiaries(@NonNull final GetApiariesCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (cachedApiaries != null && !cacheIsDirty) {
            callback.onApiariesLoaded(new ArrayList<>(cachedApiaries.values()));
            return;
        }

        // Query the local storage if available. If not, query the network
        apiariesLocalDataSource.getApiaries(new GetApiariesCallback() {

            @Override
            public void onApiariesLoaded(List<Apiary> apiaries) {
                refreshCache(apiaries);
                callback.onApiariesLoaded(new ArrayList<>(cachedApiaries.values()));
            }

            @Override
            public void onDataNotAvailable() {
                // TODO get from network
                // If cacheIsDirty synchronize all data from network
            }
        });
    }

    @Override
    public void getApiary(int apiaryId, @NonNull GetApiaryCallback callback) {

    }

    @Override
    public void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback) {

    }

    @Override
    public void refreshApiaries() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteApiary(int apiaryId, @NonNull TaskCallback callback) {

    }

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
