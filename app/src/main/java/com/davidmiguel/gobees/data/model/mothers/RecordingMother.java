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

package com.davidmiguel.gobees.data.model.mothers;

import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Mother class for recordings.
 * Using Object Mother, Test Data Builder and Builder patter.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RecordingMother {

    private static final int NUM_RECORDS = 10;
    private static final int MIN_INTERVAL_RECORDS = 1;
    private Date date;
    private List<Record> records;

    public RecordingMother() {
        setValues(NUM_RECORDS);
    }

    public RecordingMother(int numRecords) {
        setValues(numRecords);
    }

    private static RecordingMother newRecording() {
        return new RecordingMother();
    }

    private static RecordingMother newRecording(int numRecords) {
        return new RecordingMother(numRecords);
    }

    /**
     * Generate random recording with 10 records.
     *
     * @return recording.
     */
    public static Recording newDefaultRecording() {
        return RecordingMother.newRecording().build();
    }

    /**
     * Generate random recording with numRecords.
     *
     * @param numRecords number of records to generateData.
     * @return recording.
     */
    public static Recording newDefaultRecording(int numRecords) {
        return RecordingMother.newRecording(numRecords).build();
    }

    private void setValues(int numRecords) {
        Random r = new Random(System.nanoTime());
        date = generateRandomDate(r);
        records = generateRecords(numRecords, date, r);
    }

    private Recording build() {
        return new Recording(date, records);
    }

    private List<Record> generateRecords(int numRecords, Date recordingDate, Random r) {
        List<Record> records = new ArrayList<>(numRecords);
        for (int i = 0; i < numRecords; i++) {
            records.add(RecordMother.newDefaultRecord(
                    DateTimeUtils.sumTimeToDate(recordingDate, 0, i * MIN_INTERVAL_RECORDS, 0)));
        }
        Collections.sort(records);
        return records;
    }

    private Date generateRandomDate(Random r) {
        return new Date(DateTimeUtils.getActualDate().getTime() - ((1000 * 60 * 60 * 24) * r.nextInt(100)));
    }
}
