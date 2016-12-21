package com.davidmiguel.gobees.monitoring;

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;
import com.davidmiguel.gobees.video.BeesCounter;

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
    }
}
