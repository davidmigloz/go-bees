package com.davidmiguel.gobees.hive;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.monitoring.MonitoringActivity;

/**
 * Listens to user actions from the UI HiveRecordingsFragment, retrieves the data and updates the
 * UI as required.
 */
class HivePresenter implements HiveContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private HiveContract.View hiveView;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;
    private long hiveId;

    HivePresenter(GoBeesRepository goBeesRepository, HiveContract.View hiveView,
                  long hiveId) {
        this.goBeesRepository = goBeesRepository;
        this.hiveView = hiveView;
        this.hiveView.setPresenter(this);
        this.hiveId = hiveId;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a recording was successfully saved, show snackbar
        if (MonitoringActivity.REQUEST_MONITORING == requestCode && Activity.RESULT_OK == resultCode) {
            // Refresh recordings
            loadRecordings(true);
            // Show message
            hiveView.showSuccessfullySavedMessage();
        }
    }
    @Override
    public void loadRecordings(boolean forceUpdate) {
        // Force update the first time
        forceUpdate = forceUpdate || firstLoad;
        firstLoad = false;
        // Show progress indicator
        hiveView.setLoadingIndicator(true);
        // Refresh data if needed
        if (forceUpdate) {
            goBeesRepository.refreshRecordings(hiveId);
        }
        // Get recordings
        goBeesRepository.getHiveWithRecordings(hiveId, new GoBeesDataSource.GetHiveCallback() {

            @Override
            public void onHiveLoaded(Hive hive) {
                // The view may not be able to handle UI updates anymore
                if (!hiveView.isActive()) {
                    return;
                }
                // Hide progress indicator
                hiveView.setLoadingIndicator(false);
                // Set hive name as title
                hiveView.showTitle(hive.getName());
                // Process recordings
                if (hive.getRecordings().isEmpty()) {
                    // Show a message indicating there are no recordings
                    hiveView.showNoRecordings();
                } else {
                    // Show the list of recordings
                    hiveView.showRecordings(hive.getRecordings());
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!hiveView.isActive()) {
                    return;
                }
                hiveView.showLoadingRecordingsError();
            }
        });
    }

    @Override
    public void startNewRecording() {
        hiveView.startNewRecording(hiveId);
    }

    @Override
    public void openRecordingsDetail(@NonNull Recording recording) {
        hiveView.showRecordingDetail(hiveId, recording.getDate());
    }

    @Override
    public void start() {
        loadRecordings(false);
    }
}
