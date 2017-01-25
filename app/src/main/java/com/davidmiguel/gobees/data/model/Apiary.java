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

import java.util.List;

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
     * Apiary location latitude.
     */
    @Nullable
    private Double locationLat;

    /**
     * Apiary location longitude.
     */
    @Nullable
    private Double locationLong;

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
     * Current weather in the apiary.
     */
    @Nullable
    private MeteoRecord currentWeather;

    /**
     * List of meteorological data from recordings.
     */
    @Nullable
    private RealmList<MeteoRecord> meteoRecords;

    public Apiary() {
        // Needed by Realm
    }

    public Apiary(long id, String name, @Nullable String imageUrl, @Nullable Double locationLat,
                  @Nullable Double locationLong, @Nullable String notes,
                  @Nullable RealmList<Hive> hives, @Nullable MeteoRecord currentWeather,
                  @Nullable RealmList<MeteoRecord> meteoRecords) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.notes = notes;
        this.hives = hives;
        this.currentWeather = currentWeather;
        this.meteoRecords = meteoRecords;
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
    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(@Nullable Double locationLat) {
        this.locationLat = locationLat;
    }

    @Nullable
    public Double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(@Nullable Double locationLong) {
        this.locationLong = locationLong;
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
    public MeteoRecord getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(@Nullable MeteoRecord currentWeather) {
        this.currentWeather = currentWeather;
    }

    @Nullable
    public RealmList<MeteoRecord> getMeteoRecords() {
        return meteoRecords;
    }

    public void setMeteoRecords(@Nullable RealmList<MeteoRecord> meteoRecords) {
        this.meteoRecords = meteoRecords;
    }

    public void addHive(@NonNull Hive hive) {
        if (hives != null) {
            hives.add(hive);
        }
    }

    public void addMeteoRecord(@NonNull MeteoRecord meteoRecord) {
        if (meteoRecords != null) {
            meteoRecords.add(meteoRecord);
        }
    }

    public void addMeteoRecords(@NonNull List<MeteoRecord> meteoRecords) {
        if (this.meteoRecords != null) {
            this.meteoRecords.addAll(meteoRecords);
        }
    }

    public boolean hasLocation() {
        return locationLat != null && locationLong != null;
    }
}
