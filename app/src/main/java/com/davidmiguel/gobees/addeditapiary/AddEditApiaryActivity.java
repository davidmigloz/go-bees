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

package com.davidmiguel.gobees.addeditapiary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseActivity;

/**
 * Add / edit apiary activity.
 */
public class AddEditApiaryActivity extends BaseActivity {

    public static final int REQUEST_ADD_APIARY = 1;
    public static final int NEW_APIARY = -1;

    private Fragment addEditApiaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeditapiary_act);

        // Set up the toolbar
        ActionBar actionBar = AndroidUtils.setUpToolbar(this, false);

        // Get apiary id (if edit)
        long apiaryId = getIntent()
                .getLongExtra(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID, NEW_APIARY);

        // Add fragment to the activity and set title
        addEditApiaryFragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (addEditApiaryFragment == null) {
            addEditApiaryFragment = AddEditApiaryFragment.newInstance();
            if (getIntent().hasExtra(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID)) {
                // If edit -> set edit title
                if (actionBar != null) {
                    actionBar.setTitle(R.string.edit_apiary);
                }
                Bundle bundle = new Bundle();
                bundle.putString(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID,
                        Long.toString(apiaryId));
                addEditApiaryFragment.setArguments(bundle);
            } else {
                // If new -> set add title
                if (actionBar != null) {
                    actionBar.setTitle(R.string.add_apiary);
                }
            }
            AndroidUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditApiaryFragment, R.id.contentFrame);
        }

        // Create the presenter
        new AddEditApiaryPresenter(goBeesRepository,
                (AddEditApiaryContract.View) addEditApiaryFragment, apiaryId,
                new LocationService(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (addEditApiaryFragment != null) {
            addEditApiaryFragment.onRequestPermissionsResult(requestCode,
                    permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
