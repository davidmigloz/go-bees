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

package com.davidmiguel.gobees.hive;

import android.app.Activity;
import android.content.Intent;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.model.mothers.HiveMother;
import com.davidmiguel.gobees.data.model.mothers.RecordingMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetHiveCallback;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
import com.davidmiguel.gobees.monitoring.MonitoringActivity;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of HivePresenter.
 */
public class HivePresenterTest {

    private static Hive HIVE;

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private HiveContract.HiveRecordingsView hiveRecordingsView;

    @Mock
    private HiveContract.HiveInfoView hiveInfoView;

    @Mock
    private Intent intent;

    private HivePresenter hivePresenter;

    @Captor
    private ArgumentCaptor<GetHiveCallback> getHiveCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.TaskCallback> taskCallbackArgumentCaptor;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Create a hive
        HIVE = HiveMother.newDefaultHive();
        HIVE.setRecordings(Lists.newArrayList(
                RecordingMother.newDefaultRecording(),
                RecordingMother.newDefaultRecording(),
                RecordingMother.newDefaultRecording()));

        // Get a reference to the class under test
        hivePresenter = new HivePresenter(goBeesRepository,
                hiveRecordingsView, hiveInfoView, 0, HIVE.getId());

        // The presenter won't update the view unless it's active
        when(hiveRecordingsView.isActive()).thenReturn(true);
        when(hiveInfoView.isActive()).thenReturn(true);
        when(hiveRecordingsView.checkCameraPermission()).thenReturn(true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loadRecordings_showRecordingsIntoView() {
        // Given an initialized HivePresenter
        // When loading of recordings is requested
        hivePresenter.start();

        // Callback is captured and invoked with stubbed hives
        verify(goBeesRepository).getHiveWithRecordings(anyLong(), getHiveCallbackArgumentCaptor.capture());
        getHiveCallbackArgumentCaptor.getValue().onHiveLoaded(HIVE);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(hiveRecordingsView);
        // Then progress indicator is hidden and all hives are shown in UI
        inOrder.verify(hiveRecordingsView).isActive();
        inOrder.verify(hiveRecordingsView).setLoadingIndicator(false);
        ArgumentCaptor<List> showRecordingsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(hiveRecordingsView).showRecordings(showRecordingsArgumentCaptor.capture());
        // Assert that the number of hives shown is the expected
        assertTrue(showRecordingsArgumentCaptor.getValue().size() == HIVE.getRecordings().size());
    }

    @Test
    public void loadRecordingsError_showMsg() {
        // Given an initialized HivePresenter
        // When loading of recordings is requested
        hivePresenter.start();
        // Callback is captured and invoked with stubbed hives
        verify(goBeesRepository).getHiveWithRecordings(anyLong(),
                getHiveCallbackArgumentCaptor.capture());
        getHiveCallbackArgumentCaptor.getValue().onDataNotAvailable();
        // Show error
        verify(hiveRecordingsView).showLoadingRecordingsError();
    }

    @Test
    public void newRecordingSaved_showMsg() {
        hivePresenter.result(MonitoringActivity.REQUEST_MONITORING, Activity.RESULT_OK, null);
        // Show msg
        verify(hiveRecordingsView).showSuccessfullySavedMessage();
    }

    @Test
    public void recordingTooShort_showMsg() {
        when(intent.getIntExtra(anyString(), anyInt()))
                .thenReturn(HiveRecordingsFragment.ERROR_RECORDING_TOO_SHORT);
        hivePresenter.result(MonitoringActivity.REQUEST_MONITORING,
                Activity.RESULT_CANCELED, intent);
        // Show msg
        verify(hiveRecordingsView).showRecordingTooShortErrorMessage();
    }

    @Test
    public void saveRecordingError_showMsg() {
        when(intent.getIntExtra(anyString(), anyInt()))
                .thenReturn(HiveRecordingsFragment.ERROR_SAVING_RECORDING);
        hivePresenter.result(MonitoringActivity.REQUEST_MONITORING,
                Activity.RESULT_CANCELED, intent);
        // Show msg
        verify(hiveRecordingsView).showSaveErrorMessage();
        // Undefined error
        hivePresenter.result(-1,
                Activity.RESULT_CANCELED, intent);
        // Show msg
        verify(hiveRecordingsView).showSaveErrorMessage();
    }

    @Test
    public void onStartPressed_initMonitoring() {
        hivePresenter.startNewRecording();
        // Start monitoring
        verify(hiveRecordingsView).startNewRecording(eq(0L), eq(HIVE.getId()));
    }

    @Test
    public void onRecordingClicked_openRecording() {
        hivePresenter.openRecordingsDetail(HIVE.getRecordings().get(0));
        // Open recording
        verify(hiveRecordingsView).showRecordingDetail(eq(0L), eq(HIVE.getId()),
                eq(HIVE.getRecordings().get(0).getDate()));
    }

    @Test
    public void deleteRecording_showOkMsg() {
        Recording recording = HIVE.getRecordings().get(0);
        hivePresenter.deleteRecording(recording);
        // Delete recording
        verify(goBeesRepository).deleteRecording(eq(HIVE.getId()), eq(recording),
                taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // Show msg
        verify(hiveRecordingsView).showSuccessfullyDeletedMessage();
    }

    @Test
    public void deleteApiaryError_showError() {
        Recording recording = HIVE.getRecordings().get(0);
        hivePresenter.deleteRecording(recording);
        // Delete recording
        verify(goBeesRepository).deleteRecording(eq(HIVE.getId()), eq(recording),
                taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onFailure();
        // Show msg
        verify(hiveRecordingsView).showDeletedErrorMessage();
    }
}