package com.davidmiguel.gobees.hive;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.premonitoring.PreMonitoringActivity;

import java.util.Date;
import java.util.List;

/**
 * Listens to user actions from the UI HiveRecordingsFragment, retrieves the data and updates the
 * UI as required.
 */
public class HivePresenter implements HiveContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private HiveContract.View hiveView;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;
    private long hiveId;

    public HivePresenter(GoBeesRepository goBeesRepository, HiveContract.View hiveView,
                         long hiveId) {
        this.goBeesRepository = goBeesRepository;
        this.hiveView = hiveView;
        this.hiveView.setPresenter(this);
        this.hiveId = hiveId;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a hive was successfully added, show snackbar
        if (PreMonitoringActivity.REQUEST_RECORD_HIVE == requestCode && Activity.RESULT_OK == resultCode) {
            hiveView.showSuccessfullySavedMessage();
        }
        // TODO show error message if it fails
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
                // Process recordings
                if (hive.getRecordings().isEmpty()) {
                    // Show a message indicating there are no recordings
                    hiveView.showNoRecordings();
                } else {
                    // Set hive name as title
                    hiveView.showTitle(hive.getName());
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
        hiveView.startNewRecording();
    }

    @Override
    public void openRecordingsDetail(@NonNull Recording recording) {
        // TODO
    }

    @Override
    public void start() {
        loadRecordings(false);
    }
}
