package com.davidmiguel.gobees.hive;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Recording;

import java.util.Date;
import java.util.List;

/**
 * Dispaly a list of recordings.
 */
public class HiveRecordingsFragment implements HiveContract.View {
    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showRecordings(@NonNull List<Recording> recordings) {

    }

    @Override
    public void startNewRecording() {

    }

    @Override
    public void showRecordingDetail(Date date) {

    }

    @Override
    public void showLoadingRecordingsError() {

    }

    @Override
    public void showNoRecordings() {

    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public void showTitle(@NonNull String title) {

    }

    @Override
    public void setPresenter(@NonNull HiveContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
