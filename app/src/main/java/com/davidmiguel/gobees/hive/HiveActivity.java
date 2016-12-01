package com.davidmiguel.gobees.hive;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
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

    public static final int NO_HIVE = -1;

    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hive_act);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Create recordings fragment
        HiveRecordingsFragment hiveRecordingsFragment = HiveRecordingsFragment.newInstance();

        // Create hive info fragment
        HiveInfoFragment hiveInfoFragment = HiveInfoFragment.newInstance();

        // Set up tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabsFragmentPagerAdapter adapter = new TabsFragmentPagerAdapter(
                getSupportFragmentManager(),
                HiveActivity.this,
                Lists.<BaseTabFragment>newArrayList(hiveRecordingsFragment, hiveInfoFragment)
        );
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Get hive id
        long hiveId = getIntent().getLongExtra(HiveRecordingsFragment.ARGUMENT_HIVE_ID, NO_HIVE);
        if (hiveId == NO_HIVE) {
            throw new IllegalArgumentException("No hive id passed!");
        }

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new HivePresenter(goBeesRepository, hiveRecordingsFragment, hiveId);
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
