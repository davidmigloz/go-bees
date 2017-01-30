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

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.mothers.HiveMother;
import com.davidmiguel.gobees.data.model.mothers.RecordingMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetHiveCallback;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
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
import static org.mockito.Matchers.anyLong;
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

    private HivePresenter hivePresenter;

    @Captor
    private ArgumentCaptor<GetHiveCallback> getHiveCallbackArgumentCaptor;

    @Before
    public void setupHivesPresenter() {
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
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loadRecordings_showRecordingsIntoView() {
        // Given an initialized HivePresenter
        // When loading of recordings is requested
        hivePresenter.loadData(true);

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
}