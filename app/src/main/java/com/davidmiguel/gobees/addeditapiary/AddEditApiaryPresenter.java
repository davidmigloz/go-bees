package com.davidmiguel.gobees.addeditapiary;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetApiaryCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.TaskCallback;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

/**
 * Listens to user actions from the UI AddEditApiaryFragment, retrieves the data and updates the
 * UI as required.
 */
public class AddEditApiaryPresenter implements AddEditApiaryContract.Presenter,
        GetApiaryCallback, TaskCallback {

    private GoBeesRepository goBeesRepository;
    private AddEditApiaryContract.View addeditapiaryView;

    private long apiaryId;

    public AddEditApiaryPresenter(GoBeesRepository goBeesRepository,
                                  AddEditApiaryContract.View addeditapiaryView,
                                  long apiaryId) {
        this.goBeesRepository = goBeesRepository;
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
        goBeesRepository.getApiary(apiaryId, this);
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
        return apiaryId == AddEditApiaryActivity.NEW_APIARY;
    }

    private void createApiary(final String name, final String notes, final TaskCallback listener) {
        // Get next id
        goBeesRepository.getNextApiaryId(new GoBeesDataSource.GetNextApiaryIdCallback() {
            @Override
            public void onNextApiaryIdLoaded(long apiaryId) {
                // Create apiary
                Apiary newApiary =
                        new Apiary(apiaryId, name, null, null, null, notes, null, null, null);
                // Save it if it is correct
                if (newApiary.isValidApiary()) {
                    goBeesRepository.saveApiary(newApiary, listener);
                } else {
                    addeditapiaryView.showEmptyApiaryError();
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
        goBeesRepository.saveApiary(editedApiary, this);
    }
}
