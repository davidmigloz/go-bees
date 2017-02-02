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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for Record class.
 */
@SuppressWarnings({"EqualsWithItself", "ObjectEqualsNull"})
public class RecordTest {

    @Test
    public void recordTest() {
        Record record = new Record();
        // id
        record.setId(0);
        assertEquals(0, record.getId());
        // num bees
        record.setNumBees(0);
        assertEquals(0, record.getNumBees());
        // equals
        Record newRecord = new Record();
        newRecord.setId(0);
        assertTrue(record.equals(record));
        assertFalse(record.equals(null));
        assertTrue(record.equals(newRecord));
        newRecord.setId(1);
        assertFalse(record.equals(newRecord));
        // hash code
        assertNotEquals(record.hashCode(), newRecord.hashCode());
    }
}