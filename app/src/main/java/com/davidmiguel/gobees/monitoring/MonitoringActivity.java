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

package com.davidmiguel.gobees.monitoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;

/**
 * Monitoring activity.
 */
public class MonitoringActivity extends AppCompatActivity {

    public static final int REQUEST_MONITORING = 1;
    public static final int NO_APIARY = -1;
    public static final int NO_HIVE = -1;

    private MonitoringFragment monitoringFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring_act);

        // Get apiary id
        long apiaryId = getIntent().getLongExtra(MonitoringFragment.ARGUMENT_APIARY_ID, NO_APIARY);
        if (apiaryId == NO_APIARY) {
            throw new IllegalArgumentException("No apiary id passed!");
        }

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
            AndroidUtils.addFragmentToActivity(
                    getSupportFragmentManager(), monitoringFragment, R.id.contentFrame);
        }

        // Add monitoringSettingsFragment to the activity
        MonitoringSettingsFragment monitoringSettingsFragment = new MonitoringSettingsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.settingsFrame, monitoringSettingsFragment)
                .commit();

        // Create the presenter
        new MonitoringPresenter(monitoringFragment, monitoringSettingsFragment, apiaryId, hiveId);
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
