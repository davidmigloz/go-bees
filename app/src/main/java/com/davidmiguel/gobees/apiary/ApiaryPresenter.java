package com.davidmiguel.gobees.apiary;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.addeditapiary.AddEditApiaryActivity;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

/**
 * Listens to user actions from the UI ApiaryHivesFragment, retrieves the data and updates the
 * UI as required.
 */
public class ApiaryPresenter implements ApiaryContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private ApiaryContract.View apiaryView;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;
    private long apiaryId;

    public ApiaryPresenter(GoBeesRepository goBeesRepository, ApiaryContract.View apiaryView,
                           long apiaryId) {
        this.goBeesRepository = goBeesRepository;
        this.apiaryView = apiaryView;
        this.apiaryView.setPresenter(this);
        this.apiaryId = apiaryId;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a hive was successfully added, show snackbar
        if (AddEditApiaryActivity.REQUEST_ADD_APIARY == requestCode && Activity.RESULT_OK == resultCode) {
            apiaryView.showSuccessfullySavedMessage();
        }
        // TODO show error message if it fails
    }

    @Override
    public void loadHives(boolean forceUpdate) {
        // Force update the first time
        forceUpdate = forceUpdate || firstLoad;
        firstLoad = false;
        // Show progress indicator
        apiaryView.setLoadingIndicator(true);
        // Refresh data if needed
        if (forceUpdate) {
            goBeesRepository.refreshHives(apiaryId);
        }
        // Get apiary
        goBeesRepository.getApiary(apiaryId, new GoBeesDataSource.GetApiaryCallback() {
            @Override
            public void onApiaryLoaded(Apiary apiary) {
                // The view may not be able to handle UI updates anymore
                if (!apiaryView.isActive()) {
                    return;
                }
                // Hide progress indicator
                apiaryView.setLoadingIndicator(false);
                // Process hives
                if (apiary.getHives() == null || apiary.getHives().isEmpty()) {
                    // Show a message indicating there are no hives
                    apiaryView.showNoHives();
                } else {
                    // Set apiary name as title
                    apiaryView.showTitle(apiary.getName());
                    // Show the list of hives
                    apiaryView.showHives(apiary.getHives());
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!apiaryView.isActive()) {
                    return;
                }
                apiaryView.showLoadingHivesError();
            }
        });
    }

    @Override
    public void addEditHive() {
        apiaryView.showAddEditHive();
    }

    @Override
    public void openHiveDetail(@NonNull Hive requestedHive) {
        // TODO
    }

    @Override
    public void start() {
        loadHives(false);
    }
}
