package com.davidmiguel.gobees.hive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Hive activity.
 */
public class HiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
