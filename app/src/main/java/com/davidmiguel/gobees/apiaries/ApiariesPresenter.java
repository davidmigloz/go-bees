package com.davidmiguel.gobees.apiaries;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.cache.ApiariesRepository;

/**
 * Listens to user actions from the UI ApiariesFragment, retrieves the data and updates the
 * UI as required.
 */
public class ApiariesPresenter implements ApiariesContract.Presenter {

    private ApiariesRepository apiariesRepository;
    private ApiariesContract.View apiariesView;

    public ApiariesPresenter(ApiariesRepository apiariesRepository, ApiariesContract.View apiariesView) {
        this.apiariesRepository = apiariesRepository;
        this.apiariesView = apiariesView;
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadApiaries(boolean forceUpdate) {

    }

    @Override
    public void addNewApiary() {

    }

    @Override
    public void openApiaryDetail(@NonNull Apiary requestedApiary) {

    }

    @Override
    public void start() {

    }
}
