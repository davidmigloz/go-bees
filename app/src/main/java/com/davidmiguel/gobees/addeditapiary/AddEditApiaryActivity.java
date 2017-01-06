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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.utils.ActivityUtils;

/**
 * Add / edit apiary activity.
 */
public class AddEditApiaryActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_APIARY = 1;
    public static final int NEW_APIARY = -1;

    private Fragment addEditApiaryFragment;
    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeditapiary_act);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

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
                bundle.putString(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID, apiaryId + "");
                addEditApiaryFragment.setArguments(bundle);
            } else {
                // If new -> set add title
                if (actionBar != null) {
                    actionBar.setTitle(R.string.add_apiary);
                }
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditApiaryFragment, R.id.contentFrame);
        }

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new AddEditApiaryPresenter(goBeesRepository,
                (AddEditApiaryContract.View) addEditApiaryFragment, apiaryId);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (addEditApiaryFragment != null) {
            addEditApiaryFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
