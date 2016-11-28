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
public class AddEditHivePresenter implements AddEditHiveContract.Presenter,
        GetHiveCallback, TaskCallback {

    private GoBeesRepository goBeesRepository;
    private AddEditHiveContract.View addEditHiveView;

    private long hiveId;

    public AddEditHivePresenter(GoBeesRepository goBeesRepository,
                                AddEditHiveContract.View addEditHiveView,
                                  long hiveId) {
        this.goBeesRepository = goBeesRepository;
        this.addEditHiveView = addEditHiveView;
        this.hiveId = hiveId;
        addEditHiveView.setPresenter(this);
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
        if (addEditHiveView.isActive()) {
            addEditHiveView.setName(hive.getName());
            addEditHiveView.setNotes(hive.getNotes());
        }
    }

    @Override
    public void onDataNotAvailable() {
        // Show error message
        if (addEditHiveView.isActive()) {
            addEditHiveView.showEmptyHiveError();
        }
    }

    @Override
    public void onSuccess() {
        // Apiary saved successfully -> go back to apiaries activity
        addEditHiveView.showHivesList();
    }

    @Override
    public void onFailure() {
        // Error saving apiaries
        addEditHiveView.showSaveHiveError();
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
                    goBeesRepository.saveHive(newHive, listener);
                } else {
                    addEditHiveView.showEmptyHiveError();
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
        goBeesRepository.saveHive(editedHive, this);
    }
}
