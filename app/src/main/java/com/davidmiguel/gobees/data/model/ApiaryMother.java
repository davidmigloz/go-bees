package com.davidmiguel.gobees.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.RealmList;

/**
 * Mother class for apiaries.
 * Using Object Mother, Test Data Builder and Builder patter.
 */
public class ApiaryMother {

    private static final String NAME_PREFIX = "Apiary";
    private static final int MAX_ID = 1000;
    private static final int NUM_HIVES = 3;

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
        List<Hive> generatedHives = generateHives(NUM_HIVES);
        hives = new RealmList<>(generatedHives.toArray(new Hive[generatedHives.size()]));
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

    private List<Hive> generateHives(int num) {
        List<Hive> hives = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            hives.add(HiveMother.newDefaultHive());
        }
        return hives;
    }

    /**
     * Generate random apiary with 3 hives.
     * @return apiary.
     */
    public static Apiary newDefaultApiary() {
        return ApiaryMother.newApiary().build();
    }
}