package com.davidmiguel.gobees.hive;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

import java.util.Date;

/**
 * Listens to user actions from the UI HiveRecordingsFragment, retrieves the data and updates the
 * UI as required.
 */
public class HivePresenter implements HiveContract.Presenter {

    public HivePresenter(GoBeesRepository goBeesRepository, HiveContract.View hiveView,
                         long hiveId) {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadRecordings(boolean forceUpdate) {

    }

    @Override
    public void startNewRecording() {

    }

    @Override
    public void openRecordingsDetail(@NonNull Date date) {

    }

    @Override
    public void start() {

    }
}
