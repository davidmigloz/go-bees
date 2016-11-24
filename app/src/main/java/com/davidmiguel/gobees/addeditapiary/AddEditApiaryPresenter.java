package com.davidmiguel.gobees.addeditapiary;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetApiaryCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.TaskCallback;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

/**
 * Listens to user actions from the UI ApiariesFragment, retrieves the data and updates the
 * UI as required.
 */
public class AddEditApiaryPresenter implements AddEditApiaryContract.Presenter,
        GetApiaryCallback, TaskCallback {

    private GoBeesRepository apiariesRepository;
    private AddEditApiaryContract.View addeditapiaryView;

    private int apiaryId;

    public AddEditApiaryPresenter(GoBeesRepository apiariesRepository,
                                  AddEditApiaryContract.View addeditapiaryView,
                                  int apiaryId) {
        this.apiariesRepository = apiariesRepository;
        this.addeditapiaryView = addeditapiaryView;
        this.apiaryId = apiaryId;
        addeditapiaryView.setPresenter(this);
    }

    @Override
    public void saveApiary(String name, String notes) {
        if (isNewApiary()) {
            createApiary(name, notes, this);
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
        // Show apiary data on view
        if (addeditapiaryView.isActive()) {
            addeditapiaryView.setName(apiary.getName());
            addeditapiaryView.setNotes(apiary.getNotes());
        }
    }

    @Override
    public void onDataNotAvailable() {
        // Show error message
        if (addeditapiaryView.isActive()) {
            addeditapiaryView.showEmptyApiaryError();
        }
    }

    @Override
    public void onSuccess() {
        // Apiary saved successfully -> go back to apiaries activity
        addeditapiaryView.showApiariesList();
    }

    @Override
    public void onFailure() {
        // Error saving apiaries
        addeditapiaryView.showSaveApiaryError();
    }

    private boolean isNewApiary() {
        return apiaryId == -1;
    }

    private void createApiary(final String name, final String notes, final TaskCallback listener) {
        // Get next id
        apiariesRepository.getNextApiaryId(new GoBeesDataSource.GetNextApiaryIdCallback() {
            @Override
            public void onNextApiaryIdLoaded(long apiaryId) {
                // Create apiary
                Apiary newApiary =
                        new Apiary(apiaryId, name, null, null, null, notes, null, null, null);
                // Save it if it is correct
                if (newApiary.isValidApiary()) {
                    addeditapiaryView.showEmptyApiaryError();
                } else {
                    apiariesRepository.saveApiary(newApiary, listener);
                }
            }
        });
    }

    private void updateApiary(String name, String notes) {
        if (isNewApiary()) {
            throw new RuntimeException("updateApiary() was called but apiary is new.");
        }
        // Create new apiary with the modifications
        Apiary editedApiary =
                new Apiary(apiaryId, name, null, null, null, notes, null, null, null);
        apiariesRepository.saveApiary(editedApiary, this);
    }
}
