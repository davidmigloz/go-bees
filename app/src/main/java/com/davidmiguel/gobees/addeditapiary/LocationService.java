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

    private Context context;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    /**
     * Location service constructor.
     *
     * @param context context.
     */
    LocationService(Context context) {
        this.context = context;
    }

    /**
     * Set location service listeners.
     *
     * @param connectionListener       ConnectionCallbacks.
     * @param connectionFailedListener OnConnectionFailedListener.
     */
    void setListeners(ConnectionCallbacks connectionListener,
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
     * Check if location service is connected.
     *
     * @return status.
     */
    boolean isConnected() {
        return googleApiClient != null && googleApiClient.isConnected();
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
