package com.davidmiguel.gobees.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.ApiariesDataSource;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a local data source with static access to the data for easy testing.
 */
public class FakeApiariesLocalDataSource implements ApiariesDataSource {

    private static FakeApiariesLocalDataSource INSTANCE = null;

    private static final Map<Integer, Apiary> APIARIES_SERVICE_DATA = new LinkedHashMap<>();

    private FakeApiariesLocalDataSource() {
        // Add fake data
        APIARIES_SERVICE_DATA.put(1, new Apiary(1, "Apiary 1", null, null, null));
        APIARIES_SERVICE_DATA.put(2, new Apiary(2, "Apiary 2", null, null, null));
        APIARIES_SERVICE_DATA.put(3, new Apiary(3, "Apiary 3", null, null, null));
    }

    public static FakeApiariesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeApiariesLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getApiaries(@NonNull GetApiariesCallback callback) {
        callback.onApiariesLoaded(Lists.newArrayList(APIARIES_SERVICE_DATA.values()));
    }

    @Override
    public void getApiary(int apiaryId, @NonNull GetApiaryCallback callback) {
        Apiary task = APIARIES_SERVICE_DATA.get(apiaryId);
        callback.onApiaryLoaded(task);
    }

    @Override
    public void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback) {
        APIARIES_SERVICE_DATA.put(apiary.getId(), apiary);
    }

    @Override
    public void refreshApiaries() {
        // Not required because the TasksRepository handles the logic of refreshing the
        // tasks from all the available data sources
    }

    @Override
    public void deleteApiary(int apiaryId, @NonNull TaskCallback callback) {
        APIARIES_SERVICE_DATA.remove(apiaryId);
    }

    @Override
    public void deleteAllApiaries(@NonNull TaskCallback callback) {
        APIARIES_SERVICE_DATA.clear();
        callback.onSuccess();
    }

    @VisibleForTesting
    public void addTasks(Apiary... apiaries) {
        for (Apiary apiary : apiaries) {
            APIARIES_SERVICE_DATA.put(apiary.getId(), apiary);
        }
    }
}
