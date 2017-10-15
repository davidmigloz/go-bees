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

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.davidmiguel.gobees.data.source.local.GoBeesDbConfig;
import com.davidmiguel.gobees.logging.Log;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashlytics();
        initLogger();
        initRealm();
    }

    protected void initCrashlytics() {
        // Set up Crashlytics, disabled for mock builds
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder()
                .disabled(isMock()).build())
                .build());
    }

    protected void initLogger() {
        Log.initLogger();
    }

    protected void initRealm() {
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        // Get Realm config
        GoBeesDbConfig realmConfig = new GoBeesDbConfig();
        Realm.setDefaultConfiguration(realmConfig.getRealmConfiguration());
    }

    public static boolean isMock() {
        return "mock".equals(BuildConfig.FLAVOR);
    }

}
