package com.davidmiguel.gobees.data.source.local;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.model.mothers.HiveMother;
import com.davidmiguel.gobees.data.model.mothers.RecordingMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;

import java.util.Random;

/**
 * Generate sample data.
 */
public class DataGenerator implements GoBeesDataSource.TaskCallback {

    private GoBeesDataSource goBeesDataSource;

    public DataGenerator(GoBeesDataSource goBeesDataSource) {
        this.goBeesDataSource = goBeesDataSource;
    }

    private static int randInt(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void generateData(int numApiaries) {
        for (int i = 0; i < numApiaries; i++) {
            Apiary a = generateApiary();
            for (int j = 0; j < randInt(2, 6); j++) {
                Hive h = generateHive(a);
                for (int k = 0; k < randInt(2, 5); k++) {
                    generateRecording(h);
                }
            }
        }
    }

    private Apiary generateApiary() {
        final long[] id = new long[1];
        goBeesDataSource.getNextApiaryId(new GoBeesDataSource.GetNextApiaryIdCallback() {
            @Override
            public void onNextApiaryIdLoaded(long apiaryId) {
                id[0] = apiaryId;
            }
        });
        Apiary apiary = ApiaryMother.newDefaultApiary(0);
        apiary.setId(id[0]);
        goBeesDataSource.saveApiary(apiary, this);
        return apiary;
    }

    private Hive generateHive(Apiary apiary) {
        final long[] id = new long[1];
        goBeesDataSource.getNextHiveId(new GoBeesDataSource.GetNextHiveIdCallback() {
            @Override
            public void onNextHiveIdLoaded(long hiveId) {
                id[0] = hiveId;
            }
        });
        Hive hive = HiveMother.newDefaultHive();
        hive.setId(id[0]);
        goBeesDataSource.saveHive(apiary.getId(), hive, this);
        return hive;
    }

    private Recording generateRecording(Hive hive) {
        Recording recording = RecordingMother.newDefaultRecording(50);
        for (Record record : recording.getRecords()) {
            goBeesDataSource.saveRecord(hive.getId(), record, this);
        }
        return recording;
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure() {
    }
}
