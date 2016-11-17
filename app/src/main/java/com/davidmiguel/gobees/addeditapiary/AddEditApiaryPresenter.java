package com.davidmiguel.gobees.addeditapiary;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.source.cache.ApiariesRepository;

/**
 * Listens to user actions from the UI ApiariesFragment, retrieves the data and updates the
 * UI as required.
 */
public class AddEditApiaryPresenter implements AddEditApiaryContract.Presenter {

    @NonNull
    private ApiariesRepository apiariesRepository;
    @NonNull
    private AddEditApiaryContract.View addeditapiaryView;
    private int apiaryId;

    public AddEditApiaryPresenter(@NonNull ApiariesRepository apiariesRepository,
                                  @NonNull AddEditApiaryContract.View addeditapiaryView,
                                  int apiaryId) {
        this.apiariesRepository = apiariesRepository;
        this.addeditapiaryView = addeditapiaryView;
        this.apiaryId = apiaryId;
    }

    @Override
    public void saveApiary(String name, String notes) {

    }

    @Override
    public void populateApiary() {

    }

    @Override
    public void start() {

    }
}
