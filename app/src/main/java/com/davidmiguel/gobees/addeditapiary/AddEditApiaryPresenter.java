package com.davidmiguel.gobees.addeditapiary;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.ApiariesDataSource;
import com.davidmiguel.gobees.data.source.cache.ApiariesRepository;

import java.util.Random;

/**
 * Listens to user actions from the UI ApiariesFragment, retrieves the data and updates the
 * UI as required.
 */
public class AddEditApiaryPresenter implements AddEditApiaryContract.Presenter,
        ApiariesDataSource.GetApiaryCallback, ApiariesDataSource.TaskCallback {

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

        addeditapiaryView.setPresenter(this);
    }

    @Override
    public void saveApiary(String name, String notes) {
        if (isNewApiary()) {
            createApiary(name, notes);
        } else {
            updateApiary(name, notes);
        }
    }

    @Override
    public void populateApiary() {
        if (isNewApiary()) {
            throw new RuntimeException("populateApiary() was called but apiary is new.");
        }
        apiariesRepository.getApiary(apiaryId, this);
    }

    @Override
    public void start() {
        if (!isNewApiary()) {
            populateApiary();
        }
    }

    @Override
    public void onApiaryLoaded(Apiary apiary) {
        if (addeditapiaryView.isActive()) {
            addeditapiaryView.setName(apiary.getName());
            addeditapiaryView.setNotes(apiary.getNotes());
        }
    }

    @Override
    public void onDataNotAvailable() {
        if (addeditapiaryView.isActive()) {
            addeditapiaryView.showEmptyApiaryError();
        }
    }

    @Override
    public void onSuccess() {
        addeditapiaryView.showApiariesList();
    }

    @Override
    public void onFailure() {
        addeditapiaryView.showSaveApiaryError();
    }

    private boolean isNewApiary() {
        return apiaryId == -1;
    }

    private void createApiary(String name, String notes) {
        Random r = new Random();
        Apiary newApiary = new Apiary(r.nextInt(), name, null, null, notes);
        if (newApiary.hasNoName()) {
            addeditapiaryView.showEmptyApiaryError();
        } else {
            apiariesRepository.saveApiary(newApiary, new ApiariesDataSource.TaskCallback() {
                @Override
                public void onSuccess() {
                    addeditapiaryView.showApiariesList();
                }

                @Override
                public void onFailure() {

                }
            });

        }
    }

    private void updateApiary(String name, String notes) {
        if (isNewApiary()) {
            throw new RuntimeException("updateApiary() was called but apiary is new.");
        }
        Apiary editedApiary = new Apiary(apiaryId, name, null, null, notes);
        apiariesRepository.saveApiary(editedApiary, this);
    }
}
