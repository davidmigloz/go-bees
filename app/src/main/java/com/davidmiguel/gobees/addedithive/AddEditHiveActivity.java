package com.davidmiguel.gobees.addedithive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Add / edit hive activity.
 */
public class AddEditHiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
