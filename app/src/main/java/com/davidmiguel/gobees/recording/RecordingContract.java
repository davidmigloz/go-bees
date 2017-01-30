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

package com.davidmiguel.gobees.recording;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.BaseLoadDataPresenter;
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

    interface Presenter extends BaseLoadDataPresenter {

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
    }
}
