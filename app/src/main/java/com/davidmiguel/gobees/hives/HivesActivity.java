package com.davidmiguel.gobees.hives;

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
 * Hives activity.
 */
public class HivesActivity extends AppCompatActivity {

    public static final int NO_APIARY = -1;

    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hives_act);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.hives);
        }

        // Get apiary id
        long apiaryId = getIntent().getLongExtra(HivesFragment.ARGUMENT_APIARY_ID, NO_APIARY);
        if (apiaryId == NO_APIARY) {
            throw new IllegalArgumentException("No apiary id passed!");
        }

        // Add fragment to the activity
        HivesFragment hivesFragment =
                (HivesFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (hivesFragment == null) {
            // Create the fragment
            hivesFragment = HivesFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(HivesFragment.ARGUMENT_APIARY_ID, apiaryId + "");
            hivesFragment.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    hivesFragment, R.id.contentFrame);
        }

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new HivesPresenter(goBeesRepository, hivesFragment, apiaryId);
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
