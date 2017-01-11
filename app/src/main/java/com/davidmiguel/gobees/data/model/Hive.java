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
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Model class for a hive.
 */
@SuppressWarnings("unused")
public class Hive extends RealmObject {

    @PrimaryKey
    private long id;

    /**
     * Hive name.
     */
    @Required
    private String name;

    /**
     * Hive image url.
     */
    @Nullable
    private String imageUrl;

    /**
     * Hive notes.
     */
    @Nullable
    private String notes;

    /**
     * Last revision of the hive. (Last date when some event related with the hive happened
     * (the hive was created, modified, a new recording was added...).
     */
    @Required
    private Date lastRevision;

    /**
     * List of records of the hive.
     */
    @Nullable
    private RealmList<Record> records;

    /**
     * List of recordings. It's used to display the records grouped according to some criteria,
     * e.g. grouped by day.
     * A recording is not stored in the db, just the records.
     */
    @Ignore
    private List<Recording> recordings;

    public Hive() {
        // Needed by Realm
    }

    public Hive(long id, String name, @Nullable String imageUrl,
                @Nullable String notes, Date lastRevision, @Nullable RealmList<Record> records) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.notes = notes;
        this.lastRevision = lastRevision;
        this.records = records;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@Nullable String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    public Date getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(Date lastRevision) {
        this.lastRevision = lastRevision;
    }

    @Nullable
    public RealmList<Record> getRecords() {
        return records;
    }

    public void setRecords(@Nullable RealmList<Record> records) {
        this.records = records;
    }

    public boolean isValidHive() {
        return !Strings.isNullOrEmpty(name);
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }

    public void addRecord(@NonNull Record record) {
        if (records != null) {
            records.add(record);
        }
    }

    public void addRecords(@NonNull List<Record> recordsList) {
        if (records != null) {
            records.addAll(recordsList);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hive hive = (Hive) o;
        return id == hive.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
