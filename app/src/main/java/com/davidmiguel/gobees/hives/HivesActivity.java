package com.davidmiguel.gobees.hives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

/**
 * Hives activity.
 */
public class HivesActivity extends AppCompatActivity {

    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
