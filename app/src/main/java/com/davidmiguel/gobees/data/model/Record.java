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

package com.davidmiguel.gobees.data.model;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Model class for a record.
 */
@SuppressWarnings("unused")
public class Record extends RealmObject implements Comparable<Record> {

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

    public Record(Date timestamp, int numBees) {
        this(-1, timestamp, numBees);
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

    public void setNumBees(int numBees) {
        this.numBees = numBees;
    }

    @Override
    public int compareTo(@NonNull Record r) {
        return this.getTimestamp().compareTo(r.getTimestamp());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Record record = (Record) obj;
        return id == record.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
