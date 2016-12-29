package com.davidmiguel.gobees.addedithive;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetHiveCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.TaskCallback;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetNextHiveIdCallback;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

/**
 * Listens to user actions from the UI AddEditHiveFragment, retrieves the data and updates the
 * UI as required.
 */
class AddEditHivePresenter implements AddEditHiveContract.Presenter,
        GetHiveCallback, TaskCallback {

    private GoBeesRepository goBeesRepository;
    private AddEditHiveContract.View view;

    private long apiaryId;
    private long hiveId;

    AddEditHivePresenter(GoBeesRepository goBeesRepository,
                         AddEditHiveContract.View view,
                         long apiaryId, long hiveId) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.apiaryId = apiaryId;
        this.hiveId = hiveId;
        view.setPresenter(this);
    }

    @Override
    public void saveHive(String name, String notes) {
        if (isNewHive()) {
            createHive(name, notes, this);
        } else {
            updateHive(name, notes);
        }
    }

    @Override
    public void populateHive() {
        if (isNewHive()) {
            throw new RuntimeException("populateHive() was called but hive is new.");
        }
        goBeesRepository.getHive(hiveId, this);
    }

    @Override
    public void start() {
        if (!isNewHive()) {
            populateHive();
        }
    }

    @Override
    public void onHiveLoaded(Hive hive) {
        // Show hive data on view
        if (view.isActive()) {
            view.setName(hive.getName());
            view.setNotes(hive.getNotes());
        }
    }

    @Override
    public void onDataNotAvailable() {
        // Show error message
        if (view.isActive()) {
            view.showEmptyHiveError();
        }
    }

    @Override
    public void onSuccess() {
        // Apiary saved successfully -> go back to apiaries activity
        view.showHivesList();
    }

    @Override
    public void onFailure() {
        // Error saving apiaries
        view.showSaveHiveError();
    }

    private boolean isNewHive() {
        return hiveId == AddEditHiveActivity.NEW_HIVE;
    }

    private void createHive(final String name, final String notes, final TaskCallback listener) {
        // Get next id
        goBeesRepository.getNextHiveId(new GetNextHiveIdCallback() {
            @Override
            public void onNextHiveIdLoaded(long hiveId) {
                // Create hive
                Hive newHive = new Hive(hiveId, name, null, notes, null);
                // Save it if it is correct
                if (newHive.isValidHive()) {
                    goBeesRepository.saveHive(apiaryId, newHive, listener);
                } else {
                    view.showEmptyHiveError();
                }
            }
        });
    }

    private void updateHive(String name, String notes) {
        if (isNewHive()) {
            throw new RuntimeException("updateHive() was called but hive is new.");
        }
        // Create new hive with the modifications
        Hive editedHive = new Hive(hiveId, name, null, notes, null);
        goBeesRepository.saveHive(apiaryId, editedHive, this);
    }
}
