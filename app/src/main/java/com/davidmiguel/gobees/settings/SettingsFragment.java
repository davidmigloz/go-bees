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
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.apiaries.ApiariesActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display list of Preference objects.
 * Android 3.0 (API level 11) and higher.
 */
public class SettingsFragment extends PreferenceFragment implements SettingsContract.View {

    private SettingsContract.Presenter presenter;
    private Toast loadingToast;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.general_settings);

        // For all preferences with values, attach an OnPreferenceChangeListener
        // so the UI summary can be updated when the preference changes
        presenter.bindPreferenceSummaryToValue(
                findPreference(getString(R.string.pref_weather_units_key)));

        // For all preferences that trigger some action, attach an OnPreferenceClickListener
        presenter.bindPreferenceClickListener(
                findPreference(getString(R.string.pref_generate_sample_data_key)));
        presenter.bindPreferenceClickListener(
                findPreference(getString(R.string.pref_delete_all_data_key)));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull SettingsContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showLoadingMsg() {
        loadingToast = Toast.makeText(getActivity(), getString(R.string.loading_msg),
                Toast.LENGTH_SHORT);
        loadingToast.show();
    }

    @Override
    public void showDataGeneratedMsg() {
        hideLoading();
        // Show msg
        Toast.makeText(getActivity(), getString(R.string.sample_data_generated_msg),
                Toast.LENGTH_SHORT).show();
        // Go to main activity
        Intent intent = new Intent(getActivity(), ApiariesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);


    }

    @Override
    public void showDataDeletedMsg() {
        hideLoading();
        // Show msg
        Toast.makeText(getActivity(), getString(R.string.all_data_deleted_msg),
                Toast.LENGTH_SHORT).show();
        // Go to main activity
        Intent intent = new Intent(getActivity(), ApiariesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    /**
     * Hides loading message.
     */
    private void hideLoading() {
        if (loadingToast != null) {
            loadingToast.cancel();
        }
    }
}
