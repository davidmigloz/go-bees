package com.davidmiguel.gobees.addedithive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Add / edit hive activity.
 */
public class AddEditHiveActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_HIVE = 1;
    public static final int NEW_HIVE = -1;

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
