/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees.data.source.local;

import io.realm.RealmConfiguration;

/**
 * Configuration of the realm database.
 */
public class GoBeesDbConfig {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gobees.realm";

    private RealmConfiguration realmConfiguration = null;

    public RealmConfiguration getRealmConfiguration() {
        if (realmConfiguration != null) {
            return realmConfiguration;
        }
        // Create config
        realmConfiguration = new RealmConfiguration.Builder()
                .name(DATABASE_NAME)
                .schemaVersion(DATABASE_VERSION)
                .migration(new GoBeesDbMigration())
                .build();
        return realmConfiguration;
    }
}
