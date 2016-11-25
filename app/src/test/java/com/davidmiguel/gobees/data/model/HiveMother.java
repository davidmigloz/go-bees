package com.davidmiguel.gobees.data.model;

import java.util.Random;

import io.realm.RealmList;

/**
 * Mother class for apiaries.
 */
public class HiveMother {

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

    private HiveMother() {
        // Default values
        Random r = new Random(System.currentTimeMillis());
        id = r.nextInt(MAX_ID);
        name = NAME_PREFIX + " " + id;
    }

    protected static HiveMother newApiary() {
        return new HiveMother();
    }

    protected HiveMother withName(String name) {
        this.name = name;
        return this;
    }

    protected HiveMother withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    private Apiary build() {
        return new Apiary(id, name, imageUrl, locationLong,
                locationLat, notes, hives, meteoDays, meteoDetails);
    }

    public static Apiary newDefaultApiary() {
        return HiveMother.newApiary().build();
    }
}