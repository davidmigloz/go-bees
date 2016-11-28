package com.davidmiguel.gobees.data.model;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


/**
 * Model class for an Apiary.
 */
@SuppressWarnings("unused")
public class Apiary extends RealmObject {

    @PrimaryKey
    private long id;

    /**
     * Apiary name.
     */
    @Required
    private String name;

    /**
     * Apiary image url.
     */
    @Nullable
    private String imageUrl;

    /**
     * Apiary location longitude.
     */
    @Nullable
    private Double locationLong;

    /**
     * Apiary location latitude.
     */
    @Nullable
    private Double locationLat;

    /**
     * Apiary notes.
     */
    @Nullable
    private String notes;

    /**
     * List of hives of the apiary.
     */
    @Nullable
    private RealmList<Hive> hives;

    /**
     * List of meteorological data from days.
     */
    @Nullable
    private RealmList<MeteoDay> meteoDays;

    /**
     * List of meteorological data from specific moments in time.
     */
    @Nullable
    private RealmList<MeteoDetail> meteoDetails;

    public Apiary() {
        // Needed by Realm
    }

    public Apiary(long id, String name, @Nullable String imageUrl, @Nullable Double locationLong,
                  @Nullable Double locationLat, @Nullable String notes,
                  @Nullable RealmList<Hive> hives, @Nullable RealmList<MeteoDay> meteoDays,
                  @Nullable RealmList<MeteoDetail> meteoDetails) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.locationLong = locationLong;
        this.locationLat = locationLat;
        this.notes = notes;
        this.hives = hives;
        this.meteoDays = meteoDays;
        this.meteoDetails = meteoDetails;
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
    public Double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(@Nullable Double locationLong) {
        this.locationLong = locationLong;
    }

    @Nullable
    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(@Nullable Double locationLat) {
        this.locationLat = locationLat;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    @Nullable
    public RealmList<Hive> getHives() {
        return hives;
    }

    public void setHives(@Nullable RealmList<Hive> hives) {
        this.hives = hives;
    }

    @Nullable
    public RealmList<MeteoDay> getMeteoDays() {
        return meteoDays;
    }

    public void setMeteoDays(@Nullable RealmList<MeteoDay> meteoDays) {
        this.meteoDays = meteoDays;
    }

    @Nullable
    public RealmList<MeteoDetail> getMeteoDetails() {
        return meteoDetails;
    }

    public void setMeteoDetails(@Nullable RealmList<MeteoDetail> meteoDetails) {
        this.meteoDetails = meteoDetails;
    }

    public boolean isValidApiary() {
        return !Strings.isNullOrEmpty(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apiary apiary = (Apiary) o;
        return id == apiary.id &&
                Objects.equal(name, apiary.name) &&
                Objects.equal(imageUrl, apiary.imageUrl) &&
                Objects.equal(locationLong, apiary.locationLong) &&
                Objects.equal(locationLat, apiary.locationLat) &&
                Objects.equal(notes, apiary.notes) &&
                Objects.equal(hives, apiary.hives) &&
                Objects.equal(meteoDays, apiary.meteoDays) &&
                Objects.equal(meteoDetails, apiary.meteoDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, imageUrl, locationLong, locationLat, notes, hives, meteoDays, meteoDetails);
    }
}
