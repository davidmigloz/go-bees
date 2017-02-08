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

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit test for Recording class.
 */
@SuppressWarnings({"EqualsWithItself", "ObjectEqualsNull"})
public class RecordingTest {

    @Test
    public void recordingTest() {
        Recording recording = new Recording(new Date(), new ArrayList<Record>(),
                new ArrayList<MeteoRecord>());
        // Get meteo
        assertEquals(0, recording.getMeteo().size());
        // Hash code
        Recording newRecording = new Recording(new Date(100000), new ArrayList<Record>(),
                new ArrayList<MeteoRecord>());
        assertNotEquals(recording.hashCode(), newRecording.hashCode());
        // Compare
        assertEquals(1, recording.compareTo(newRecording));
        // Equals
        assertTrue(recording.equals(recording));
        assertFalse(recording.equals(null));
        assertFalse(recording.equals(newRecording));
        newRecording = new Recording(recording.getDate(), new ArrayList<Record>(),
                new ArrayList<MeteoRecord>());
        assertTrue(recording.equals(newRecording));
    }
}