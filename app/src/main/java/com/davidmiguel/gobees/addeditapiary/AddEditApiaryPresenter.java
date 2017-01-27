/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees.addeditapiary;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetApiaryCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.TaskCallback;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
import com.davidmiguel.gobees.utils.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.common.base.Strings;

/**
 * Listens to user actions from the UI AddEditApiaryFragment, retrieves the data and updates the
 * UI as required.
 */
class AddEditApiaryPresenter implements AddEditApiaryContract.Presenter,
        GetApiaryCallback, TaskCallback, ConnectionCallbacks, OnConnectionFailedListener,
        LocationListener {

    private GoBeesRepository goBeesRepository;
    private AddEditApiaryContract.View view;

    private LocationService locationService;

    private long apiaryId;
    private Apiary apiary;

    AddEditApiaryPresenter(GoBeesRepository goBeesRepository,
                           AddEditApiaryContract.View view,
                           long apiaryId, LocationService locationService) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.apiaryId = apiaryId;
        if (isNewApiary()) {
            apiary = new Apiary();
        }
        view.setPresenter(this);
        this.locationService = locationService;
        this.locationService.setListeners(this, this);
    }

    @Override
    public void save(String name, String latitude, String longitude, String notes) {
        Double lat = null;
        Double lon = null;
        // Check name not empty
        if (Strings.isNullOrEmpty(name)) {
            view.showEmptyApiaryError();
            return;
        }
        // Check location if it exists
        else if (hasLocation(latitude, longitude)) {
            lat = Double.parseDouble(latitude.replace(',', '.'));
            lon = Double.parseDouble(longitude.replace(',', '.'));
            if (!LocationUtils.isValidLocation(lat, lon)) {
                view.showInvalidLocationError();
                return;
            }
        }
        // Create or update
        if (isNewApiary()) {
            createApiary(name, lat, lon, notes);
        } else {
            updateApiary(name, lat, lon, notes);
        }
    }

    @Override
    public void populateApiary() {
        if (!isNewApiary()) {
            goBeesRepository.getApiary(apiaryId, this);
        }
    }

    @Override
    public void toogleLocation() {
        if (view.checkLocationPermission()) {
            if (!locationService.isConnected()) {
                // Connect GPS service
                locationService.connect();
                view.setLocationIcon(true);
                view.showSearchingGpsMsg();
            } else {
                // Disconnect GPS service
                stopLocation();
            }
        }
    }

    @Override
    public void stopLocation() {
        if (locationService.isConnected()) {
            locationService.stopLocationUpdates(this);
            locationService.disconnect();
        }
        view.setLocationIcon(false);
    }

    @Override
    public void start() {
        if (!isNewApiary()) {
            populateApiary();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onApiaryLoaded(Apiary apiary) {
        this.apiary = apiary;
        // Show apiary data on view
        if (view.isActive()) {
            // Set name
            view.setName(apiary.getName());
            // Set notes
            view.setNotes(apiary.getNotes());
            // Set location
            if (apiary.hasLocation()) {
                Location apiaryLocation = new Location("");
                apiaryLocation.setLatitude(apiary.getLocationLat());
                apiaryLocation.setLongitude(apiary.getLocationLong());
                view.setLocation(apiaryLocation);
            }
        }
    }

    @Override
    public void onDataNotAvailable() {
        // Show error message
        if (view.isActive()) {
            view.showEmptyApiaryError();
        }
    }

    @Override
    public void onSuccess() {
        // Close keyboard
        view.closeKeyboard();
        // Apiary saved successfully -> go back to apiaries activity
        view.showApiariesList();
    }

    @Override
    public void onFailure() {
        // Close keyboard
        view.closeKeyboard();
        // Error saving apiaries
        view.showSaveApiaryError();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // When we are connected to Google Play service
        // Get last known recent location (maybe it's not very accurate)
        Location lastLocation = locationService.getLastLocation();
        // Note that this can be NULL if last location isn't already known
        if (lastLocation != null) {
            // Show last location
            updateLocation(lastLocation);
        }
        // Begin polling for new location updates
        locationService.createLocationRequest(this);
        locationService.startLocationUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // When we get a location update
        if (location != null) {
            // Show current location
            updateLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // We don't need to perform any action
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Show error msg
        view.showGpsConnectionError();
    }

    /**
     * Checks whether an apiary is new or not.
     *
     * @return true/false.
     */
    private boolean isNewApiary() {
        return apiaryId == AddEditApiaryActivity.NEW_APIARY;
    }

    /**
     * Create an save a new apiary.
     *
     * @param name      apiary name.
     * @param latitude  latitude coordinate.
     * @param longitude longitude coordinate.
     * @param notes     apiary notes.
     */
    private void createApiary(final String name, final Double latitude, final Double longitude,
                              final String notes) {
        // Get next id
        goBeesRepository.getNextApiaryId(new GoBeesDataSource.GetNextApiaryIdCallback() {
            @Override
            public void onNextApiaryIdLoaded(long apiaryId) {
                saveApiary(apiaryId, name, latitude, longitude, notes);
            }
        });
    }

    /**
     * Update and save an apiary.
     *
     * @param name  apiary name.
     * @param notes apiary notes.
     */
    private void updateApiary(String name, Double latitude, Double longitude, String notes) {
        if (!isNewApiary()) {
            saveApiary(apiaryId, name, latitude, longitude, notes);
        }
    }

    /**
     * Saves (or update) the apiary.
     *
     * @param apiaryId apiary id.
     * @param name     apiary name.
     * @param notes    apiary notes.
     */
    private void saveApiary(long apiaryId, String name, Double latitude, Double longitude,
                            String notes) {
        // Set id
        apiary.setId(apiaryId);
        // Set name
        apiary.setName(name);
        // Set location
        if (latitude != null && longitude != null) {
            apiary.setLocationLat(latitude);
            apiary.setLocationLong(longitude);
        }
        // Set notes
        apiary.setNotes(notes);
        // Reset current weather if exists
        apiary.setCurrentWeather(null);
        // Save it if it is correct
        goBeesRepository.saveApiary(apiary, this);
    }

    /**
     * Update apiary location.
     *
     * @param location new location.
     */
    private void updateLocation(@NonNull Location location) {
        view.setLocation(location);
    }

    /**
     * Returns whether a location has been introduced.
     *
     * @param latitude  latitude string.
     * @param longitude longitude string.
     * @return if it has location.
     */
    private boolean hasLocation(String latitude, String longitude) {
        return !Strings.isNullOrEmpty(latitude) && !Strings.isNullOrEmpty(longitude);
    }
}
