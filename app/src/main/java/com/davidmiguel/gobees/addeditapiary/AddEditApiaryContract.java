package com.davidmiguel.gobees.addeditapiary;

import android.content.Context;
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
    }

    interface Presenter extends BasePresenter {

        /**
         * Saves or updates an apiary in the repository.
         *
         * @param name  apiary name.
         * @param notes apiary notes.
         */
        void save(String name, String notes);

        /**
         * Fill apiary data (the apiary must already exist in the repository).
         */
        void populateApiary();

        /**
         * Start/stop location service.
         *
         * @param context context.
         */
        void toogleLocation(Context context);

        /**
         * Stop location service.
         */
        void stopLocation();
    }
}
