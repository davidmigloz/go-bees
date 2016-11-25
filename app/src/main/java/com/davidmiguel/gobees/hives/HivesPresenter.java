package com.davidmiguel.gobees.hives;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.addeditapiary.AddEditApiaryActivity;
import com.davidmiguel.gobees.apiaries.ApiariesContract;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

import java.util.List;

/**
 * Listens to user actions from the UI HivesFragment, retrieves the data and updates the
 * UI as required.
 */
public class HivesPresenter implements HivesContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private HivesContract.View hivesView;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;
    private long apiaryId;

    public HivesPresenter(GoBeesRepository goBeesRepository, HivesContract.View hivesView,
                          long apiaryId) {
        this.goBeesRepository = goBeesRepository;
        this.hivesView = hivesView;
        this.hivesView.setPresenter(this);
        this.apiaryId = apiaryId;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a hive was successfully added, show snackbar
        if (AddEditApiaryActivity.REQUEST_ADD_APIARY == requestCode && Activity.RESULT_OK == resultCode) {
            hivesView.showSuccessfullySavedMessage();
        }
        // TODO show error message if it fails
    }

    @Override
    public void loadHives(boolean forceUpdate) {
        // Force update the first time
        forceUpdate = forceUpdate || firstLoad;
        firstLoad = false;
        // Show progress indicator
        hivesView.setLoadingIndicator(true);
        // Refresh data if needed
        if (forceUpdate) {
            goBeesRepository.refreshHives(apiaryId);
        }
        // Get apiaires
        goBeesRepository.getHives(apiaryId, new GoBeesDataSource.GetHivesCallback() {
            @Override
            public void onHivesLoaded(List<Hive> hives) {
                // The view may not be able to handle UI updates anymore
                if (!hivesView.isActive()) {
                    return;
                }
                // Hide progress indicator
                hivesView.setLoadingIndicator(false);
                // Process hives
                if (hives.isEmpty()) {
                    // Show a message indicating there are no hives
                    hivesView.showNoHives();
                } else {
                    // Show the list of hives
                    hivesView.showHives(hives);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!hivesView.isActive()) {
                    return;
                }
                hivesView.showLoadingHivesError();
            }
        });
    }

    @Override
    public void addEditHive() {
        hivesView.showAddEditHive();
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
