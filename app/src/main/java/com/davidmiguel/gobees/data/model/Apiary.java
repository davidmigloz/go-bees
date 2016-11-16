package com.davidmiguel.gobees.data.model;

import android.location.Location;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;


/**
 * Model class for an Apiary.
 */
public class Apiary {

    private final int id;
    private final String name;
    @Nullable
    private final String imageUrl;
    @Nullable
    private final Location location;
    @Nullable
    private final String notes;

    public Apiary(int id, String name, @Nullable String imageUrl,
                  @Nullable Location location, @Nullable String notes) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    @Nullable
    public Location getLocation() {
        return location;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apiary apiary = (Apiary) o;
        return id == apiary.id &&
                Objects.equal(name, apiary.name) &&
                Objects.equal(imageUrl, apiary.imageUrl) &&
                Objects.equal(location, apiary.location) &&
                Objects.equal(notes, apiary.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, imageUrl, location, notes);
    }
}
