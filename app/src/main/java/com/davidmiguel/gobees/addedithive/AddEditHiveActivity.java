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

package com.davidmiguel.gobees.addedithive;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseActivity;

/**
 * Add / edit hive activity.
 */
public class AddEditHiveActivity extends BaseActivity {

    public static final int REQUEST_ADD_HIVE = 1;
    public static final int NO_APIARY = -1;
    public static final int NEW_HIVE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addedithive_act);

        // Set up the toolbar
        ActionBar actionBar = AndroidUtils.setUpToolbar(this, false);

        // Get apiary
        long apiaryId = getIntent()
                .getLongExtra(AddEditHiveFragment.ARGUMENT_EDIT_APIARY_ID, NO_APIARY);
        if (apiaryId == NO_APIARY) {
            throw new IllegalArgumentException("No apiary id passed!");
        }

        // Get hive id (if edit)
        long hiveId = getIntent()
                .getLongExtra(AddEditHiveFragment.ARGUMENT_EDIT_HIVE_ID, NEW_HIVE);


        // Add fragment to the activity and set title
        AddEditHiveFragment addEditHiveFragment =
                (AddEditHiveFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (addEditHiveFragment == null) {
            addEditHiveFragment = AddEditHiveFragment.newInstance();
            if (getIntent().hasExtra(AddEditHiveFragment.ARGUMENT_EDIT_HIVE_ID)) {
                // If edit -> set edit title
                if (actionBar != null) {
                    actionBar.setTitle(R.string.edit_hive);
                }
                Bundle bundle = new Bundle();
                bundle.putString(AddEditHiveFragment.ARGUMENT_EDIT_HIVE_ID, Long.toString(hiveId));
                addEditHiveFragment.setArguments(bundle);
            } else {
                // If new -> set add title
                if (actionBar != null) {
                    actionBar.setTitle(R.string.add_hive);
                }
            }
            AndroidUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditHiveFragment, R.id.contentFrame);
        }

        // Create the presenter
        new AddEditHivePresenter(goBeesRepository, addEditHiveFragment, apiaryId, hiveId);
    }
}
