package com.davidmiguel.gobees.addeditapiary;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetApiaryCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.TaskCallback;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;

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
    private Location apiaryLocation;

    AddEditApiaryPresenter(GoBeesRepository goBeesRepository,
                           AddEditApiaryContract.View view,
                           long apiaryId) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.apiaryId = apiaryId;
        view.setPresenter(this);
    }

    @Override
    public void saveApiary(String name, String notes) {
        if (isNewApiary()) {
            createApiary(name, notes, this);
        } else {
            updateApiary(name, notes);
        }
    }

    @Override
    public void populateApiary() {
        if (isNewApiary()) {
            throw new RuntimeException("populateApiary() was called but apiary is new.");
        }
        goBeesRepository.getApiary(apiaryId, this);
    }

    @Override
    public void toogleLocation(Context context) {
        if (locationService == null) {
            // Conect GPS service
            // TODO check permissions
            locationService = new LocationService(context, this, this);
            locationService.connect();
            view.setLocationIcon(true);
            view.showSearchingGpsMsg();
        } else {
            // Disconect GPS service
            stopLocation();
        }

    }

    @Override
    public void stopLocation() {
        if (locationService != null) {
            locationService.stopLocationUpdates(this);
            locationService.disconnect();
        }
        locationService = null;
        view.setLocationIcon(false);
    }

    @Override
    public void start() {
        if (!isNewApiary()) {
            populateApiary();
        }
    }

    @Override
    public void onApiaryLoaded(Apiary apiary) {
        // Show apiary data on view
        if (view.isActive()) {
            // Set name
            view.setName(apiary.getName());
            // Set notes
            view.setNotes(apiary.getNotes());
            // Set location
            if (apiary.getLocationLat() != null && apiary.getLocationLong() != null) {
                apiaryLocation = new Location("");
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
        // Apiary saved successfully -> go back to apiaries activity
        view.showApiariesList();
    }

    @Override
    public void onFailure() {
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
    public void onConnectionSuspended(int i) {
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
     * @param name     apiary name.
     * @param notes    apiary notes.
     * @param listener TaskCallback.
     */
    private void createApiary(final String name, final String notes, final TaskCallback listener) {
        // Get next id
        goBeesRepository.getNextApiaryId(new GoBeesDataSource.GetNextApiaryIdCallback() {
            @Override
            public void onNextApiaryIdLoaded(long apiaryId) {
                // Create apiary
                Apiary newApiary = new Apiary();
                // Set id
                newApiary.setId(apiaryId);
                // Set name
                newApiary.setName(name);
                // Set location
                if (apiaryLocation != null) {
                    newApiary.setLocationLat(apiaryLocation.getLatitude());
                    newApiary.setLocationLong(apiaryLocation.getLongitude());
                }
                // Set notes
                newApiary.setNotes(notes);
                // Save it if it is correct
                if (newApiary.isValidApiary()) {
                    goBeesRepository.saveApiary(newApiary, listener);
                } else {
                    view.showEmptyApiaryError();
                }
            }
        });
    }

    /**
     * Update and save an apiary.
     *
     * @param name  apiary name.
     * @param notes apiary notes.
     */
    private void updateApiary(String name, String notes) {
        if (isNewApiary()) {
            throw new RuntimeException("updateApiary() was called but apiary is new.");
        }
        // TODO refactor createApiary() and updateApiary() (almost same code)
        // Create new apiary with the modifications
        Apiary editedApiary = new Apiary();
        // Set id
        editedApiary.setId(apiaryId);
        // Set name
        editedApiary.setName(name);
        // Set location
        if (apiaryLocation != null) {
            editedApiary.setLocationLat(apiaryLocation.getLatitude());
            editedApiary.setLocationLong(apiaryLocation.getLongitude());
        }
        // Set notes
        editedApiary.setNotes(notes);
        goBeesRepository.saveApiary(editedApiary, this);
    }

    /**
     * Update apiary location.
     *
     * @param location new location.
     */
    private void updateLocation(@NonNull Location location) {
        apiaryLocation = location;
        view.setLocation(apiaryLocation);
    }
}
