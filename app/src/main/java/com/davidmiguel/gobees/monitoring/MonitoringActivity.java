package com.davidmiguel.gobees.monitoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.utils.ActivityUtils;

/**
 * Monitoring activity.
 */
public class MonitoringActivity extends AppCompatActivity {

    public static final int REQUEST_RECORD_HIVE = 1;
    public static final int NO_HIVE = -1;

    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring_act);

        // Get hive id
        long hiveId = getIntent().getLongExtra(MonitoringFragment.ARGUMENT_HIVE_ID, NO_HIVE);
        if (hiveId == NO_HIVE) {
            throw new IllegalArgumentException("No hive id passed!");
        }

        // Add fragment to the activity
        MonitoringFragment monitoringFragment =
                (MonitoringFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (monitoringFragment == null) {
            // Create the fragment
            monitoringFragment = MonitoringFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), monitoringFragment, R.id.contentFrame);
        }

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new MonitoringPresenter(goBeesRepository, monitoringFragment, hiveId);
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
