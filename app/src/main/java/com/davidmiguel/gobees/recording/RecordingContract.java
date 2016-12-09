package com.davidmiguel.gobees.recording;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.Date;

/**
 * This specifies the contract between the view and the presenter.
 */
interface RecordingContract {

    interface View extends BaseView<Presenter> {

        /**
         * Sets the title in the action bar.
         *
         * @param date title.
         */
        void showTitle(@NonNull Date date);

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Show recording details.
         *
         * @param recording recording.
         */
        void showRecording(@NonNull Recording recording);

        /**
         * Shows loading recording error message.
         */
        void showLoadingRecordingError();

        /**
         * Makes visible the no records view.
         */
        void showNoRecords();

        /**
         * Shows temperature chart.
         */
        void showTempChart();

        /**
         * Shows rain chart.
         */
        void showRainChart();

        /**
         * Shows wind chart.
         */
        void showWindChart();

    }

    interface Presenter extends BasePresenter {

        /**
         * Opens temperature chart.
         */
        void openTempChart();

        /**
         * Opens rain chart.
         */
        void openRainChart();

        /**
         * Opens wind chart.
         */
        void openWindChart();

        /**
         * Delete the recording records.
         */
        void deleteRecording();
    }
}
