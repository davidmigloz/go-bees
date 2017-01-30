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

import android.os.Bundle;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseActivity;

import java.util.Date;

/**
 * Recording detail activity.
 */
public class RecordingActivity extends BaseActivity {

    public static final int NO_APIARY = -1;
    public static final int NO_HIVE = -1;
    public static final int NO_DATE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_act);

        // Set up the toolbar
        AndroidUtils.setUpToolbar(this, false);

        // Get apiary id
        long apiaryId = getIntent().getLongExtra(RecordingFragment.ARGUMENT_APIARY_ID, NO_APIARY);
        if (apiaryId == NO_APIARY) {
            throw new IllegalArgumentException("No apiary id passed!");
        }

        // Get hive id
        long hiveId = getIntent().getLongExtra(RecordingFragment.ARGUMENT_HIVE_ID, NO_HIVE);
        if (hiveId == NO_HIVE) {
            throw new IllegalArgumentException("No hive id passed!");
        }

        // Get start and end dates
        long startDate = getIntent().getLongExtra(RecordingFragment.ARGUMENT_START_DATE, NO_DATE);
        if (startDate == NO_DATE) {
            throw new IllegalArgumentException("No start date id passed!");
        }
        long endDate = getIntent().getLongExtra(RecordingFragment.ARGUMENT_END_DATE, NO_DATE);
        if (endDate == NO_DATE) {
            throw new IllegalArgumentException("No end date id passed!");
        }

        // Add fragment to the activity
        RecordingFragment recordingFragment =
                (RecordingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (recordingFragment == null) {
            // Create the fragment
            recordingFragment = RecordingFragment.newInstance();
            AndroidUtils.addFragmentToActivity(
                    getSupportFragmentManager(), recordingFragment, R.id.contentFrame);
        }

        // Create the presenter
        new RecordingPresenter(goBeesRepository, recordingFragment, apiaryId, hiveId,
                new Date(startDate), new Date(endDate));
    }
}
