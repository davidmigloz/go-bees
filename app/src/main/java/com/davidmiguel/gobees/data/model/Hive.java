package com.davidmiguel.gobees.data.model;

import android.location.Location;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;

/**
 * Model class for a hive.
 */
public class Hive {

    private final int id;
    @Nullable
    private final String imageUrl;
    @Nullable
    private final String notes;

    public Hive(int id, String imageUrl, String notes) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hive hive = (Hive) o;
        return id == hive.id &&
                Objects.equal(imageUrl, hive.imageUrl) &&
                Objects.equal(notes, hive.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, imageUrl, notes);
    }
}
