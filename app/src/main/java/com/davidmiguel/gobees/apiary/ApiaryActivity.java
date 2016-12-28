package com.davidmiguel.gobees.apiary;

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
 * Apiary activity.
 */
public class ApiaryActivity extends AppCompatActivity {

    public static final int NO_APIARY = -1;

    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apiary_act);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

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

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new ApiaryPresenter(goBeesRepository, apiaryHivesFragment, apiaryId);
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
