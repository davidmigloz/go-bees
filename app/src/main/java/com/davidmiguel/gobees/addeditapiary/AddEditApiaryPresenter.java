package com.davidmiguel.gobees.addeditapiary;

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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Listens to user actions from the UI AddEditApiaryFragment, retrieves the data and updates the
 * UI as required.
 */
class AddEditApiaryPresenter implements AddEditApiaryContract.Presenter,
        GetApiaryCallback, TaskCallback, ConnectionCallbacks, OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, LocationListener {

    private GoBeesRepository goBeesRepository;
    private AddEditApiaryContract.View addeditapiaryView;

    private long apiaryId;

    AddEditApiaryPresenter(GoBeesRepository goBeesRepository,
                           AddEditApiaryContract.View addeditapiaryView,
                           long apiaryId) {
        this.goBeesRepository = goBeesRepository;
        this.addeditapiaryView = addeditapiaryView;
        this.apiaryId = apiaryId;
        addeditapiaryView.setPresenter(this);
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
    public void start() {
        if (!isNewApiary()) {
            populateApiary();
        }
    }

    @Override
    public void onApiaryLoaded(Apiary apiary) {
        // Show apiary data on view
        if (addeditapiaryView.isActive()) {
            addeditapiaryView.setName(apiary.getName());
            addeditapiaryView.setNotes(apiary.getNotes());
        }
    }

    @Override
    public void onDataNotAvailable() {
        // Show error message
        if (addeditapiaryView.isActive()) {
            addeditapiaryView.showEmptyApiaryError();
        }
    }

    @Override
    public void onSuccess() {
        // Apiary saved successfully -> go back to apiaries activity
        addeditapiaryView.showApiariesList();
    }

    @Override
    public void onFailure() {
        // Error saving apiaries
        addeditapiaryView.showSaveApiaryError();
    }

    private boolean isNewApiary() {
        return apiaryId == AddEditApiaryActivity.NEW_APIARY;
    }

    private void createApiary(final String name, final String notes, final TaskCallback listener) {
        // Get next id
        goBeesRepository.getNextApiaryId(new GoBeesDataSource.GetNextApiaryIdCallback() {
            @Override
            public void onNextApiaryIdLoaded(long apiaryId) {
                // Create apiary
                Apiary newApiary =
                        new Apiary(apiaryId, name, null, null, null, notes, null, null, null);
                // Save it if it is correct
                if (newApiary.isValidApiary()) {
                    goBeesRepository.saveApiary(newApiary, listener);
                } else {
                    addeditapiaryView.showEmptyApiaryError();
                }
            }
        });
    }

    private void updateApiary(String name, String notes) {
        if (isNewApiary()) {
            throw new RuntimeException("updateApiary() was called but apiary is new.");
        }
        // Create new apiary with the modifications
        Apiary editedApiary =
                new Apiary(apiaryId, name, null, null, null, notes, null, null, null);
        goBeesRepository.saveApiary(editedApiary, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        //final LocationSettingsStates = locationSettingsResult.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can
                // initialize location requests here.
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
//                try {
//                    // Show the dialog by calling startResolutionForResult(),
//                    // and check the result in onActivityResult().
//                    status.startResolutionForResult(
//                            OuterClass.this,
//                            REQUEST_CHECK_SETTINGS);
//                } catch (SendIntentException e) {
//                    // Ignore the error.
//                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way
                // to fix the settings so we won't show the dialog.
                break;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
