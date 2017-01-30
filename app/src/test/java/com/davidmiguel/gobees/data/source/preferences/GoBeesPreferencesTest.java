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

package com.davidmiguel.gobees.data.source.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.davidmiguel.gobees.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * GoBeesPreferences unit test.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PreferenceManager.class})
public class GoBeesPreferencesTest {

    @Mock
    private Context context;

    @Mock
    private SharedPreferences sharedPreferences;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Mock static classes
        mockStatic(PreferenceManager.class);
        when(PreferenceManager.getDefaultSharedPreferences(Matchers.<Context>any()))
                .thenReturn(sharedPreferences);
    }

    @Test
    public void isMetric() throws Exception {
        when(sharedPreferences.getString(eq("units"), anyString())).thenReturn("metric");
        when(context.getSharedPreferences(eq("units"), anyInt())).thenReturn(sharedPreferences);
        when(context.getString(R.string.pref_weather_units_key)).thenReturn("units");
        when(context.getString(R.string.pref_weather_units_metric)).thenReturn("metric");
        Boolean result = GoBeesPreferences.isMetric(context);
        assertTrue(result);
    }
}