package com.davidmiguel.gobees.recording;

import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetRecordingCallback;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

import java.util.Date;

/**
 * Listens to user actions from the UI RecordingFragment, retrieves the data and updates the
 * UI as required.
 */
class RecordingPresenter implements RecordingContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private RecordingContract.View view;

    private long hiveId;
    private Date start;
    private Date end;
    private Recording recording;

    RecordingPresenter(GoBeesRepository goBeesRepository, RecordingContract.View view,
                       long hiveId, Date start, Date end) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.view.setPresenter(this);
        this.hiveId = hiveId;
        this.start = start;
        this.end = end;
    }

    @Override
    public void openTempChart() {
        view.showTempChart();
    }

    @Override
    public void openRainChart() {
        view.showRainChart();
    }

    @Override
    public void openWindChart() {
        view.showWindChart();
    }

    @Override
    public void deleteRecording() {
        // TODO
    }

    @Override
    public void start() {
        loadRecording(start, end);
    }

    private void loadRecording(final Date start, Date end) {
        // Set title
        view.showTitle(start);
        // Show loading indicator
        view.setLoadingIndicator(true);
        // Get recording
        goBeesRepository.getRecording(hiveId, start, end, new GetRecordingCallback() {
            @Override
            public void onRecordingLoaded(Recording r) {
                recording = r;
                // Hide loading indicator
                view.setLoadingIndicator(false);
                if (!recording.getRecords().isEmpty()) {
                    // Show data
                    view.showRecording(recording);
                } else {
                    // No records
                    view.showNoRecords();
                }
            }

            @Override
            public void onDataNotAvailable() {
                // Hide loading indicator
                view.setLoadingIndicator(false);
                // Show error message
                view.showLoadingRecordingError();
            }
        });
    }
}
