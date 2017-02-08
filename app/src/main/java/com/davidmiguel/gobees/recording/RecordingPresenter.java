/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees.recording;

import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetRecordingCallback;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;

import java.util.Date;

/**
 * Listens to user actions from the UI RecordingFragment, retrieves the data and updates the
 * UI as required.
 */
class RecordingPresenter implements RecordingContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private RecordingContract.View view;

    private long apiaryId;
    private long hiveId;
    private Date start;
    private Date end;
    private Recording recording;

    RecordingPresenter(GoBeesRepository goBeesRepository, RecordingContract.View view,
                       long apiaryId, long hiveId, Date start, Date end) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.view.setPresenter(this);
        this.apiaryId = apiaryId;
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
    public void start() {
        loadData(false);
    }

    @Override
    public void loadData(boolean forceUpdate) {
        loadRecording(start, end);
    }

    /**
     * Load recording from a given period.
     *
     * @param start start date.
     * @param end   end date.
     */
    private void loadRecording(final Date start, Date end) {
        // Set title
        view.showTitle(start);
        // Get recording
        goBeesRepository.getRecording(apiaryId, hiveId, start, end, new GetRecordingCallback() {
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
