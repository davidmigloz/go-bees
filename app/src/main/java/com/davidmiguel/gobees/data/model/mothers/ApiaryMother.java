package com.davidmiguel.gobees.data.model.mothers;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.MeteoRecord;

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

    private long id;
    private String name;
    private String imageUrl;
    private Double locationLong;
    private Double locationLat;
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
        Random r = new Random(System.nanoTime());
        id = r.nextInt(MAX_ID);
        name = NAME_PREFIX + " " + id;
        List<Hive> generatedHives = generateHives(numHives);
        hives = new RealmList<>(generatedHives.toArray(new Hive[generatedHives.size()]));
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
                locationLat, notes, hives, currentWeather, meteoRecords);
    }

    private List<Hive> generateHives(int num) {
        List<Hive> hives = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            hives.add(HiveMother.newDefaultHive());
        }
        return hives;
    }
}