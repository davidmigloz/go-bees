package com.davidmiguel.gobees.data.model.mothers;

import com.davidmiguel.gobees.data.model.Record;

import java.util.Date;
import java.util.Random;

/**
 * Mother class for records.
 * Using Object Mother, Test Data Builder and Builder patter.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RecordMother {

    private static final int MAX_ID = 10000;
    private static final int MIN_NUM_BEES = 5;
    private static final int MAX_NUM_BEES = 35;

    private long id;
    private Date timestamp;
    private int numBees;

    public RecordMother() {
        setValues();
    }

    private static RecordMother newRecord() {
        return new RecordMother();
    }

    /**
     * Generate random record.
     *
     * @return record.
     */
    public static Record newRandomRecord() {
        return RecordMother.newRecord().build();
    }

    /**
     * Generate a record with the given data.
     *
     * @param timestamp timestamp.
     * @return record.
     */
    public static Record newDefaultRecord(Date timestamp) {
        return RecordMother.newRecord().withTimestamp(timestamp).build();
    }

    /**
     * Generate a record with the given data.
     *
     * @param timestamp timestamp.
     * @param numBees number of bees.
     * @return record.
     */
    public static Record newDefaultRecord(Date timestamp, int numBees) {
        return RecordMother.newRecord()
                .withTimestamp(timestamp)
                .withNumBees(numBees)
                .build();
    }

    private void setValues() {
        Random r = new Random(System.nanoTime());
        timestamp = new Date(Math.abs(System.currentTimeMillis() - r.nextLong()));
        id = timestamp.getTime();
        numBees = r.nextInt((MAX_NUM_BEES - MIN_NUM_BEES) + 1) + MIN_NUM_BEES;
    }

    private RecordMother withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        this.id = timestamp.getTime();
        return this;
    }

    private RecordMother withNumBees(int numBees) {
        this.numBees = numBees;
        return this;
    }

    private Record build() {
        return new Record(id, timestamp, numBees);
    }
}
