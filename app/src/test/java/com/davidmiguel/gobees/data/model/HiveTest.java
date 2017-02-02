/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.davidmiguel.gobees.data.model;

import com.davidmiguel.gobees.data.model.mothers.HiveMother;
import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;

import static org.junit.Assert.*;

/**
 * Unit test for Hive class.
 */
@SuppressWarnings({"ConstantConditions", "EqualsWithItself", "ObjectEqualsNull"})
public class HiveTest {

    @Test
    public void hiveTest() {
        Hive hive = HiveMother.newDefaultHive();
        // Image url
        hive.setImageUrl("asdf");
        assertEquals("asdf", hive.getImageUrl());
        // Last revision
        Date now = new Date();
        hive.setLastRevision(now);
        assertEquals(now, hive.getLastRevision());
        // Records
        hive.setRecords(new RealmList<Record>());
        assertEquals(0, hive.getRecords().size());
        hive.addRecord(new Record());
        assertEquals(1, hive.getRecords().size());
        List<Record> records = Lists.newArrayList(new Record(), new Record());
        hive.addRecords(records);
        assertEquals(3, hive.getRecords().size());
        // Equals
        Hive newHive = HiveMother.newDefaultHive();
        assertTrue(hive.equals(hive));
        assertFalse(hive.equals(newHive));
        newHive.setId(hive.getId());
        assertTrue(hive.equals(newHive));
        assertFalse(hive.equals(null));
        // Hash code
        assertNotEquals(hive.hashCode(), HiveMother.newDefaultHive());
    }
}