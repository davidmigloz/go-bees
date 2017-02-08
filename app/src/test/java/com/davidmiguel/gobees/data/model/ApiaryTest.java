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

import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.model.mothers.HiveMother;
import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.List;

import io.realm.RealmList;

import static org.junit.Assert.*;

/**
 * Unit test for Apiary class.
 */
@SuppressWarnings("ConstantConditions")
public class ApiaryTest {

    @Test
    public void apiaryTest() {
        Apiary apiary = ApiaryMother.newDefaultApiary();
        // Image url
        apiary.setImageUrl("asdf");
        assertEquals("asdf", apiary.getImageUrl());
        // Add hive
        int hives = apiary.getHives().size();
        apiary.addHive(HiveMother.newDefaultHive());
        assertEquals(hives + 1, apiary.getHives().size());
        apiary.setHives(null);
        // Meteo records
        assertNull(apiary.getMeteoRecords());
        apiary.setMeteoRecords(new RealmList<MeteoRecord>());
        assertNotNull(apiary.getMeteoRecords());
        apiary.addMeteoRecord(new MeteoRecord());
        assertEquals(1, apiary.getMeteoRecords().size());
        List<MeteoRecord> meteoRecords = Lists.newArrayList(new MeteoRecord(), new MeteoRecord());
        apiary.addMeteoRecords(meteoRecords);
        assertEquals(3, apiary.getMeteoRecords().size());

    }

}