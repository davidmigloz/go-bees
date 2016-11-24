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
        Realm.setDefaultConfiguration(realmConfig.getRealmConfiguration());
    }
}
