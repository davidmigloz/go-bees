package com.davidmiguel.gobees.recording;

import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

import java.util.Date;

/**
 * Listens to user actions from the UI RecordingFragment, retrieves the data and updates the
 * UI as required.
 */
public class RecordingPresenter implements RecordingContract.Presenter {

    public RecordingPresenter(GoBeesRepository goBeesRepository, RecordingContract.View view,
                              long hiveId, Date start, Date end) {

    }

    @Override
    public void deleteRecording() {

    }

    @Override
    public void start() {

    }
}
