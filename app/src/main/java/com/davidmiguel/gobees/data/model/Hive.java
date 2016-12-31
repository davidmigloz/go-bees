package com.davidmiguel.gobees.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

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
                @Nullable String notes, @Nullable RealmList<Record> records) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.notes = notes;
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
