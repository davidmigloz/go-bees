package com.davidmiguel.gobees.addedithive;

import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

/**
 * Listens to user actions from the UI AddEditHiveFragment, retrieves the data and updates the
 * UI as required.
 */
public class AddEditHivePresenter implements AddEditHiveContract.Presenter {

    public AddEditHivePresenter(GoBeesRepository goBeesRepository,
                                AddEditHiveContract.View addEditHiveView,
                                  long apiaryId) {

    }

    @Override
    public void saveHive(String name, String notes) {

    }

    @Override
    public void populateHive() {

    }

    @Override
    public void start() {

    }
}
