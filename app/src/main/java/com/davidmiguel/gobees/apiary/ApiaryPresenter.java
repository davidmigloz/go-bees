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
class ApiaryPresenter implements ApiaryContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private ApiaryContract.View view;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;
    private long apiaryId;

    ApiaryPresenter(GoBeesRepository goBeesRepository, ApiaryContract.View view,
                    long apiaryId) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.view.setPresenter(this);
        this.apiaryId = apiaryId;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a hive was successfully added, show snackbar
        if (AddEditApiaryActivity.REQUEST_ADD_APIARY == requestCode && Activity.RESULT_OK == resultCode) {
            view.showSuccessfullySavedMessage();
        }
        // TODO show error message if it fails
    }

    @Override
    public void loadHives(boolean forceUpdate) {
        // Force update the first time
        forceUpdate = forceUpdate || firstLoad;
        firstLoad = false;
        // Show progress indicator
        view.setLoadingIndicator(true);
        // Refresh data if needed
        if (forceUpdate) {
            goBeesRepository.refreshHives(apiaryId);
        }
        // Get apiary
        goBeesRepository.getApiary(apiaryId, new GoBeesDataSource.GetApiaryCallback() {
            @Override
            public void onApiaryLoaded(Apiary apiary) {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Hide progress indicator
                view.setLoadingIndicator(false);
                // Set apiary name as title
                view.showTitle(apiary.getName());
                // Process hives
                if (apiary.getHives() == null || apiary.getHives().isEmpty()) {
                    // Show a message indicating there are no hives
                    view.showNoHives();
                } else {
                    // Show the list of hives
                    view.showHives(apiary.getHives());
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                view.showLoadingHivesError();
            }
        });
    }

    @Override
    public void addEditHive(long hiveId) {
        view.showAddEditHive(apiaryId, hiveId);
    }

    @Override
    public void openHiveDetail(@NonNull Hive requestedHive) {
        view.showHiveDetail(requestedHive.getId());
    }

    @Override
    public void deleteHive(@NonNull Hive hive) {
        // Show progress indicator
        view.setLoadingIndicator(true);
        // Delete hive
        goBeesRepository.deleteHive(hive.getId(), new GoBeesDataSource.TaskCallback() {
            @Override
            public void onSuccess() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Refresh recordings
                loadHives(true);
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
        loadHives(false);
    }
}
