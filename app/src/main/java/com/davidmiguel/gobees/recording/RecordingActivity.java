package com.davidmiguel.gobees.recording;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.utils.ActivityUtils;

import java.util.Date;

/**
 * Recording detail activity.
 */
public class RecordingActivity extends AppCompatActivity {

    public static final int NO_HIVE = -1;
    public static final int NO_DATE = -1;

    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_act);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Create recording fragment


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
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), recordingFragment, R.id.contentFrame);
        }

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new RecordingPresenter(goBeesRepository, recordingFragment, hiveId,
                new Date(startDate), new Date(endDate));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database
        goBeesRepository.closeDb();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
