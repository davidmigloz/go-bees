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
    private Hive hive;

    AddEditHivePresenter(GoBeesRepository goBeesRepository,
                         AddEditHiveContract.View view,
                         long apiaryId, long hiveId) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.apiaryId = apiaryId;
        this.hiveId = hiveId;
        if (isNewHive()) {
            hive = new Hive();
        }
        view.setPresenter(this);
    }

    @Override
    public void save(String name, String notes) {
        if (isNewHive()) {
            createHive(name, notes);
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
        this.hive = hive;
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

    /**
     * Checks whether a hive is new or not.
     *
     * @return true/false.
     */
    private boolean isNewHive() {
        return hiveId == AddEditHiveActivity.NEW_HIVE;
    }

    /**
     * Create an save a new hive.
     *
     * @param name  hive name.
     * @param notes hive notes.
     */
    private void createHive(final String name, final String notes) {
        // Get next id
        goBeesRepository.getNextHiveId(new GetNextHiveIdCallback() {
            @Override
            public void onNextHiveIdLoaded(long hiveId) {
                saveHive(hiveId, name, notes);
            }
        });
    }

    /**
     * Update and save a hive.
     *
     * @param name  hive name
     * @param notes hive notes
     */
    private void updateHive(String name, String notes) {
        if (isNewHive()) {
            throw new RuntimeException("updateHive() was called but hive is new.");
        }
        saveHive(hiveId, name, notes);
    }

    /**
     * aves (or update) the hive.
     *
     * @param hiveId hive id.
     * @param name   hive name.
     * @param notes  hive notes.
     */
    private void saveHive(long hiveId, String name, String notes) {
        // Set id
        hive.setId(hiveId);
        // Set name
        hive.setName(name);
        // Set notes
        hive.setNotes(notes);
        // Save it if it is correct
        if (hive.isValidHive()) {
            goBeesRepository.saveHive(apiaryId, hive, this);
        } else {
            view.showEmptyHiveError();
        }
    }
}
