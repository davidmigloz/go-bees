package com.davidmiguel.gobees.addeditapiary;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Location service.
 * Uses Google Play services location API.
 */
@SuppressWarnings("MissingPermission")
class LocationService {

    private static final int UPDATE_INTERVAL = 2000;
    private static final int FASTEST_INTERVAL = 1000;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    /**
     * Location service constructor.
     *
     * @param context                  context.
     * @param connectionListener       ConnectionCallbacks.
     * @param connectionFailedListener OnConnectionFailedListener.
     */
    LocationService(Context context,
                    ConnectionCallbacks connectionListener,
                    OnConnectionFailedListener connectionFailedListener) {
        // Create an instance of the Google Play services API client
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionListener)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Connects the client to Google Play services.
     */
    void connect() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    /**
     * Closes the connection to Google Play services.
     */
    void disconnect() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Configure parameters for requests to the fused location provider.
     *
     * @param callback LocationListener.
     */
    void createLocationRequest(LocationListener callback) {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // TODO check if gps is on and if not show a dialog to the user
        // TODO (now if it's off, we just use the network signal)
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                locationRequest, callback);
    }

    /**
     * Start requesting location updates.
     *
     * @param listener LocationListener.
     */
    void startLocationUpdates(LocationListener listener) {
        if (locationRequest != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, listener);
        }
    }

    /**
     * Stop requesting location updates.
     *
     * @param listener LocationListener.
     */
    void stopLocationUpdates(LocationListener listener) {
        if (locationRequest != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, listener);
        }
    }

    /**
     * Get last known location.
     *
     * @return location.
     */
    Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
    }
}
