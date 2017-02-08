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

package com.davidmiguel.gobees.monitoring.camera;

import android.hardware.Camera;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the implementation of PreviewSizeComparator.
 */
@SuppressWarnings("deprecation")
public class PreviewSizeComparatorTest {

    private PreviewSizeComparator previewSizeComparator;

    @Mock
    private Camera.Size sizeA;

    @Mock
    private Camera.Size sizeB;

    @Before
    public void configTest() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        previewSizeComparator = new PreviewSizeComparator();


    }

    @Test
    public void testSizeComparator() {
        int result;
        // null size
        result = previewSizeComparator.compare(null, null);
        assertEquals(0, result);
        // sizeA null -> sizeB win
        result = previewSizeComparator.compare(null, sizeB);
        assertEquals(1, result);
        // sizeB null -> sizeA win
        result = previewSizeComparator.compare(sizeA, null);
        assertEquals(-1, result);
        // sizeA > sizeB
        sizeA.width = 10000;
        sizeB.width = 100;
        result = previewSizeComparator.compare(sizeA, sizeB);
        assertEquals(1, result);
        // sizeA < sizeB
        sizeA.width = 100;
        sizeB.width = 10000;
        result = previewSizeComparator.compare(sizeA, sizeB);
        assertEquals(-1, result);
        // sizeA = sizeB
        sizeA.width = 100;
        sizeB.width = 100;
        result = previewSizeComparator.compare(sizeA, sizeB);
        assertEquals(0, result);
    }

}