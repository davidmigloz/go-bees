package com.davidmiguel.gobees.data.model;

import com.google.common.base.Objects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Model class for a record.
 */
@SuppressWarnings("unused")
public class Record extends RealmObject {

    @PrimaryKey
    private long id;

    /**
     * Recording timestamp.
     */
    @Required
    private Date timestamp;

    /**
     * Number of bees in that specific moment.
     */
    private int numBees;

    public Record() {
        // Needed by Realm
    }

    public Record(long id, Date timestamp, int numBees) {
        this.id = id;
        this.timestamp = timestamp;
        this.numBees = numBees;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getNumBees() {
        return numBees;
    }
}
