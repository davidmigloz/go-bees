package com.davidmiguel.gobees.data.model.mothers;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Record;

import java.util.Random;

import io.realm.RealmList;

/**
 * Mother class for hives.
 * Using Object Mother, Test Data Builder and Builder patter.
 */
public class HiveMother {

    private static final String NAME_PREFIX = "Hive";
    private static final int MAX_ID = 1000;

    private long id;

    private String name;

    private String imageUrl;

    private String notes;

    private RealmList<Record> records;

    private HiveMother() {
        // Default values
        Random r = new Random(System.nanoTime());
        id = r.nextInt(MAX_ID);
        name = NAME_PREFIX + " " + id;
    }

    private static HiveMother newHive() {
        return new HiveMother();
    }

    public static Hive newDefaultHive() {
        return HiveMother.newHive().build();
    }

    private HiveMother withName(String name) {
        this.name = name;
        return this;
    }

    private HiveMother withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    private Hive build() {
        return new Hive(id, name, imageUrl, notes, records);
    }
}