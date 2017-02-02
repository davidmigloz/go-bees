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

import com.davidmiguel.gobees.monitoring.algorithm.AreaBeesCounter;
import com.davidmiguel.gobees.monitoring.algorithm.BeesCounter;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;

/**
 * Listens to user actions from the UI MonitoringFragment, retrieves the data and updates the
 * UI as required.
 */
class MonitoringPresenter implements MonitoringContract.Presenter, CvCameraViewListener2 {

    private MonitoringContract.View view;
    private MonitoringContract.SettingsView settingsView;

    private long apiaryId;
    private long hiveId;
    private BeesCounter bc;
    private Mat processedFrame;
    private boolean showAlgoOutput;

    MonitoringPresenter(MonitoringContract.View view, MonitoringContract.SettingsView settingsView,
                        long apiaryId, long hiveId) {
        this.view = view;
        this.view.setPresenter(this);
        this.settingsView = settingsView;
        this.settingsView.setPresenter(this);
        this.apiaryId = apiaryId;
        this.hiveId = hiveId;
    }

    @Override
    public void onOpenCvConnected() {
        view.enableCameraView();
    }

    @Override
    public void openSettings() {
        settingsView.showSettings();
    }

    @Override
    public void startMonitoring() {
        // Get settings and set hive id
        MonitoringSettings ms = settingsView.getMonitoringSettings();
        ms.setApiaryId(apiaryId);
        ms.setHiveId(hiveId);
        // Hide camera view
        view.hideCameraView();
        // Show monitoring view
        view.showMonitoringView();
        // Start recording service
        view.startMonitoringService(ms);
        // Bind to service
        view.bindMonitoringService();
    }

    @Override
    public void stopMonitoring() {
        view.stopMonitoringService();
    }

    @Override
    public void closeSettings() {
        settingsView.hideSettings();
    }

    @Override
    public void showAlgoOutput(boolean status) {
        showAlgoOutput = status;
        view.showNumBeesView(status);
    }

    @Override
    public void updateAlgoBlobSize(BeesCounter.BlobSize size) {
        bc.updateBlobSize(size);
    }

    @Override
    public void updateAlgoMinArea(double value) {
        bc.updateMinArea(value);
    }

    @Override
    public void updateAlgoMaxArea(double value) {
        bc.updateMaxArea(value);
    }

    @Override
    public void updateAlgoZoom(int ratio) {
        view.updateAlgoZoom(ratio);
    }

    @Override
    public void start(boolean serviceRunning) {
        if (serviceRunning) {
            // Bind to service (that is already running)
            view.bindMonitoringService();
            view.hideCameraView();
            view.showMonitoringView();
        } else {
            // Start camera view
            view.initOpenCV(this);
        }
    }

    @Override
    public void start() {
        // Not needed
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        processedFrame = new Mat();
        bc = AreaBeesCounter.getInstance();
        settingsView.initSettings();
    }

    @Override
    public void onCameraViewStopped() {
        // No action needed
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        if(showAlgoOutput) {
            int numBees = bc.countBees(inputFrame.gray());
            view.setNumBees(numBees);
            bc.getProcessedFrame().copyTo(processedFrame);
            bc.getProcessedFrame().release();
            return processedFrame;
        }
        // If show algorithm output is false -> show original frame
        return inputFrame.rgba();
    }
}
