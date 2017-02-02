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

package com.davidmiguel.gobees.monitoring;

import com.davidmiguel.gobees.monitoring.algorithm.BeesCounter;
import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import org.opencv.android.CameraBridgeViewBase;

/**
 * This specifies the contract between the view and the presenter.
 */
interface MonitoringContract {

    interface View extends BaseView<MonitoringContract.Presenter> {
        /**
         * Inits OpenCV lib.
         *
         * @param listener CvCameraViewListener2.
         */
        void initOpenCV(CameraBridgeViewBase.CvCameraViewListener2 listener);

        /**
         * Enables the camera view.
         */
        void enableCameraView();

        /**
         * Update zoom parameter in the algorithm.
         *
         * @param ratio zoom (100 = x1, 200 = x2…).
         */
        void updateAlgoZoom(int ratio);

        /**
         * Sets the number of bees on the screen.
         *
         * @param numBees number of bees.
         */
        void setNumBees(int numBees);

        /**
         * Starts monitoring service.
         *
         * @param ms monitoring settings.
         */
        void startMonitoringgService(MonitoringSettings ms);

        /**
         * Stops monitoring service.
         */
        void stopMonitoringService();


        /**
         * Binds to the monitoring service.
         */
        void bindMonitoringService();

        /**
         * Hides the camera preview.
         */
        void hideCameraView();

        /**
         * Hides count down and shows monitoring view.
         */
        void showMonitoringView();

        /**
         * Hides or shows the num bees view.
         *
         * @param active true to show.
         */
        void showNumBeesView(boolean active);
    }

    interface SettingsView extends BaseView<MonitoringContract.Presenter> {
        /**
         * Inits settings and set the values stored in settings.
         */
        void initSettings();

        /**
         * Shows settings view.
         */
        void showSettings();

        /**
         * Hides settings view.
         */
        void hideSettings();

        /**
         * Get monitoring settings.
         *
         * @return monitoring settings.
         */
        MonitoringSettings getMonitoringSettings();
    }

    interface Presenter extends BasePresenter {

        /**
         * Called when OpenCV manager is connected.
         */
        void onOpenCvConnected();

        /**
         * Opens settings view.
         */
        void openSettings();

        /**
         * Starts monitoring.
         */
        void startMonitoring();

        /**
         * Stops monitoring.
         */
        void stopMonitoring();

        /**
         * Closes settings view.
         */
        void closeSettings();

        /**
         * Shows the output of the algorithm or the raw camera frame.
         *
         * @param status on/off.
         */
        void showAlgoOutput(boolean status);

        /**
         * Update blob size parameter in the algorithm.
         *
         * @param size blob size.
         */
        void updateAlgoBlobSize(BeesCounter.BlobSize size);

        /**
         * Update min area parameter in the algorithm.
         *
         * @param value min area.
         */
        void updateAlgoMinArea(double value);

        /**
         * Update max area parameter in the algorithm.
         *
         * @param value max area.
         */
        void updateAlgoMaxArea(double value);

        /**
         * Update zoom parameter in the algorithm.
         *
         * @param ratio zoom (100 = x1, 200 = x2…).
         */
        void updateAlgoZoom(int ratio);

        /**
         * Start presenter logic. It should be called by the view when it is prepared.
         *
         * @param serviceRunning whether the monitoring service is running.
         */
        void start(boolean serviceRunning);
    }
}
