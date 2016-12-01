package com.davidmiguel.gobees.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a local data source with static access to the data for easy testing.
 */
public class FakeGoBeesLocalDataSource implements GoBeesDataSource {

    private static FakeGoBeesLocalDataSource INSTANCE = null;

    private static final Map<Long, Apiary> APIARIES_SERVICE_DATA = new LinkedHashMap<>();

    private FakeGoBeesLocalDataSource() {
        // Create apiaries
        Apiary apiary1 = ApiaryMother.newDefaultApiary();
        Apiary apiary2 = ApiaryMother.newDefaultApiary();
        Apiary apiary3 = ApiaryMother.newDefaultApiary();

        // Add fake data
        APIARIES_SERVICE_DATA.put(apiary1.getId(), apiary1);
        APIARIES_SERVICE_DATA.put(apiary2.getId(), apiary2);
        APIARIES_SERVICE_DATA.put(apiary3.getId(), apiary3);
    }

    public static FakeGoBeesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeGoBeesLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void openDb() {

    }

    @Override
    public void closeDb() {

    }

    @Override
    public void getApiaries(@NonNull GetApiariesCallback callback) {
        callback.onApiariesLoaded(Lists.newArrayList(APIARIES_SERVICE_DATA.values()));
    }

    @Override
    public void getApiary(long apiaryId, @NonNull GetApiaryCallback callback) {
        Apiary task = APIARIES_SERVICE_DATA.get(apiaryId);
        callback.onApiaryLoaded(task);
    }

    @Override
    public void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback) {
        APIARIES_SERVICE_DATA.put(apiary.getId(), apiary);
        callback.onSuccess();
    }

    @Override
    public void refreshApiaries() {
        // Not required because the TasksRepository handles the logic of refreshing the
        // tasks from all the available data sources
    }

    @Override
    public void deleteApiary(long apiaryId, @NonNull TaskCallback callback) {
        APIARIES_SERVICE_DATA.remove(apiaryId);
    }

    @Override
    public void deleteAllApiaries(@NonNull TaskCallback callback) {
        APIARIES_SERVICE_DATA.clear();
        callback.onSuccess();
    }

    @Override
    public void getNextApiaryId(@NonNull GetNextApiaryIdCallback callback) {
        Long nextId = Collections.max(APIARIES_SERVICE_DATA.keySet()) + 1;
        callback.onNextApiaryIdLoaded(nextId);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getHives(long apiaryId, @NonNull GetHivesCallback callback) {
        callback.onHivesLoaded(Lists.newArrayList(APIARIES_SERVICE_DATA.get(apiaryId).getHives()));
    }

    @Override
    public void getHive(long hiveId, @NonNull GetHiveCallback callback) {
        // TODO
    }

    @Override
    public void refreshHives(long apiaryId) {
        // Not required because the TasksRepository handles the logic of refreshing the
        // tasks from all the available data sources
    }

    @Override
    public void saveHive(@NonNull Hive hive, @NonNull TaskCallback callback) {
        // TODO
    }

    @Override
    public void getNextHiveId(@NonNull GetNextHiveIdCallback callback) {
        // TODO
    }

    @Override
    public void getRecordings(long hiveId, @NonNull GetRecordingsCallback callback) {
        // TODO
    }

    @VisibleForTesting
    public void addTasks(Apiary... apiaries) {
        for (Apiary apiary : apiaries) {
            APIARIES_SERVICE_DATA.put(apiary.getId(), apiary);
        }
    }
}
