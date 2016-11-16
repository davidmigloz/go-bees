package com.davidmiguel.gobees.data.source.cache;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.ApiariesDataSource;

/**
 * Concrete implementation to load apiaries from the data sources into a cache.
 * Here is where the synchronisation between different data sources must be done.
 * In this version, there's just a local data source.
 */
public class ApiariesRepository implements ApiariesDataSource {

    private static ApiariesRepository INSTANCE = null;

    private final ApiariesDataSource apiariesLocalDataSource;

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
    public void getApiaries(@NonNull GetApiariesCallback callback) {

    }

    @Override
    public void getApiary(int apiaryId, @NonNull GetApiaryCallback callback) {

    }

    @Override
    public void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback) {

    }

    @Override
    public void refreshApiaries(@NonNull TaskCallback callback) {

    }

    @Override
    public void deleteApiary(int apiaryId, @NonNull TaskCallback callback) {

    }
}
