package com.davidmiguel.gobees.data.model;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import io.realm.RealmList;
import io.realm.RealmObject;
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
}
