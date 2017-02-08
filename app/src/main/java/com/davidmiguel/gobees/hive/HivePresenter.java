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

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;
import com.davidmiguel.gobees.monitoring.MonitoringActivity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Listens to user actions from the UI HiveRecordingsFragment, retrieves the data and updates the
 * UI as required.
 */
class HivePresenter implements HiveContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private HiveContract.HiveRecordingsView hiveRecordingsView;
    private HiveContract.HiveInfoView hiveInfoView;
    private AtomicInteger ready;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;
    private long apiaryId;
    private long hiveId;

    HivePresenter(GoBeesRepository goBeesRepository,
                  HiveContract.HiveRecordingsView hiveRecordingsView,
                  HiveContract.HiveInfoView hiveInfoView,
                  long apiaryId, long hiveId) {
        this.goBeesRepository = goBeesRepository;
        this.hiveRecordingsView = hiveRecordingsView;
        this.hiveRecordingsView.setPresenter(this);
        this.hiveInfoView = hiveInfoView;
        this.hiveInfoView.setPresenter(this);
        this.apiaryId = apiaryId;
        this.hiveId = hiveId;
        this.ready = new AtomicInteger(0);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        // If a recording was successfully saved, show snackbar
        if (MonitoringActivity.REQUEST_MONITORING == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                // Refresh recordings
                loadData(true);
                // Show message
                hiveRecordingsView.showSuccessfullySavedMessage();
            } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
                // Get error type
                int error = data.getIntExtra(HiveRecordingsFragment.ARGUMENT_MONITORING_ERROR, -1);
                // Show error message
                switch (error) {
                    case HiveRecordingsFragment.ERROR_RECORDING_TOO_SHORT:
                        hiveRecordingsView.showRecordingTooShortErrorMessage();
                        break;
                    case HiveRecordingsFragment.ERROR_SAVING_RECORDING:
                        hiveRecordingsView.showSaveErrorMessage();
                        break;
                    default:
                        hiveRecordingsView.showSaveErrorMessage();
                }
            }
        }
    }

    @Override
    public void loadData(boolean forceUpdate) {
        // Force update the first time
        boolean update = forceUpdate || firstLoad;
        firstLoad = false;
        // Refresh data if needed
        if (update) {
            goBeesRepository.refreshRecordings(hiveId);
        }
        // Get recordings
        goBeesRepository.getHiveWithRecordings(hiveId, new GoBeesDataSource.GetHiveCallback() {

            @Override
            public void onHiveLoaded(Hive hive) {
                // The hiveRecordingsView may not be able to handle UI updates anymore
                if (!hiveRecordingsView.isActive() && !hiveInfoView.isActive()) {
                    return;
                }
                // Hide progress indicator
                hiveRecordingsView.setLoadingIndicator(false);
                hiveInfoView.setLoadingIndicator(false);
                // Set hive name as title
                hiveRecordingsView.showTitle(hive.getName());
                // Process recordings
                if (hive.getRecordings().isEmpty()) {
                    // Show a message indicating there are no recordings
                    hiveRecordingsView.showNoRecordings();
                } else {
                    // Show the list of recordings
                    hiveRecordingsView.showRecordings(hive.getRecordings());
                }
                // Show hive info
                hiveInfoView.showInfo(hive);
            }

            @Override
            public void onDataNotAvailable() {
                // The hiveRecordingsView may not be able to handle UI updates anymore
                if (!hiveRecordingsView.isActive() || !hiveInfoView.isActive()) {
                    return;
                }
                // Hide progress indicator
                hiveRecordingsView.setLoadingIndicator(false);
                hiveInfoView.setLoadingIndicator(false);
                // Show error
                hiveRecordingsView.showLoadingRecordingsError();
            }
        });
    }

    @Override
    public void startNewRecording() {
        if (hiveRecordingsView.checkCameraPermission()) {
            hiveRecordingsView.startNewRecording(apiaryId, hiveId);
        }
    }

    @Override
    public void openRecordingsDetail(@NonNull Recording recording) {
        hiveRecordingsView.showRecordingDetail(apiaryId, hiveId, recording.getDate());
    }

    @Override
    public void deleteRecording(@NonNull Recording recording) {
        // Show progress indicator
        hiveRecordingsView.setLoadingIndicator(true);
        // Delete recording
        goBeesRepository.deleteRecording(hiveId, recording, new GoBeesDataSource.TaskCallback() {
            @Override
            public void onSuccess() {
                // The hiveRecordingsView may not be able to handle UI updates anymore
                if (!hiveRecordingsView.isActive()) {
                    return;
                }
                // Refresh recordings
                loadData(true);
                // Show success message
                hiveRecordingsView.showSuccessfullyDeletedMessage();
            }

            @Override
            public void onFailure() {
                // The hiveRecordingsView may not be able to handle UI updates anymore
                if (!hiveRecordingsView.isActive()) {
                    return;
                }
                // Hide progress indicator
                hiveRecordingsView.setLoadingIndicator(false);
                // Show error
                hiveRecordingsView.showDeletedErrorMessage();
            }
        });
    }

    @Override
    public void start() {
        int num = ready.incrementAndGet();
        if(num >= 2) {
            loadData(false);
        }
    }
}
