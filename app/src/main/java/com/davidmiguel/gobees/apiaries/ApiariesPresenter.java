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

package com.davidmiguel.gobees.apiaries;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.addeditapiary.AddEditApiaryActivity;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Listens to user actions from the UI ApiariesFragment, retrieves the data and updates the
 * UI as required.
 */
class ApiariesPresenter implements ApiariesContract.LoadDataPresenter {

    private static final int MIN_UPDATE_WEATHER = 15;

    private GoBeesRepository goBeesRepository;
    private ApiariesContract.View view;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;

    ApiariesPresenter(GoBeesRepository goBeesRepository, ApiariesContract.View view) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a apiary was successfully added, show snackbar
        if (AddEditApiaryActivity.REQUEST_ADD_APIARY == requestCode
                && Activity.RESULT_OK == resultCode) {
            view.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadData(final boolean forceUpdate) {
        // Force update the first time
        final boolean update = forceUpdate || firstLoad;
        firstLoad = false;
        // Refresh data if needed
        if (update) {
            goBeesRepository.refreshApiaries();
        }
        // Get apiaries
        goBeesRepository.getApiaries(new GoBeesDataSource.GetApiariesCallback() {
            @Override
            public void onApiariesLoaded(List<Apiary> apiaries) {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Hide progress indicator
                view.setLoadingIndicator(false);
                // Process apiaries
                if (apiaries.isEmpty()) {
                    view.showNoApiaries();
                } else {
                    // Show the list of apiaries
                    view.showApiaries(apiaries);
                    // Check whether current weather is up to date
                    checkCurrentWeather(forceUpdate, apiaries);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                view.showLoadingApiariesError();
            }
        });
    }

    @Override
    public void addEditApiary(long apiaryId) {
        view.showAddEditApiary(apiaryId);
    }

    @Override
    public void openApiaryDetail(@NonNull Apiary requestedApiary) {
        view.showApiaryDetail(requestedApiary.getId());
    }

    @Override
    public void deleteApiary(@NonNull Apiary apiary) {
        // Show progress indicator
        view.setLoadingIndicator(true);
        // Delete apiary
        goBeesRepository.deleteApiary(apiary.getId(), new GoBeesDataSource.TaskCallback() {
            @Override
            public void onSuccess() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Refresh recordings
                loadData(true);
                // Show success message
                view.showSuccessfullyDeletedMessage();
            }

            @Override
            public void onFailure() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Hide progress indicator
                view.setLoadingIndicator(false);
                // Show error
                view.showDeletedErrorMessage();
            }
        });
    }

    @Override
    public void start() {
        loadData(false);
    }

    /**
     * Checks whether current weather is up to date (less than 15min old).
     * If not, orders to update the current weather in all apiaries.
     *
     * @param forceUpdate force to update weather.
     * @param apiaries list of apiaries.
     */
    private void checkCurrentWeather(boolean forceUpdate, List<Apiary> apiaries) {
        List<Apiary> apiariesToUpdate = new ArrayList<>();
        Date now = new Date();
        // Check dates
        for (Apiary apiary : apiaries) {
            if (apiary.hasLocation()) {
                if (!forceUpdate && apiary.getCurrentWeather() != null) {
                    // Check time
                    Date weatherDate = apiary.getCurrentWeather().getTimestamp();
                    long minFromLastUpdate = (now.getTime() - weatherDate.getTime()) / 60000;
                    if (minFromLastUpdate >= MIN_UPDATE_WEATHER) {
                        apiariesToUpdate.add(apiary);
                    }
                } else {
                    apiariesToUpdate.add(apiary);
                }
            }
        }
        // Update weather if needed
        if (!apiariesToUpdate.isEmpty()) {
            goBeesRepository.updateApiariesCurrentWeather(apiariesToUpdate,
                    new GoBeesDataSource.TaskCallback() {
                        @Override
                        public void onSuccess() {
                            view.notifyApiariesUpdated();
                        }

                        @Override
                        public void onFailure() {
                            view.showWeatherUpdateErrorMessage();
                        }
                    });
        }
    }
}
