package com.davidmiguel.gobees.backupstorage;

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.io.File;

/**
 * This specifies the contract between the view and the presenter.
 */
interface StorageBackupRestoreContract {

    interface View extends BaseView<StorageBackupRestoreContract.Presenter> {

        /**
         * Checks whether READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions are granted.
         * If not, asks for it.
         *
         * @return if the permission is granted.
         */
        boolean checkReadWritePermission();

        /**
         * Return the primary external storage directory.
         *
         * @return primary external storage directory.
         */
        File getExternalStorageDirectory();

        /**
         * Shows backup error message.
         */
        void showBackupError();

        /**
         * Shows successfully backup created message.
         */
        void showSuccessfullyBackupMessage();
    }

    interface Presenter extends BasePresenter {

        /**
         * When Backup Now button is clicked.
         */
        void onBackupClicked();

    }
}
