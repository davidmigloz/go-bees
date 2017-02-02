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

package com.davidmiguel.gobees.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.MeteoRecord;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of SettingsPresenter.
 */
public class SettingsPresenterTest {

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private SettingsContract.View view;

    @Mock
    private ListPreference listPreference;

    @Mock
    private Preference preference;

    @Mock
    private Context context;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.TaskCallback> taskCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetNextApiaryIdCallback> getNextApiaryIdCallbackCaptor;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetNextHiveIdCallback> getNextHiveIdCallbackCaptor;

    private SettingsPresenter settingsPresenter;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        settingsPresenter = new SettingsPresenter(goBeesRepository, view);
        settingsPresenter.start();

        // The presenter won't update the view unless it's active
        when(view.isActive()).thenReturn(true);
        when(view.getContext()).thenReturn(context);
        when(context.getString(anyInt())).thenReturn("");
    }

    @Test
    public void onPreferenceChange_updateSummary() {
        String summary = "asdf";
        // List preference
        CharSequence[] entries = {summary};
        when(listPreference.findIndexOfValue(anyString())).thenReturn(0);
        when(listPreference.getEntries()).thenReturn(entries);
        settingsPresenter.onPreferenceChange(listPreference, summary);
        // Update summary
        verify(listPreference).setSummary(eq(summary));
        // Other type of preference
        settingsPresenter.onPreferenceChange(preference, summary);
        // Update summary
        verify(listPreference).setSummary(eq(summary));
    }

    @Test
    public void onGenerateDataClicked_generateData() {
        when(preference.getKey()).thenReturn("generateData");
        settingsPresenter.onPreferenceClick(preference);
        // Loading view
        verify(view).showLoadingMsg();
        // Generate apiary
        verify(goBeesRepository).getNextApiaryId(getNextApiaryIdCallbackCaptor.capture());
        getNextApiaryIdCallbackCaptor.getValue().onNextApiaryIdLoaded(0);
        verify(goBeesRepository).saveApiary(any(Apiary.class),
                taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // Generate hives
        verify(goBeesRepository, times(3)).getNextHiveId(getNextHiveIdCallbackCaptor.capture());
        getNextHiveIdCallbackCaptor.getValue().onNextHiveIdLoaded(0);
        verify(goBeesRepository, times(3)).saveHive(anyLong(), any(Hive.class),
                taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // Generate records
        verify(goBeesRepository, times(9)).saveRecords(anyLong(), anyListOf(Record.class),
                any(GoBeesDataSource.SaveRecordingCallback.class));
        // Generate weather data
        verify(goBeesRepository, times(3)).saveMeteoRecords(anyLong(),
                anyListOf(MeteoRecord.class));
    }

    @Test
    public void onDeleteDataClicked_deleteData() {
        when(preference.getKey()).thenReturn("deleteData");
        settingsPresenter.onPreferenceClick(preference);
        // Loading view
        verify(view).showLoadingMsg();
        // Delete data
        verify(goBeesRepository).deleteAll(taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        verify(view).showDataDeletedMsg();
        // On failure -> do nothing
        taskCallbackArgumentCaptor.getValue().onFailure();
    }

    @Test
    public void onBindPreferenceClickListener_bindPreference() {
        settingsPresenter.bindPreferenceClickListener(preference);
        verify(preference).setOnPreferenceClickListener(eq(settingsPresenter));
    }
}