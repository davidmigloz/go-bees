package com.davidmiguel.gobees.data.source.local;

import io.realm.RealmConfiguration;

/**
 * Configuration of the realm database.
 */
public class GoBeesDbConfig {

    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "gobees.realm";

    private RealmConfiguration realmConfiguration = null;

    public RealmConfiguration getRealmConfiguration() {
        if (realmConfiguration != null) {
            return realmConfiguration;
        }
        // Create config
        realmConfiguration = new RealmConfiguration.Builder()
                .name(DATABASE_NAME)
                .schemaVersion(DATABASE_VERSION)
                .deleteRealmIfMigrationNeeded() // TODO delete this for production (use RealmMigration)
                .build();
        return realmConfiguration;
    }
}
