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

package com.davidmiguel.gobees.apiary;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.utils.BaseLoadDataPresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.Date;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ApiaryContract {

    interface ApiaryHivesView extends BaseView<ApiaryContract.Presenter> {

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Shows list of hives.
         *
         * @param hives hives to show (list cannot be empty).
         */
        void showHives(@NonNull List<Hive> hives);

        /**
         * Opens activity to add or edit a hive.
         *
         * @param apiaryId apiary id.
         * @param hiveId   hive id (or -1 for creating a new one).
         */
        void showAddEditHive(long apiaryId, long hiveId);

        /**
         * Opens activity to show the details of the given hive.
         *
         * @param apiaryId apiary id.
         * @param hiveId   hive to show.
         */
        void showHiveDetail(long apiaryId, long hiveId);

        /**
         * Shows loading hives error message.
         */
        void showLoadingHivesError();

        /**
         * Makes visible the no hives view.
         */
        void showNoHives();

        /**
         * Shows successfully saved message.
         */
        void showSuccessfullySavedMessage();

        /**
         * Shows successfully deleted message.
         */
        void showSuccessfullyDeletedMessage();

        /**
         * Shows error while deleting hive message.
         */
        void showDeletedErrorMessage();

        /**
         * Sets the title in the action bar.
         *
         * @param title title.
         */
        void showTitle(@NonNull String title);
    }

    interface ApiaryInfoView extends BaseView<ApiaryContract.Presenter> {

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Shows the hive info.
         *
         * @param apiary           apiary to show.
         * @param lastRevisionDate apiary last revision.
         */
        void showInfo(Apiary apiary, Date lastRevisionDate);

        /**
         * Shows apiary location on a map.
         *
         * @param apiary apiary to show.
         */
        void openMap(Apiary apiary);
    }

    interface Presenter extends BaseLoadDataPresenter {

        /**
         * Shows a snackbar showing whether a hive was successfully added or not.
         *
         * @param requestCode request code from the intent.
         * @param resultCode  result code from the intent.
         */
        void result(int requestCode, int resultCode);

        /**
         * Orders to open activity to add or edit a hive.
         *
         * @param hiveId hive id (or -1 for creating a new one).
         */
        void addEditHive(long hiveId);

        /**
         * Opens activity to show the details of the given hive.
         *
         * @param requestedHive hive to show.
         */
        void openHiveDetail(@NonNull Hive requestedHive);

        /**
         * Deletes given hive.
         *
         * @param hive hive to delete.
         */
        void deleteHive(@NonNull Hive hive);

        /**
         * Called when the user clicks the map icon (to show the apiary on a map).
         */
        void onOpenMapClicked();
    }
}
