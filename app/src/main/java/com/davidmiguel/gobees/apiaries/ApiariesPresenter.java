package com.davidmiguel.gobees.apiaries;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.addeditapiary.AddEditApiaryActivity;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.ApiariesDataSource;
import com.davidmiguel.gobees.data.source.cache.ApiariesRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens to user actions from the UI AddEditApiaryFragment, retrieves the data and updates the
 * UI as required.
 */
public class ApiariesPresenter implements ApiariesContract.Presenter {

    private ApiariesRepository apiariesRepository;
    private ApiariesContract.View apiariesView;

    private boolean firstLoad = true;

    public ApiariesPresenter(ApiariesRepository apiariesRepository, ApiariesContract.View apiariesView) {
        this.apiariesRepository = apiariesRepository;
        this.apiariesView = apiariesView;
        this.apiariesView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (AddEditApiaryActivity.REQUEST_ADD_APIARY == requestCode && Activity.RESULT_OK == resultCode) {
            apiariesView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadApiaries(boolean forceUpdate) {
        // Force update the first time
        forceUpdate = forceUpdate || firstLoad;
        firstLoad = false;
        // Show progress indicator
        apiariesView.setLoadingIndicator(true);
        // Refresh data if needed
        if (forceUpdate) {
            apiariesRepository.refreshApiaries();
        }
        // Get apiaires
        apiariesRepository.getApiaries(new ApiariesDataSource.GetApiariesCallback() {

            @Override
            public void onApiariesLoaded(List<Apiary> apiaries) {
                // The view may not be able to handle UI updates anymore
                if (!apiariesView.isActive()) {
                    return;
                }
                // Hide progress indicator
                apiariesView.setLoadingIndicator(false);
                // Process apiaries
                if (apiaries.isEmpty()) {
                    // Show a message indicating there are no apiaries
                    apiariesView.showNoApiaries();
                } else {
                    // Show the list of apiaries
                    apiariesView.showApiaries(apiaries);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!apiariesView.isActive()) {
                    return;
                }
                apiariesView.showLoadingApiariesError();
            }
        });
    }

    @Override
    public void addNewApiary() {
        apiariesView.showAddApiary();
    }

    @Override
    public void openApiaryDetail(@NonNull Apiary requestedApiary) {
        // TODO
    }

    @Override
    public void start() {
        loadApiaries(false);
    }
}
