package com.davidmiguel.gobees.backupstorage;

import java.util.Date;

/**
 * Model class for a storage backup.
 */
public class StorageBackup {

    private Date backupDate;
    private long backupSize;

    public StorageBackup(Date backupDate, long backupSize) {
        this.backupDate = backupDate;
        this.backupSize = backupSize;
    }

    public Date getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(Date backupDate) {
        this.backupDate = backupDate;
    }

    public long getBackupSize() {
        return backupSize;
    }

    public void setBackupSize(long backupSize) {
        this.backupSize = backupSize;
    }
}
