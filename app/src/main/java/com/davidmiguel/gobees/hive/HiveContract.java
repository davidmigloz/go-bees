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

package com.davidmiguel.gobees.hive;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.BaseLoadDataPresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.Date;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface HiveContract {

    interface HiveRecordingsView extends BaseView<HiveContract.Presenter> {

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Shows list of recordings.
         *
         * @param recordings recordings to show (list cannot be empty).
         */
        void showRecordings(@NonNull List<Recording> recordings);

        /**
         * Opens activity to record a hive.
         *
         * @param apiaryId apiary id.
         * @param hiveId   hive id.
         */
        void startNewRecording(long apiaryId, long hiveId);

        /**
         * Opens activity to show the details of the given recording.
         *
         * @param apiaryId apiary id.
         * @param hiveId   hive id.
         * @param date     recording date.
         */
        void showRecordingDetail(long apiaryId, long hiveId, Date date);

        /**
         * Shows loading recordings error message.
         */
        void showLoadingRecordingsError();

        /**
         * Makes visible the no recordings view.
         */
        void showNoRecordings();

        /**
         * Shows successfully saved message.
         */
        void showSuccessfullySavedMessage();

        /**
         * Shows error while saving message.
         */
        void showSaveErrorMessage();

        /**
         * Shows recording too short error message.
         */
        void showRecordingTooShortErrorMessage();

        /**
         * Shows successfully deleted message.
         */
        void showSuccessfullyDeletedMessage();

        /**
         * Shows error while deleting recording message.
         */
        void showDeletedErrorMessage();

        /**
         * Sets the title in the action bar.
         *
         * @param title title.
         */
        void showTitle(@NonNull String title);

        /**
         * Checks whether ACCESS_FINE_LOCATION permission is granted. If not, asks for it.
         *
         * @return if the permission is granted.
         */
        boolean checkCameraPermission();
    }

    interface HiveInfoView extends BaseView<HiveContract.Presenter> {

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Shows the hive info.
         *
         * @param hive hive to show.
         */
        void showInfo(Hive hive);
    }

    interface Presenter extends BaseLoadDataPresenter {

        /**
         * Shows a snackbar showing whether a hive was successfully added or not.
         *
         * @param requestCode request code from the intent.
         * @param resultCode  result code from the intent.
         * @param data        intent data.
         */
        void result(int requestCode, int resultCode, Intent data);

        /**
         * Orders to open activity to record a hive.
         */
        void startNewRecording();

        /**
         * Opens activity to show the details of the given hive.
         *
         * @param recording recording.
         */
        void openRecordingsDetail(@NonNull Recording recording);

        /**
         * Deletes given recording.
         *
         * @param recording recording to delete.
         */
        void deleteRecording(@NonNull Recording recording);
    }
}
