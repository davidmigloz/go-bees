package com.davidmiguel.gobees.backupstorage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;

import io.realm.Realm;

/**
 * Export and import Realm database instance to/from internal storage.
 */
public class StorageBackupRestoreActivity extends AppCompatActivity {

    private StorageBackupRestoreFragment storageBackupRestoreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storagebackup_act);

        // Set up the toolbar
        AndroidUtils.setUpToolbar(this, false, R.string.pref_backup_cat_label);

        // Add fragment to the activity
        storageBackupRestoreFragment =
                (StorageBackupRestoreFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (storageBackupRestoreFragment == null) {
            // Create the fragment
            storageBackupRestoreFragment = StorageBackupRestoreFragment.newInstance();
            AndroidUtils.addFragmentToActivity(
                    getSupportFragmentManager(), storageBackupRestoreFragment, R.id.contentFrame);
        }

        // Get realm instance
        Realm realm = Realm.getDefaultInstance();

        // Create the presenter
        new StorageBackupRestorePresenter(storageBackupRestoreFragment, realm);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (storageBackupRestoreFragment != null) {
            storageBackupRestoreFragment.onRequestPermissionsResult(requestCode,
                    permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
