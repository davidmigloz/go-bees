package com.davidmiguel.gobees.hives;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

/**
 * Listens to user actions from the UI HivesFragment, retrieves the data and updates the
 * UI as required.
 */
public class HivesPresenter implements HivesContract.Presenter {

    public HivesPresenter(GoBeesRepository goBeesRepository, HivesContract.View hivesView) {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadHives(int apiaryId, boolean forceUpdate) {

    }

    @Override
    public void addEditHive() {

    }

    @Override
    public void openHiveDetail(@NonNull Hive requestedHive) {

    }

    @Override
    public void start() {

    }
}
