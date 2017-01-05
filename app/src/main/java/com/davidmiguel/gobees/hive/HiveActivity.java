package com.davidmiguel.gobees.hive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.utils.BaseTabFragment;
import com.davidmiguel.gobees.utils.TabsFragmentPagerAdapter;
import com.google.common.collect.Lists;

/**
 * Hive activity.
 */
public class HiveActivity extends AppCompatActivity {

    public static final int NO_APIARY = -1;
    public static final int NO_HIVE = -1;

    private Fragment hiveRecordingsFragment;
    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hive_act);

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

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
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

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new HivePresenter(goBeesRepository, (HiveContract.View) hiveRecordingsFragment,
                apiaryId, hiveId);
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
        if (hiveRecordingsFragment != null) {
            hiveRecordingsFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
