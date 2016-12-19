package com.davidmiguel.gobees.monitoring;

import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
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

    private GoBeesRepository goBeesRepository;
    private MonitoringContract.View view;
    private MonitoringContract.SettingsView settingsView;

    private long hiveId;
    private BeesCounter bc;
    private Mat processedFrame;

    MonitoringPresenter(GoBeesRepository goBeesRepository, MonitoringContract.View view,
                        MonitoringContract.SettingsView settingsView, long hiveId) {
        this.goBeesRepository = goBeesRepository;
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
    public void closeSettings() {
        settingsView.hideSettings();
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
    public void updateAlgoZoom(double value) {
        // TODO
    }

    @Override
    public void start() {
        view.initOpenCV(this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        processedFrame = new Mat();
        bc = new ContourBeesCounter();
        settingsView.initSettings();
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        int numBees = bc.countBees(inputFrame.gray());
        view.setNumBees(numBees);
        bc.getProcessedFrame().copyTo(processedFrame);
        bc.getProcessedFrame().release();
        return processedFrame;
    }
}
