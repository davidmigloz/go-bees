package com.davidmiguel.gobees.addeditapiary;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.utils.ActivityUtils;
import com.google.common.base.Strings;

/**
 * Add / edit apiaries activity.
 */
public class AddEditApiaryActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_APIARY = 1;
    public static final int NEW_APIARY = -1;

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
        String id = getIntent().getStringExtra(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID);
        long apiaryId;
        if (Strings.isNullOrEmpty(id)) {
            apiaryId = NEW_APIARY;
        } else {
            apiaryId = Integer.parseInt(id);
        }

        // Add fragment to the activity and set title
        AddEditApiaryFragment addEditApiaryFragment =
                (AddEditApiaryFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
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
        new AddEditApiaryPresenter(goBeesRepository, addEditApiaryFragment, apiaryId);
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
