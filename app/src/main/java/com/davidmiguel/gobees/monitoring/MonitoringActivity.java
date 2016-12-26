package com.davidmiguel.gobees.monitoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.ActivityUtils;

/**
 * Monitoring activity.
 */
public class MonitoringActivity extends AppCompatActivity {

    public static final int NO_HIVE = -1;

    private MonitoringFragment monitoringFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring_act);

        // Get hive id
        long hiveId = getIntent().getLongExtra(MonitoringFragment.ARGUMENT_HIVE_ID, NO_HIVE);
        if (hiveId == NO_HIVE) {
            throw new IllegalArgumentException("No hive id passed!");
        }

        // Add monitoringFragment to the activity
        monitoringFragment = (MonitoringFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (monitoringFragment == null) {
            // Create the fragment
            monitoringFragment = MonitoringFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), monitoringFragment, R.id.contentFrame);
        }

        // Add monitoringSettingsFragment to the activity
        MonitoringSettingsFragment monitoringSettingsFragment = new MonitoringSettingsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.settingsFrame, monitoringSettingsFragment)
                .commit();

        // Create the presenter
        new MonitoringPresenter(monitoringFragment, monitoringSettingsFragment, hiveId);
    }

    @Override
    public void onBackPressed() {
        if (monitoringFragment != null) {
            boolean defaultAction = monitoringFragment.onBackPressed();
            if (!defaultAction) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
