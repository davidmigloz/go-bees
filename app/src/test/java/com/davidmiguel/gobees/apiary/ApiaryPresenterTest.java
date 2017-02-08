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

package com.davidmiguel.gobees.apiary;

import android.app.Activity;

import com.davidmiguel.gobees.addedithive.AddEditHiveActivity;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of ApiaryPresenter.
 */
public class ApiaryPresenterTest {

    private static final long APIARY_ID = 1;
    private static final int NUM_BEES = 5;
    private static Apiary APIARY;

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private ApiaryContract.ApiaryHivesView apiaryHivesView;

    @Mock
    private ApiaryContract.ApiaryInfoView apiaryInfoView;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.TaskCallback> taskCallbackArgumentCaptor;

    private ApiaryPresenter apiaryPresenter;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetHivesCallback> getHivesCallbackCaptor;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetApiaryCallback> getApiaryCallbackArgumentCaptor;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        apiaryPresenter = new ApiaryPresenter(goBeesRepository,
                apiaryHivesView, apiaryInfoView, APIARY_ID);

        // The presenter won't update the view unless it's active
        when(apiaryHivesView.isActive()).thenReturn(true);

        // Create 3 hives
        APIARY = ApiaryMother.newDefaultApiary(NUM_BEES);
        APIARY.setId(APIARY_ID);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Test
    public void loadHives_showHivesIntoView() {
        // Given an initialized ApiaryPresenter
        // When loading of hives of apiary 1 is requested
        apiaryPresenter.loadData(true);

        // Callback is captured and invoked with stubbed hives
        verify(goBeesRepository).getApiary(anyLong(), getApiaryCallbackArgumentCaptor.capture());
        getApiaryCallbackArgumentCaptor.getValue().onApiaryLoaded(APIARY);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(apiaryHivesView);
        // Then progress indicator is hidden and all hives are shown in UI
        inOrder.verify(apiaryHivesView).setLoadingIndicator(false);
        ArgumentCaptor<List> showHivesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(apiaryHivesView).showHives(showHivesArgumentCaptor.capture());
        // Assert that the number of hives shown is the expected
        assertTrue(showHivesArgumentCaptor.getValue().size() == APIARY.getHives().size());
    }

    @Test
    public void loadHivesError_showMsg() {
        // Given an initialized ApiaryPresenter
        // When loading of apiaries is requested
        apiaryPresenter.start();
        // Callback is captured and invoked with stubbed apiaries
        verify(goBeesRepository).getApiary(anyLong(), getApiaryCallbackArgumentCaptor.capture());
        getApiaryCallbackArgumentCaptor.getValue().onDataNotAvailable();
        // Show error
        verify(apiaryHivesView).showLoadingHivesError();
    }

    @Test
    public void newApiaryCreated_showMsg() {
        apiaryPresenter.result(AddEditHiveActivity.REQUEST_ADD_HIVE,
                Activity.RESULT_OK);
        // Show msg
        verify(apiaryHivesView).showSuccessfullySavedMessage();
    }

    @Test
    public void onAddEditHive_openAddEditAct() {
        apiaryPresenter.addEditHive(1);
        // Open act
        verify(apiaryHivesView).showAddEditHive(eq(1L), eq(1L));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void onHiveClicked_openApiary() {
        apiaryPresenter.openHiveDetail(APIARY.getHives().get(0));
        // Open apiary
        verify(apiaryHivesView).showHiveDetail(eq(APIARY.getId()),
                eq(APIARY.getHives().get(0).getId()));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void deleteHive_showOkMsg() {
        apiaryPresenter.deleteHive(APIARY.getHives().get(0));
        // Delete hive
        verify(goBeesRepository).deleteHive(eq(APIARY.getHives().get(0).getId()),
                taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // Show msg
        verify(apiaryHivesView).showSuccessfullyDeletedMessage();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void deleteApiaryError_showError() {
        apiaryPresenter.deleteHive(APIARY.getHives().get(0));
        // Delete hive
        verify(goBeesRepository).deleteHive(eq(APIARY.getHives().get(0).getId()),
                taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onFailure();
        // Show msg
        verify(apiaryHivesView).showDeletedErrorMessage();
    }

    @Test
    public void onMapPressed_openMap() {
        apiaryPresenter.onOpenMapClicked();
        // Open map
        verify(apiaryInfoView).openMap(any(Apiary.class));
    }
}