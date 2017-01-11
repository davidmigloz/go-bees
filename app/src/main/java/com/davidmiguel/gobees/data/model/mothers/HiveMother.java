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

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Record;

import java.util.Date;
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

    /**
     * Generate a hive.
     *
     * @return hive.
     */
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
        return new Hive(id, name, imageUrl, notes, new Date(), records);
    }
}