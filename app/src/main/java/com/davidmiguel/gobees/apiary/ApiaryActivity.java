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

package com.davidmiguel.gobees.apiary;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseActivity;
import com.davidmiguel.gobees.utils.BaseTabFragment;
import com.davidmiguel.gobees.utils.TabsFragmentPagerAdapter;
import com.google.common.collect.Lists;

/**
 * Apiary activity.
 */
public class ApiaryActivity extends BaseActivity {

    public static final int NO_APIARY = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apiary_act);

        // Set up the toolbar
        AndroidUtils.setUpToolbar(this, false);

        // Get apiary id
        long apiaryId = getIntent().getLongExtra(ApiaryHivesFragment.ARGUMENT_APIARY_ID, NO_APIARY);
        if (apiaryId == NO_APIARY) {
            throw new IllegalArgumentException("No apiary id passed!");
        }

        // Create hives fragment
        ApiaryHivesFragment apiaryHivesFragment = ApiaryHivesFragment.newInstance(apiaryId);

        // Create apiary info fragment
        ApiaryInfoFragment apiaryInfoFragment = ApiaryInfoFragment.newInstance();

        // Set up tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabsFragmentPagerAdapter adapter = new TabsFragmentPagerAdapter(
                getSupportFragmentManager(),
                ApiaryActivity.this,
                Lists.<BaseTabFragment>newArrayList(apiaryHivesFragment, apiaryInfoFragment)
        );
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Create the presenter
        new ApiaryPresenter(goBeesRepository, apiaryHivesFragment, apiaryInfoFragment, apiaryId);
    }
}
