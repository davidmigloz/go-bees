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

package com.davidmiguel.gobees.addedithive;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.mothers.HiveMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of AddEditHivePresenter.
 */
public class AddEditHivePresenterTest {

    private static final int APIARY_ID = 1;

    @Mock
    private GoBeesRepository apiariesRepository;

    @Mock
    private AddEditHiveContract.View addEditHiveView;

    private AddEditHivePresenter addEditHivePresenter;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetHiveCallback> getHiveCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetNextHiveIdCallback> getNextHiveIdCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.TaskCallback> taskCallbackArgumentCaptor;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active
        when(addEditHiveView.isActive()).thenReturn(true);
    }

    @Test
    public void saveNewHiveToRepository_showsSuccessMessage() {
        // Get a reference to the class under test
        addEditHivePresenter =
                new AddEditHivePresenter(apiariesRepository, addEditHiveView, APIARY_ID,
                        AddEditHiveActivity.NEW_HIVE);
        // When the presenter is asked to save a hive
        addEditHivePresenter.save("Hive 1", "Some notes about it....");
        // Then a new id is requested
        verify(apiariesRepository).getNextHiveId(getNextHiveIdCallbackArgumentCaptor.capture());
        getNextHiveIdCallbackArgumentCaptor.getValue().onNextHiveIdLoaded(1);
        // And the hive is saved in the repository
        verify(apiariesRepository)
                .saveHive(anyLong(), any(Hive.class), taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // And the view updated
        verify(addEditHiveView).showHivesList();
    }

    @Test
    public void saveEmptyApiary_showsErrorUi() {
        // Get a reference to the class under test
        addEditHivePresenter =
                new AddEditHivePresenter(apiariesRepository, addEditHiveView, APIARY_ID,
                        AddEditHiveActivity.NEW_HIVE);
        // When the presenter is asked to save an empty hive
        addEditHivePresenter.save("", "");
        // Then a new id is requested
        verify(apiariesRepository).getNextHiveId(getNextHiveIdCallbackArgumentCaptor.capture());
        getNextHiveIdCallbackArgumentCaptor.getValue().onNextHiveIdLoaded(1);
        // Then an empty hive error is shown in the UI
        verify(addEditHiveView).showEmptyHiveError();
    }

    @Test
    public void saveExistingApiaryToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test for hive with id=1
        addEditHivePresenter =
                new AddEditHivePresenter(apiariesRepository, addEditHiveView, APIARY_ID, 1);
        addEditHivePresenter.onHiveLoaded(HiveMother.newDefaultHive());
        // When the presenter is asked to save a hive
        addEditHivePresenter.save("Apiary 1", "Some more notes about it....");
        // Then a hive is saved in the repository
        verify(apiariesRepository)
                .saveHive(anyLong(), any(Hive.class), taskCallbackArgumentCaptor.capture());
        taskCallbackArgumentCaptor.getValue().onSuccess();
        // And the view updated
        verify(addEditHiveView).showHivesList();
    }

    @Test
    public void saveHiveErrorSaving_showsErrorUi() {
        // Get a reference to the class under test
        addEditHivePresenter = new AddEditHivePresenter(
                apiariesRepository, addEditHiveView, APIARY_ID, AddEditHiveActivity.NEW_HIVE);
        // When the presenter is asked to save an empty apiary
        addEditHivePresenter.save("Hive 1", "");
        // Then a new id is requested
        verify(apiariesRepository).getNextHiveId(getNextHiveIdCallbackArgumentCaptor.capture());
        getNextHiveIdCallbackArgumentCaptor.getValue().onNextHiveIdLoaded(1);
        // Then the apiaries repository is queried
        verify(apiariesRepository)
                .saveHive(anyLong(), any(Hive.class), taskCallbackArgumentCaptor.capture());
        // Simulate callback
        taskCallbackArgumentCaptor.getValue().onFailure();
        // Then an empty apiary error is shown in the UI
        verify(addEditHiveView).showSaveHiveError();
    }

    @Test
    public void populateHive_callsRepoAndUpdatesView() {
        Hive testHive = HiveMother.newDefaultHive();
        // Get a reference to the class under test
        addEditHivePresenter = new AddEditHivePresenter(
                apiariesRepository, addEditHiveView, APIARY_ID, testHive.getId());
        // When the presenter is asked to populate an existing hive
        addEditHivePresenter.start();
        // Then the repository is queried and the view updated
        verify(apiariesRepository).getHive(eq(testHive.getId()),
                getHiveCallbackArgumentCaptor.capture());
        // Simulate callback
        getHiveCallbackArgumentCaptor.getValue().onHiveLoaded(testHive);
        // Verify UI has been updated
        verify(addEditHiveView).setName(testHive.getName());
        verify(addEditHiveView).setNotes(testHive.getNotes());
    }

    @Test
    public void populateHive_errorLoadingData() {
        Hive testHive = HiveMother.newDefaultHive();
        // Get a reference to the class under test
        addEditHivePresenter = new AddEditHivePresenter(
                apiariesRepository, addEditHiveView, APIARY_ID, testHive.getId());
        // When the presenter is asked to populate an existing apiary
        addEditHivePresenter.start();
        // Then the apiaries repository is queried and the view updated
        verify(apiariesRepository).getHive(eq(testHive.getId()),
                getHiveCallbackArgumentCaptor.capture());
        // Simulate callback
        getHiveCallbackArgumentCaptor.getValue().onDataNotAvailable();
        // Verify UI has been updated
        verify(addEditHiveView).showEmptyHiveError();
    }
}