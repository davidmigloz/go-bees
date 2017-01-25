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

package com.davidmiguel.gobees.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.local.DataGenerator;
import com.davidmiguel.gobees.data.source.repository.GoBeesRepository;

/**
 * Listens to user actions from the UI SettingsFragment, retrieves the data and updates the
 * UI as required.
 */
class SettingsPresenter implements SettingsContract.Presenter,
        Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private GoBeesRepository goBeesRepository;
    private SettingsContract.View view;

    SettingsPresenter(GoBeesRepository goBeesRepository, SettingsContract.View view) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        // Nothing to do
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values)
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation
            preference.setSummary(stringValue);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "generateData":
                generateSampleData(view.getContext());
                break;
            case "deleteData":
                deleteAllData();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void bindPreferenceClickListener(Preference preference) {
        preference.setOnPreferenceClickListener(this);
    }

    @Override
    public void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's current value
        onPreferenceChange(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

    /**
     * Generates a sample apiary.
     *
     * @param context context.
     */
    private void generateSampleData(final Context context) {
        view.showLoadingMsg();
        DataGenerator dataGenerator = new DataGenerator(context, goBeesRepository);
        dataGenerator.generateData();
        view.showDataGeneratedMsg();
    }

    /**
     * Delete all data stored in the database.
     */
    private void deleteAllData() {
        view.showLoadingMsg();
        goBeesRepository.deleteAll(new GoBeesDataSource.TaskCallback() {
            @Override
            public void onSuccess() {
                view.showDataDeletedMsg();
            }

            @Override
            public void onFailure() {
                // Nothing to do
            }
        });
    }
}
