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

package com.davidmiguel.gobees;

import android.app.Application;

import com.davidmiguel.gobees.data.source.local.GoBeesDbConfig;

import io.realm.Realm;

/**
 * Main app.
 */
public class GoBeesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        // Get Realm config
        GoBeesDbConfig realmConfig = new GoBeesDbConfig();
        // Delete all
        Realm.deleteRealm(realmConfig.getRealmConfiguration());
        // Set config
        Realm.setDefaultConfiguration(realmConfig.getRealmConfiguration());
    }
}
