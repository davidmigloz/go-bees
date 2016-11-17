package com.davidmiguel.gobees.addeditapiary;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.ActivityUtils;

/**
 * Add / edit apiaries activity.
 */
public class AddEditApiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeditapiary_act);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Get apiary id (if edit)
        int apiaryId = Integer.parseInt(getIntent()
                .getStringExtra(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID));

        // Add fragment to the activity and set title
        AddEditApiaryFragment addEditApiaryFragment =
                (AddEditApiaryFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (addEditApiaryFragment == null) {
            addEditApiaryFragment = AddEditApiaryFragment.newInstance();
            if (getIntent().hasExtra(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID)) {
                // If edit -> set edit title
                actionBar.setTitle(R.string.edit_apiary);
                Bundle bundle = new Bundle();
                bundle.putString(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID, apiaryId + "");
                addEditApiaryFragment.setArguments(bundle);
            } else {
                // If new -> set add title
                actionBar.setTitle(R.string.add_apiary);
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditApiaryFragment, R.id.contentFrame);
        }

        // Create the presenter
        new AddEditApiaryPresenter(
                Injection.provideApiariesRepository(getApplicationContext()),
                addEditApiaryFragment, apiaryId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
