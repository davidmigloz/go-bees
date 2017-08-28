package com.davidmiguel.gobees.backupstorage;

import android.util.Log;

import com.google.common.collect.Lists;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.exceptions.RealmFileException;

/**
 * Listens to user actions from the UI StorageBackupRestoreFragment, retrieves the data
 * and updates the UI as required.
 */
class StorageBackupRestorePresenter implements StorageBackupRestoreContract.Presenter {

    private static final String TAG = StorageBackupRestorePresenter.class.getSimpleName();

    private static final String APP_DIR = "GoBees";
    private static final String EXPORT_DIR = "backup";
    private static final String EXPORT_FILE_NAME = "gobees_data.backup";
    private static final String IMPORT_FILE_NAME = "gobees.realm";

    private StorageBackupRestoreContract.View view;
    private Realm realm;

    StorageBackupRestorePresenter(StorageBackupRestoreContract.View view, Realm realm) {
        this.view = view;
        this.view.setPresenter(this);
        this.realm = realm;
    }

    @Override
    public void start() {
        view.showAvailableBackups(getAvailableBackups());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onBackupClicked() {
        if (view.checkReadWritePermission()) {
            // Get timestamp
            String timestamp = new SimpleDateFormat("yyyyMMddHHmm",
                    Locale.getDefault()).format(new Date());

            // Create app dir (/GoBees/) if not exists
            File appDir = new File(view.getExternalStorageDirectory(), APP_DIR);
            if (!appDir.exists() && !appDir.mkdir()) {
                view.showBackupError();
                return;
            }

            // Create backup dir (/GoBees/backup/) if not exists
            File backupDir = new File(appDir, EXPORT_DIR);
            if (!backupDir.exists() && !backupDir.mkdir()) {
                view.showBackupError();
                return;
            }

            // Create dir for the new backup (/GoBees/backup/timestamp/)
            File exportDir = new File(backupDir, timestamp);
            if (!exportDir.exists() && !exportDir.mkdir()) {
                view.showBackupError();
                return;
            }

            // Create export file (/GoBees/backup/timestamp/timestamp_gobees_data.backup)
            // Delete if it already exists
            File exportFile = new File(exportDir, timestamp + "_" + EXPORT_FILE_NAME);
            exportFile.delete();

            // Log files
            Log.i(TAG, "Realm DB Path = " + realm.getPath());
            Log.i(TAG, "Backup DB Path = " + exportFile.getPath());

            // Copy current Realm db to backup file
            try {
                realm.writeCopyTo(exportFile);
            } catch (RealmFileException ex) {
                view.showBackupError();
                return;
            } finally {
                realm.close();
            }

            // Show backup created msg
            view.showSuccessfullyBackupMessage();
        }
    }

    private List<StorageBackup> getAvailableBackups() {
        return Lists.newArrayList(
                new StorageBackup(new Date(), 10),
                new StorageBackup(new Date(), 8),
                new StorageBackup(new Date(), 1),
                new StorageBackup(new Date(), 15)
        );
    }
}
