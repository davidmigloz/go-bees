package com.davidmiguel.gobees.data.model;

import java.util.Random;

import io.realm.RealmList;

/**
 * Mother class for apiaries.
 * Using Object Mother, Test Data Builder and Builder patter.
 */
public class ApiaryMother {

    private static final String NAME_PREFIX = "Apiary";
    private static final int MAX_ID = 1000;

    private long id;

    private String name;

    private String imageUrl;

    private Double locationLong;

        private Double locationLat;

    private String notes;

    private RealmList<Hive> hives;

    private RealmList<MeteoDay> meteoDays;

    private RealmList<MeteoDetail> meteoDetails;

    private ApiaryMother() {
        // Default values
        Random r = new Random(System.currentTimeMillis());
        id = r.nextInt(MAX_ID);
        name = NAME_PREFIX + " " + id;
    }

    private static ApiaryMother newApiary() {
        return new ApiaryMother();
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
        return new Apiary(id, name, imageUrl, locationLong,
                locationLat, notes, hives, meteoDays, meteoDetails);
    }

    public static Apiary newDefaultApiary() {
        return ApiaryMother.newApiary().build();
    }
}