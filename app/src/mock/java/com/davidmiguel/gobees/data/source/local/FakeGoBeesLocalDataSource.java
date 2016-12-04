package com.davidmiguel.gobees.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.model.mothers.HiveMother;
import com.davidmiguel.gobees.data.model.mothers.RecordingMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Implementation of a local data source with static access to the data for easy testing.
 */
public class FakeGoBeesLocalDataSource implements GoBeesDataSource {

    private static final Map<Long, Apiary> APIARIES_SERVICE_DATA = new LinkedHashMap<>();
    private static final List<Recording> RECORDINGS = new ArrayList<>();
    private static FakeGoBeesLocalDataSource INSTANCE = null;

    private Random r;

    private FakeGoBeesLocalDataSource() {
        r = new Random(System.currentTimeMillis());

        // Create recordings
        for (int i = 0; i < 5; i++) {
            RECORDINGS.add(RecordingMother.newDefaultRecording(50));
        }

        // Add fake data
        for (int i = 0; i < 5; i++) {
            Apiary a = ApiaryMother.newDefaultApiary();
            APIARIES_SERVICE_DATA.put(a.getId(), a);
        }
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
        callback.onHiveLoaded(HiveMother.newDefaultHive());
    }

    @Override
    public void getHiveWithRecordings(long hiveId, @NonNull GetHiveCallback callback) {
        Hive hive = HiveMother.newDefaultHive();
        hive.setId(hiveId);
        hive.setRecordings(RECORDINGS);
        callback.onHiveLoaded(hive);
    }

    @Override
    public void refreshHives(long apiaryId) {
        // Not required because the TasksRepository handles the logic of refreshing the
        // tasks from all the available data sources
    }

    @Override
    public void saveHive(@NonNull Hive hive, @NonNull TaskCallback callback) {
        callback.onSuccess();
    }

    @Override
    public void getNextHiveId(@NonNull GetNextHiveIdCallback callback) {
        callback.onNextHiveIdLoaded(r.nextLong());
    }

    @Override
    public void refreshRecordings(long hiveId) {
        // Not required because the TasksRepository handles the logic of refreshing the
        // tasks from all the available data sources
    }

    @VisibleForTesting
    public void addTasks(Apiary... apiaries) {
        for (Apiary apiary : apiaries) {
            APIARIES_SERVICE_DATA.put(apiary.getId(), apiary);
        }
    }
}
