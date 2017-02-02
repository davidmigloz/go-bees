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

package com.davidmiguel.gobees.recording;

import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.model.mothers.RecordingMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetRecordingCallback;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of RecordingPresenter.
 */
public class RecordingPresenterTest {

    private static final int HIVE_ID = 1;
    private static final Recording RECORDING = RecordingMother.newDefaultRecording();
    private static final Date DATE = new Date();

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private RecordingContract.View view;

    private RecordingPresenter presenter;

    @Captor
    private ArgumentCaptor<GetRecordingCallback> getRecordingCallbackArgumentCaptor;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        presenter = new RecordingPresenter(goBeesRepository, view, 0, HIVE_ID, DATE, DATE);

        // The presenter won't update the view unless it's active
        when(view.isActive()).thenReturn(true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void start_showRecordingIntoView() {
        // Given an initialized RecordingPresenter
        // When start is requested
        presenter.start();

        // Callback is captured and invoked with stubbed recording
        verify(goBeesRepository).getRecording(anyLong(), anyLong(), any(Date.class), any(Date.class),
                getRecordingCallbackArgumentCaptor.capture());
        getRecordingCallbackArgumentCaptor.getValue().onRecordingLoaded(RECORDING);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(view);
        // Then progress indicator is hidden and all hives are shown in UI
        inOrder.verify(view).setLoadingIndicator(false);
        ArgumentCaptor<Recording> showRecordingArgumentCaptor = ArgumentCaptor.forClass(Recording.class);
        verify(view).showRecording(showRecordingArgumentCaptor.capture());
        // Assert that the recording is correct
        assertTrue(showRecordingArgumentCaptor.getValue().getDate().equals(RECORDING.getDate()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void errorLoadingData_showMsg() {
        // Given an initialized RecordingPresenter
        // When start is requested
        presenter.start();
        // Callback is captured and invoked with stubbed recording
        verify(goBeesRepository).getRecording(anyLong(), anyLong(), any(Date.class), any(Date.class),
                getRecordingCallbackArgumentCaptor.capture());
        getRecordingCallbackArgumentCaptor.getValue().onDataNotAvailable();
        // Show error
        verify(view).showLoadingRecordingError();
    }

    @Test
    public void noRecords_showMsg() {
        Recording recording = new Recording(new Date(), new ArrayList<Record>());
        // Given an initialized RecordingPresenter
        // When start is requested
        presenter.start();
        // Callback is captured and invoked with stubbed recording
        verify(goBeesRepository).getRecording(anyLong(), anyLong(), any(Date.class), any(Date.class),
                getRecordingCallbackArgumentCaptor.capture());
        getRecordingCallbackArgumentCaptor.getValue().onRecordingLoaded(recording);
        // Show no records msg
        verify(view).showNoRecords();
    }

    @Test
    public void onOpenCharts_showCharts() {
        // Temp chart
        presenter.openTempChart();
        verify(view).showTempChart();
        // Rain chart
        presenter.openRainChart();
        verify(view).showRainChart();
        // Wind chart
        presenter.openWindChart();
        verify(view).showWindChart();
    }
}