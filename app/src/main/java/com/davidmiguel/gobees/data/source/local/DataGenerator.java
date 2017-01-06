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
 * // TODO remove in production version.
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
