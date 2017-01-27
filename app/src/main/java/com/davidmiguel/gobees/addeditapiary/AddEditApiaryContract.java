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

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
interface AddEditApiaryContract {

    interface View extends BaseView<Presenter> {

        /**
         * Sets apiary name in the text view.
         *
         * @param name apiary name.
         */
        void setName(String name);

        /**
         * Sets apiary location in the text view.
         *
         * @param location current location.
         */
        void setLocation(Location location);

        /**
         * Sets apiary notes in the text view.
         *
         * @param notes apiary notes.
         */
        void setNotes(String notes);

        /**
         * Set the location icon active or inactive.
         *
         * @param active status.
         */
        void setLocationIcon(boolean active);

        /**
         * Goes back to apiaries activity.
         */
        void showApiariesList();

        /**
         * Shows message informing that the GPS is running.
         */
        void showSearchingGpsMsg();

        /**
         * Shows message warning that the apiary cannot be empty.
         */
        void showEmptyApiaryError();

        /**
         * Shows message warning that the location is invalid.
         */
        void showInvalidLocationError();

        /**
         * Shows message warning that we cannot connect to GPS.
         */
        void showGpsConnectionError();

        /**
         * Shows save error message.
         */
        void showSaveApiaryError();

        /**
         * Checks whether ACCESS_FINE_LOCATION permission is granted. If not, asks for it.
         *
         * @return if the permission is granted.
         */
        boolean checkLocationPermission();

        /**
         * Closes / hides soft Android keyboard.
         */
        void closeKeyboard();
    }

    interface Presenter extends BasePresenter {

        /**
         * Saves or updates an apiary in the repository.
         *
         * @param name      apiary name.
         * @param latitude  latitude coordinate.
         * @param longitude longitude coordinate.
         * @param notes     apiary notes.
         */
        void save(String name, String latitude, String longitude, String notes);

        /**
         * Fill apiary data (the apiary must already exist in the repository).
         */
        void populateApiary();

        /**
         * Start/stop location service.
         */
        void toogleLocation();

        /**
         * Stop location service.
         */
        void stopLocation();
    }
}
