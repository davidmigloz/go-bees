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

import com.davidmiguel.gobees.data.source.local.GoBeesDbConfig;

import io.realm.Realm;

/**
 * Deletes all date whenever the app is restarted.
 */
public class App extends BaseApp {

    @Override
    protected void initRealm() {
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        GoBeesDbConfig realmConfig = new GoBeesDbConfig();
        // Delete all
        Realm.deleteRealm(realmConfig.getRealmConfiguration());
        // Set config
        Realm.setDefaultConfiguration(realmConfig.getRealmConfiguration());
    }
}
