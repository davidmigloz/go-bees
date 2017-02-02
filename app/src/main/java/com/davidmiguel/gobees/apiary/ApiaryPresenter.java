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

package com.davidmiguel.gobees.apiary;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.addeditapiary.AddEditApiaryActivity;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;

import java.util.Date;

/**
 * Listens to user actions from the UI ApiaryHivesFragment, retrieves the data and updates the
 * UI as required.
 */
class ApiaryPresenter implements ApiaryContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private ApiaryContract.ApiaryHivesView apiaryHivesView;
    private ApiaryContract.ApiaryInfoView apiaryInfoView;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;
    private long apiaryId;
    private Apiary apiary;

    ApiaryPresenter(GoBeesRepository goBeesRepository,
                    ApiaryContract.ApiaryHivesView apiaryHivesView,
                    ApiaryContract.ApiaryInfoView apiaryInfoView, long apiaryId) {
        this.goBeesRepository = goBeesRepository;
        this.apiaryHivesView = apiaryHivesView;
        this.apiaryHivesView.setPresenter(this);
        this.apiaryInfoView = apiaryInfoView;
        apiaryInfoView.setPresenter(this);
        this.apiaryId = apiaryId;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a hive was successfully added, show snackbar
        if (AddEditApiaryActivity.REQUEST_ADD_APIARY == requestCode
                && Activity.RESULT_OK == resultCode) {
            apiaryHivesView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadData(boolean forceUpdate) {
        // Force update the first time
        boolean update = forceUpdate || firstLoad;
        firstLoad = false;
        // Refresh data if needed
        if (update) {
            goBeesRepository.refreshHives(apiaryId);
        }
        // Get apiary
        goBeesRepository.getApiary(apiaryId, new GoBeesDataSource.GetApiaryCallback() {
            @Override
            public void onApiaryLoaded(Apiary apiary) {
                // Save apiary
                ApiaryPresenter.this.apiary = apiary;
                // The view may not be able to handle UI updates anymore
                if (!apiaryHivesView.isActive() && !apiaryInfoView.isActive()) {
                    return;
                }
                // Hide progress indicator
                apiaryHivesView.setLoadingIndicator(false);
                apiaryInfoView.setLoadingIndicator(false);
                // Set apiary name as title
                apiaryHivesView.showTitle(apiary.getName());
                // Process hives
                if (apiary.getHives() == null || apiary.getHives().isEmpty()) {
                    // Show a message indicating there are no hives
                    apiaryHivesView.showNoHives();
                } else {
                    // Show the list of hives
                    apiaryHivesView.showHives(apiary.getHives());
                }
                // Show apiary info
                apiaryInfoView.showInfo(apiary, getApiaryLastRevision());
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!apiaryHivesView.isActive() && !apiaryInfoView.isActive()) {
                    return;
                }
                // Hide progress indicator
                apiaryHivesView.setLoadingIndicator(false);
                apiaryInfoView.setLoadingIndicator(false);
                // Show error
                apiaryHivesView.showLoadingHivesError();
            }
        });
    }

    @Override
    public void addEditHive(long hiveId) {
        apiaryHivesView.showAddEditHive(apiaryId, hiveId);
    }

    @Override
    public void openHiveDetail(@NonNull Hive requestedHive) {
        apiaryHivesView.showHiveDetail(apiaryId, requestedHive.getId());
    }

    @Override
    public void deleteHive(@NonNull Hive hive) {
        // Show progress indicator
        apiaryHivesView.setLoadingIndicator(true);
        // Delete hive
        goBeesRepository.deleteHive(hive.getId(), new GoBeesDataSource.TaskCallback() {
            @Override
            public void onSuccess() {
                // The view may not be able to handle UI updates anymore
                if (!apiaryHivesView.isActive()) {
                    return;
                }
                // Refresh recordings
                loadData(true);
                // Show success message
                apiaryHivesView.showSuccessfullyDeletedMessage();
            }

            @Override
            public void onFailure() {
                // The view may not be able to handle UI updates anymore
                if (!apiaryHivesView.isActive()) {
                    return;
                }
                // Hide progress indicator
                apiaryHivesView.setLoadingIndicator(false);
                // Show error
                apiaryHivesView.showDeletedErrorMessage();
            }
        });
    }

    @Override
    public void onOpenMapClicked() {
        apiaryInfoView.openMap(apiary);
    }

    @Override
    public void start() {
        loadData(false);
    }

    /**
     * Gets apiary last revision.
     *
     * @return last revision date.
     */
    private Date getApiaryLastRevision() {
        return goBeesRepository.getApiaryLastRevision(apiaryId);
    }
}
