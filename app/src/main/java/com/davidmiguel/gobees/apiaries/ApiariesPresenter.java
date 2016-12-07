package com.davidmiguel.gobees.apiaries;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.addeditapiary.AddEditApiaryActivity;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.data.source.local.DataGenerator;

import java.util.List;

/**
 * Listens to user actions from the UI ApiariesFragment, retrieves the data and updates the
 * UI as required.
 */
class ApiariesPresenter implements ApiariesContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private ApiariesContract.View apiariesView;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;

    ApiariesPresenter(GoBeesRepository goBeesRepository, ApiariesContract.View apiariesView) {
        this.goBeesRepository = goBeesRepository;
        this.apiariesView = apiariesView;
        this.apiariesView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a apiary was successfully added, show snackbar
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
            goBeesRepository.refreshApiaries();
        }
        // Get apiaires
        goBeesRepository.getApiaries(new GoBeesDataSource.GetApiariesCallback() {

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
    public void addEditApiary() {
        apiariesView.showAddEditApiary();
    }

    @Override
    public void openApiaryDetail(@NonNull Apiary requestedApiary) {
        apiariesView.showApiaryDetail(requestedApiary.getId());
    }

    // TODO eliminar generar y eliminar datos
    @Override
    public void generateData() {
        DataGenerator dataGenerator = new DataGenerator(goBeesRepository);
        dataGenerator.generateData(3);
        loadApiaries(false);
    }

    @Override
    public void deleteData() {
        goBeesRepository.deleteAll();
        loadApiaries(false);
    }

    @Override
    public void start() {
        loadApiaries(false);
    }
}
