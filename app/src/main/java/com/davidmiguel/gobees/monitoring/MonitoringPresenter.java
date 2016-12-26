package com.davidmiguel.gobees.monitoring;

import com.davidmiguel.gobees.video.BeesCounter;
import com.davidmiguel.gobees.video.ContourBeesCounter;

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

    private long hiveId;
    private BeesCounter bc;
    private Mat processedFrame;
    private boolean showAlgoOutput;

    MonitoringPresenter(MonitoringContract.View view, MonitoringContract.SettingsView settingsView,
                        long hiveId) {
        this.view = view;
        this.view.setPresenter(this);
        this.settingsView = settingsView;
        this.settingsView.setPresenter(this);
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
    public void startRecording() {
        // Show monitoring view
        view.showMonitoringView();
        // Get settings and set hive id
        MonitoringSettings ms = settingsView.getMonitoringSettings();
        ms.setHiveId(hiveId);
        // Start recording service
        view.startRecordingService(ms);
        // Bind to service
        view.bindRecordingService();
    }

    @Override
    public void stopRecording() {
        view.stopRecordingService();
    }

    @Override
    public void closeSettings() {
        settingsView.hideSettings();
    }

    @Override
    public void showAlgoOutput(boolean status) {
        showAlgoOutput = status;
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
            view.bindRecordingService();
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
        bc = ContourBeesCounter.getInstance();
        settingsView.initSettings();
    }

    @Override
    public void onCameraViewStopped() {
        // No action needed
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        int numBees = bc.countBees(inputFrame.gray());
        view.setNumBees(numBees);
        bc.getProcessedFrame().copyTo(processedFrame);
        bc.getProcessedFrame().release();
        return showAlgoOutput ? processedFrame : inputFrame.rgba();
    }
}
