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

package com.davidmiguel.gobees.apiaries;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.BaseLoadDataPresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ApiariesContract {

    interface View extends BaseView<LoadDataPresenter> {

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Shows list of apiaries.
         *
         * @param apiaries apiaries to show (list cannot be empty).
         */
        void showApiaries(@NonNull List<Apiary> apiaries);

        /**
         * Notifies that the apiaries data has changed and the list must be updated.
         */
        void notifyApiariesUpdated();

        /**
         * Opens activity to add or edit an apiary.
         *
         * @param apiaryId apiary id (or -1 for creating a new one).
         */
        void showAddEditApiary(long apiaryId);

        /**
         * Opens activity to show the details of the given apiary.
         *
         * @param apiaryId apiary to show.
         */
        void showApiaryDetail(long apiaryId);

        /**
         * Shows loading apiaries error message.
         */
        void showLoadingApiariesError();

        /**
         * Makes visible the no apiaries view.
         */
        void showNoApiaries();

        /**
         * Shows successfully saved message.
         */
        void showSuccessfullySavedMessage();

        /**
         * Shows successfully deleted message.
         */
        void showSuccessfullyDeletedMessage();

        /**
         * Shows error while deleting apiary message.
         */
        void showDeletedErrorMessage();

        /**
         * Shows error while updating current weather message.
         */
        void showWeatherUpdateErrorMessage();
    }

    interface LoadDataPresenter extends BaseLoadDataPresenter {

        /**
         * Shows a snackbar showing whether an apiary was successfully added or not.
         *
         * @param requestCode request code from the intent.
         * @param resultCode  result code from the intent.
         */
        void result(int requestCode, int resultCode);

        /**
         * Orders to open activity to add or edit an apiary.
         *
         * @param apiaryId apiary id (or -1 for creating a new one).
         */
        void addEditApiary(long apiaryId);

        /**
         * Opens activity to show the details of the given apiary.
         *
         * @param requestedApiary apiary to show.
         */
        void openApiaryDetail(@NonNull Apiary requestedApiary);

        /**
         * Deletes given apiary.
         *
         * @param apiary apiary to delete.
         */
        void deleteApiary(@NonNull Apiary apiary);
    }
}
