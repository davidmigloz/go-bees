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

package com.davidmiguel.gobees.hive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseActivity;
import com.davidmiguel.gobees.utils.BaseTabFragment;
import com.davidmiguel.gobees.utils.TabsFragmentPagerAdapter;
import com.google.common.collect.Lists;

/**
 * Hive activity.
 */
public class HiveActivity extends BaseActivity {

    public static final int NO_APIARY = -1;
    public static final int NO_HIVE = -1;

    private Fragment hiveRecordingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hive_act);

        // Set up the toolbar
        AndroidUtils.setUpToolbar(this, false);

        // Get apiary id
        long apiaryId = getIntent().getLongExtra(HiveRecordingsFragment.ARGUMENT_APIARY_ID, NO_APIARY);
        if (apiaryId == NO_APIARY) {
            throw new IllegalArgumentException("No apiary id passed!");
        }

        // Get hive id
        long hiveId = getIntent().getLongExtra(HiveRecordingsFragment.ARGUMENT_HIVE_ID, NO_HIVE);
        if (hiveId == NO_HIVE) {
            throw new IllegalArgumentException("No hive id passed!");
        }

        // Create recordings fragment
        hiveRecordingsFragment = HiveRecordingsFragment.newInstance();

        // Create hive info fragment
        HiveInfoFragment hiveInfoFragment = HiveInfoFragment.newInstance();

        // Set up tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabsFragmentPagerAdapter adapter = new TabsFragmentPagerAdapter(
                getSupportFragmentManager(),
                HiveActivity.this,
                Lists.<BaseTabFragment>newArrayList((HiveRecordingsFragment) hiveRecordingsFragment,
                        hiveInfoFragment)
        );
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Create the presenter
        new HivePresenter(goBeesRepository, (HiveContract.HiveRecordingsView)
                hiveRecordingsFragment, hiveInfoFragment, apiaryId, hiveId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (hiveRecordingsFragment != null) {
            hiveRecordingsFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
