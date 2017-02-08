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

package com.davidmiguel.gobees.apiaries;

import android.app.Activity;

import com.davidmiguel.gobees.addeditapiary.AddEditApiaryActivity;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.MeteoRecord;
import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetApiariesCallback;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of ApiariesPresenter.
 */
public class ApiariesPresenterTest {

    private static List<Apiary> APIARIES;

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private ApiariesContract.View apiariesView;

    private ApiariesPresenter apiariesPresenter;

    @Captor
    private ArgumentCaptor<GetApiariesCallback> getApiariesCallbackCaptor;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.TaskCallback> taskCallbackArgumentCaptor;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        apiariesPresenter = new ApiariesPresenter(goBeesRepository, apiariesView);

        // The presenter won't update the view unless it's active
        when(apiariesView.isActive()).thenReturn(true);

        // Create 3 apiaries
        APIARIES = Lists.newArrayList(
                ApiaryMother.newDefaultApiary(),
                ApiaryMother.newDefaultApiary(),
                ApiaryMother.newDefaultApiary());
        // Add updated current weather to one
        MeteoRecord meteoRecord = new MeteoRecord();
        meteoRecord.setTimestamp(new Date());
        APIARIES.get(0).setCurrentWeather(meteoRecord);
        // Add old current weather to another
        MeteoRecord meteoRecord1 = new MeteoRecord();
        meteoRecord1.setTimestamp(new Date(1484866637));
        APIARIES.get(1).setCurrentWeather(meteoRecord1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loadAllApiariesFromRepositoryAndLoadIntoView() {
        // Given an initialized ApiariesPresenter with initialized apiaries
        // When loading of apiaries is requested
        apiariesPresenter.start();

        // Callback is captured and invoked with stubbed apiaries
        verify(goBeesRepository).getApiaries(getApiariesCallbackCaptor.capture());
        getApiariesCallbackCaptor.getValue().onApiariesLoaded(APIARIES);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(apiariesView);
        // Then progress indicator is hidden and all apairies are shown in UI
        inOrder.verify(apiariesView).setLoadingIndicator(false);
        ArgumentCaptor<List> showApiariesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(apiariesView).showApiaries(showApiariesArgumentCaptor.capture());
        // Assert that the number of apairies shown is the expected
        assertTrue(showApiariesArgumentCaptor.getValue().size() == APIARIES.size());
        // Check weather if one of them
        ArgumentCaptor<List> updateWeatherListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(goBeesRepository).updateApiariesCurrentWeather(
                updateWeatherListArgumentCaptor.capture(),
                taskCallbackArgumentCaptor.capture());
        assertTrue(updateWeatherListArgumentCaptor.getValue().size() == 2);
        // Update weather
        taskCallbackArgumentCaptor.getValue().onSuccess();
        verify(apiariesView).notifyApiariesUpdated();
    }

    @Test
    public void loadApiariesError_showMsg() {
        // Given an initialized ApiariesPresenter with initialized apiaries
        // When loading of apiaries is requested
        apiariesPresenter.start();
        // Callback is captured and invoked with stubbed apiaries
        verify(goBeesRepository).getApiaries(getApiariesCallbackCaptor.capture());
        getApiariesCallbackCaptor.getValue().onDataNotAvailable();
        // Show error
        verify(apiariesView).showLoadingApiariesError();
    }

    @Test
    public void newApiaryCreated_showMsg() {
        apiariesPresenter.result(AddEditApiaryActivity.REQUEST_ADD_APIARY,
                Activity.RESULT_OK);
        // Show msg
        verify(apiariesView).showSuccessfullySavedMessage();
    }

    @Test
    public void onAddEditApiary_openAddEditAct() {
        apiariesPresenter.addEditApiary(1);
        // Open act
        verify(apiariesView).showAddEditApiary(eq(1L));
    }

    @Test
    public void onApiaryClicked_openApiary() {
        apiariesPresenter.openApiaryDetail(APIARIES.get(0));
        // Open apiary
        verify(apiariesView).showApiaryDetail(eq(APIARIES.get(0).getId()));
    }

    @Test
    public void deleteApiary_showOkMsg() {
        apiariesPresenter.deleteApiary(APIARIES.get(0));
        // Delete apiary
        verify(goBeesRepository).deleteApiary(eq(APIARIES.get(0).getId()),
                taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // Show msg
        verify(apiariesView).showSuccessfullyDeletedMessage();
    }

    @Test
    public void deleteApiaryError_showError() {
        apiariesPresenter.deleteApiary(APIARIES.get(0));
        // Delete apiary error
        verify(goBeesRepository).deleteApiary(eq(APIARIES.get(0).getId()),
                taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onFailure();
        // Show msg
        verify(apiariesView).showDeletedErrorMessage();
    }
}