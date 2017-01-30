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

package com.davidmiguel.gobees.data.model.mothers;

import android.location.Location;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.MeteoRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.RealmList;

/**
 * Mother class for apiaries.
 * Using Object Mother, Test Data Builder and Builder patter.
 */
@SuppressWarnings("unused")
public class ApiaryMother {

    private static final String NAME_PREFIX = "Apiary";
    private static final int MAX_ID = 1000;
    private static final int NUM_HIVES = 6;
    private static final double LAT = 42.352083;
    private static final double LON = -3.697586;


    private long id;
    private String name;
    private String imageUrl;
    private Double locationLat;
    private Double locationLong;
    private String notes;
    private RealmList<Hive> hives;
    private MeteoRecord currentWeather;
    private RealmList<MeteoRecord> meteoRecords;

    private ApiaryMother() {
        setValues(NUM_HIVES);
    }

    private ApiaryMother(int numHives) {
        setValues(numHives);
    }

    private static ApiaryMother newApiary() {
        return new ApiaryMother();
    }

    private static ApiaryMother newApiary(int numHives) {
        return new ApiaryMother(numHives);
    }

    /**
     * Generate random apiary with 3 hives.
     *
     * @return apiary.
     */
    public static Apiary newDefaultApiary() {
        return ApiaryMother.newApiary().build();
    }

    /**
     * Generate random apiary with numHives.
     *
     * @param numHives number of hives to generateData.
     * @return apiary.
     */
    public static Apiary newDefaultApiary(int numHives) {
        return ApiaryMother.newApiary(numHives).build();
    }

    private void setValues(int numHives) {
        // Generate id
        Random r = new Random(System.nanoTime());
        id = r.nextInt(MAX_ID);
        // Generate name
        name = NAME_PREFIX + " " + id;
        // Generate hives
        List<Hive> generatedHives = generateHives(numHives);
        hives = new RealmList<>(generatedHives.toArray(new Hive[generatedHives.size()]));
        // Generate location
        Location location = getRandomNearLocation(LAT, LON);
        locationLat = location.getLatitude();
        locationLong = location.getLongitude();
    }

    private ApiaryMother withName(String name) {
        this.name = name;
        return this;
    }

    private ApiaryMother withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    private Apiary build() {
        return new Apiary(id, name, imageUrl, locationLat, locationLong,
                notes, hives, currentWeather, meteoRecords);
    }

    private List<Hive> generateHives(int num) {
        List<Hive> hives = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            hives.add(HiveMother.newDefaultHive());
        }
        return hives;
    }

    /**
     * Generate random location.
     *
     * @param latitude  center latitude.
     * @param longitude center longitude.
     * @return random location.
     */
    private Location getRandomNearLocation(double latitude, double longitude) {
        Random random = new Random();
        double lat = BigDecimal.valueOf(latitude + random.nextInt(100) / 100d)
                .setScale(7, RoundingMode.HALF_UP).doubleValue();
        double lon = BigDecimal.valueOf(longitude + random.nextInt(100) / 100d)
                .setScale(7, RoundingMode.HALF_UP).doubleValue();
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);
        return location;
    }
}