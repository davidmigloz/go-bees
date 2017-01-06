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

package com.davidmiguel.gobees.addeditapiary;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetNextApiaryIdCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetApiaryCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.TaskCallback;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of AddEditApiaryPresenter.
 */
public class AddEditApiaryPresenterTest {

    @Mock
    private GoBeesRepository apiariesRepository;

    @Mock
    private AddEditApiaryContract.View addeditapiaryView;

    private AddEditApiaryPresenter addEditApiaryPresenter;

    @Captor
    private ArgumentCaptor<GetApiaryCallback> getApiaryCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<GetNextApiaryIdCallback> getNextApiaryIdCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<TaskCallback> taskCallbackArgumentCaptor;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active
        when(addeditapiaryView.isActive()).thenReturn(true);
    }

    @Test
    public void saveNewApiaryToRepository_showsSuccessMessage() {
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView,
                        AddEditApiaryActivity.NEW_APIARY);
        // When the presenter is asked to save an apiary
        addEditApiaryPresenter.save("Apiary 1", "Some notes about it....");
        // Then a new id is requested
        verify(apiariesRepository).getNextApiaryId(getNextApiaryIdCallbackArgumentCaptor.capture());
        getNextApiaryIdCallbackArgumentCaptor.getValue().onNextApiaryIdLoaded(1);
        // And the apiary is saved in the repository
        verify(apiariesRepository)
                .saveApiary(any(Apiary.class), taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // And the view updated
        verify(addeditapiaryView).showApiariesList();
    }

    @Test
    public void saveEmptyApiary_showsErrorUi() {
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView,
                        AddEditApiaryActivity.NEW_APIARY);
        // When the presenter is asked to save an empty apiary
        addEditApiaryPresenter.save("", "");
        // Then a new id is requested
        verify(apiariesRepository).getNextApiaryId(getNextApiaryIdCallbackArgumentCaptor.capture());
        getNextApiaryIdCallbackArgumentCaptor.getValue().onNextApiaryIdLoaded(1);
        // Then an empty apiary error is shown in the UI
        verify(addeditapiaryView).showEmptyApiaryError();
    }

    @Test
    public void saveExistingApiaryToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test for apiary with id=1
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView, 1);
        addEditApiaryPresenter.onApiaryLoaded(ApiaryMother.newDefaultApiary());
        // When the presenter is asked to save an apiary
        addEditApiaryPresenter.save("Apiary 1", "Some more notes about it....");
        // Then an apiary is saved in the repository
        verify(apiariesRepository)
                .saveApiary(any(Apiary.class), taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // And the view updated
        verify(addeditapiaryView).showApiariesList();
    }

    @Test
    public void populateApiary_callsRepoAndUpdatesView() {
        Apiary testApiary = ApiaryMother.newDefaultApiary();
        // Get a reference to the class under test
        addEditApiaryPresenter = new AddEditApiaryPresenter(
                apiariesRepository, addeditapiaryView, testApiary.getId());
        // When the presenter is asked to populate an existing apiary
        addEditApiaryPresenter.populateApiary();
        // Then the apiaries repository is queried and the view updated
        verify(apiariesRepository).getApiary(eq(testApiary.getId()),
                getApiaryCallbackArgumentCaptor.capture());
        // Simulate callback
        getApiaryCallbackArgumentCaptor.getValue().onApiaryLoaded(testApiary);
        // Verify UI has been updated
        verify(addeditapiaryView).setName(testApiary.getName());
        verify(addeditapiaryView).setNotes(testApiary.getNotes());
    }
}