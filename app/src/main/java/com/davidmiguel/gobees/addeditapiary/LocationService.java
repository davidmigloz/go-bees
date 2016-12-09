package com.davidmiguel.gobees.addeditapiary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

/**
 * Location service.
 */
public class LocationService {

    private static final int UPDATE_INTERVAL = 1000;

    private Context context;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    public LocationService(Context context,
                           ConnectionCallbacks connectionListener,
                           OnConnectionFailedListener connectionFailedListener) {
        this.context = context;
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionListener)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    public void connect() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void disconnect() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    public void createLocationRequest(ResultCallback<LocationSettingsResult> callback) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                        builder.build());
        result.setResultCallback(callback);
    }

    @SuppressWarnings("MissingPermission")
    public void startLocationUpdates(LocationListener listener) {
        if (locationRequest != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, listener);
        }
    }

    public void stopLocationUpdates(LocationListener listener) {
        if (locationRequest != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, listener);
        }
    }

}
