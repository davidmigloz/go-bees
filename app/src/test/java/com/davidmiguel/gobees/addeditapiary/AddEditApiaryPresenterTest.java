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

import android.location.Location;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetApiaryCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetNextApiaryIdCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.TaskCallback;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
import com.google.android.gms.common.ConnectionResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
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

    @Mock
    private LocationService locationService;

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
        // We have location permissions
        when(addeditapiaryView.checkLocationPermission()).thenReturn(true);
    }

    @Test
    public void saveNewApiaryToRepository_showsSuccessMessage() {
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView,
                        AddEditApiaryActivity.NEW_APIARY, locationService);
        // When the presenter is asked to save an apiary
        addEditApiaryPresenter.save("Apiary 1", "40.741895", "-73.989308",
                "Some notes about it....");
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
                        AddEditApiaryActivity.NEW_APIARY, locationService);
        // When the presenter is asked to save an empty apiary
        addEditApiaryPresenter.save("", "", "", "");
        // Then an empty apiary error is shown in the UI
        verify(addeditapiaryView).showEmptyApiaryError();
    }

    @Test
    public void saveInvalidLocation_showsErrorUi() {
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView,
                        AddEditApiaryActivity.NEW_APIARY, locationService);
        // When the presenter is asked to save an apiary with invalid location
        addEditApiaryPresenter.save("Apiary 1", "999.741895", "999.989308",
                "Some notes about it....");
        // Then an empty apiary error is shown in the UI
        verify(addeditapiaryView).showInvalidLocationError();
    }

    @Test
    public void saveApiaryErrorSaving_showsErrorUi() {
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView,
                        AddEditApiaryActivity.NEW_APIARY, locationService);
        // When the presenter is asked to save an empty apiary
        addEditApiaryPresenter.save("Apiary 1", "", "", "");
        // Then a new id is requested
        verify(apiariesRepository).getNextApiaryId(getNextApiaryIdCallbackArgumentCaptor.capture());
        getNextApiaryIdCallbackArgumentCaptor.getValue().onNextApiaryIdLoaded(1);
        // Then the apiaries repository is queried
        verify(apiariesRepository).saveApiary(any(Apiary.class),
                taskCallbackArgumentCaptor.capture());
        // Simulate callback
        taskCallbackArgumentCaptor.getValue().onFailure();
        // Then an empty apiary error is shown in the UI
        verify(addeditapiaryView).showSaveApiaryError();
    }

    @Test
    public void saveApiaryWithoutLocationAndNotes_showsSuccessMessage() {
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView,
                        AddEditApiaryActivity.NEW_APIARY, locationService);
        // When the presenter is asked to save an apiary
        addEditApiaryPresenter.save("Apiary 1", "", "",
                "");
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
    public void saveExistingApiaryToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test for apiary with id=1
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView, 1,
                        locationService);
        addEditApiaryPresenter.onApiaryLoaded(ApiaryMother.newDefaultApiary());
        // When the presenter is asked to save an apiary
        addEditApiaryPresenter.save("Apiary 1", "40.741895", "-73.989308",
                "Some notes about it....");
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
                apiariesRepository, addeditapiaryView, testApiary.getId(), locationService);
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

    @Test
    public void populateApiary_errorLoadingData() {
        Apiary testApiary = ApiaryMother.newDefaultApiary();
        // Get a reference to the class under test
        addEditApiaryPresenter = new AddEditApiaryPresenter(
                apiariesRepository, addeditapiaryView, testApiary.getId(), locationService);
        // When the presenter is asked to populate an existing apiary
        addEditApiaryPresenter.start();
        // Then the apiaries repository is queried and the view updated
        verify(apiariesRepository).getApiary(eq(testApiary.getId()),
                getApiaryCallbackArgumentCaptor.capture());
        // Simulate callback
        getApiaryCallbackArgumentCaptor.getValue().onDataNotAvailable();
        // Verify UI has been updated
        verify(addeditapiaryView).showEmptyApiaryError();
    }

    @Test
    public void startAndStopLocationService() {
        // Location service is not connected
        when(locationService.isConnected()).thenReturn(false);
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView, 1,
                        locationService);
        // Click get location
        addEditApiaryPresenter.toogleLocation();
        // Verify connection location service
        verify(locationService).connect();
        verify(addeditapiaryView).setLocationIcon(eq(true));
        verify(addeditapiaryView).showSearchingGpsMsg();
        // Now, Location service is  connected
        when(locationService.isConnected()).thenReturn(true);
        // Stop location service
        addEditApiaryPresenter.toogleLocation();
        verify(locationService).stopLocationUpdates(eq(addEditApiaryPresenter));
        verify(locationService).disconnect();
        verify(addeditapiaryView).setLocationIcon(eq(false));
    }

    @Test
    public void receiveLocations() {
        // Location to send
        Location location = new Location("");
        when(locationService.getLastLocation()).thenReturn(location);
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView, 1,
                        locationService);
        // Send location
        InOrder inOrder = inOrder(addeditapiaryView);
        addEditApiaryPresenter.onConnected(null);
        verify(locationService).getLastLocation();
        inOrder.verify(addeditapiaryView).setLocation(eq(location));
        verify(locationService).createLocationRequest(addEditApiaryPresenter);
        verify(locationService).startLocationUpdates(addEditApiaryPresenter);
        // Update location
        addEditApiaryPresenter.onLocationChanged(location);
        inOrder.verify(addeditapiaryView).setLocation(eq(location));
    }

    @Test
    public void locationNotAvailable_ErrorMsg() {
        // Get a reference to the class under test
        addEditApiaryPresenter =
                new AddEditApiaryPresenter(apiariesRepository, addeditapiaryView, 1,
                        locationService);
        // Tell error
        addEditApiaryPresenter.onConnectionFailed(new ConnectionResult(-1));
        verify(addeditapiaryView).showGpsConnectionError();
        addEditApiaryPresenter.onConnectionSuspended(-1);
    }
}