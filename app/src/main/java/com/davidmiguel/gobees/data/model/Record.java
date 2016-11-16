package com.davidmiguel.gobees.data.model;

import com.google.common.base.Objects;

import java.sql.Timestamp;

/**
 * Model class for a record.
 */
public class Record {

    private final Timestamp timestamp;
    private final int numBees;

    public Record(Timestamp timestamp, int numBees) {
        this.timestamp = timestamp;
        this.numBees = numBees;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getNumBees() {
        return numBees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return numBees == record.numBees &&
                Objects.equal(timestamp, record.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timestamp, numBees);
    }
}
