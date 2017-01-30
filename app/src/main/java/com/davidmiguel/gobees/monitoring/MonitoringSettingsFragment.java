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

package com.davidmiguel.gobees.monitoring;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.TwoStatePreference;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.monitoring.algorithm.BeesCounter;
import com.vanniktech.vntnumberpickerpreference.VNTNumberPickerPreference;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display list of Preference objects.
 * Android 3.0 (API level 11) and higher.
 */
public class MonitoringSettingsFragment extends PreferenceFragment
        implements MonitoringContract.SettingsView, Preference.OnPreferenceChangeListener {

    private MonitoringContract.Presenter presenter;
    private RelativeLayout settingsLayout;

    public MonitoringSettingsFragment() {
        // Requires empty public constructor
    }

    public static MonitoringSettingsFragment newInstance() {
        return new MonitoringSettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.monitoring_settings);
        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        settingsLayout = (RelativeLayout) getActivity().findViewById(R.id.settings);
        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.closeSettings();
            }
        });
        return view;
    }


    @Override
    public void initSettings() {
        // Updated when the preference changes
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_blob_size_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_min_area_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_max_area_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_zoom_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_show_algo_output_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_frame_rate_key)));
    }

    @Override
    public void showSettings() {
        final Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        fadeIn.setDuration(400);
        settingsLayout.setVisibility(View.VISIBLE);
        settingsLayout.startAnimation(fadeIn);
    }

    @Override
    public void hideSettings() {
        final Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        fadeOut.setDuration(200);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Nothing to do
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                settingsLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Nothing to do
            }
        });
        settingsLayout.startAnimation(fadeOut);
    }

    @Override
    public MonitoringSettings getMonitoringSettings() {
        MonitoringSettings monitoringSettings = new MonitoringSettings();
        monitoringSettings.setBlobSize(getBlobSize());
        monitoringSettings.setMinArea(getMinArea());
        monitoringSettings.setMaxArea(getMaxArea());
        monitoringSettings.setZoomRatio(getZoomRatio());
        monitoringSettings.setFrameRate(getFrameRate());
        monitoringSettings.setMaxFrameWidth(640);
        monitoringSettings.setMaxFrameHeight(480);
        return monitoringSettings;
    }

    @Override
    public void setPresenter(@NonNull MonitoringContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes
        preference.setOnPreferenceChangeListener(this);
        // Update and set summary of the preference
        updatePreference(preference, null);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        updatePreference(preference, value);
        return true;
    }

    /**
     * Update monitoring preference.
     *
     * @param preference preference to update.
     * @param value      new value.
     */
    private void updatePreference(Preference preference, Object value) {
        // Get value if not passed
        Object newVal;
        if (value == null) {
            if (preference instanceof VNTNumberPickerPreference) {
                newVal = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getInt(preference.getKey(), Integer.parseInt(preference.getSummary().toString()));
            } else if (preference instanceof TwoStatePreference) {
                newVal = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), true);
            } else {
                newVal = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), "");
            }
        } else {
            newVal = value;
        }
        // Update preference
        updateAlgorithm(preference, newVal);
        updateSummary(preference, newVal);
    }

    /**
     * Update parameter of the bee counter algorithm.
     *
     * @param preference parameter to update.
     * @param value      new value.
     */
    private void updateAlgorithm(Preference preference, Object value) {
        if (preference.getKey().equals(getString(R.string.pref_show_algo_output_key))) {
            // Show algo output
            Boolean val = (Boolean) value;
            presenter.showAlgoOutput(val);
        } else if (preference.getKey().equals(getString(R.string.pref_blob_size_key))) {
            // Update blob size
            String val = (String) value;
            presenter.updateAlgoBlobSize(getBlobSize(val));
        } else if (preference.getKey().equals(getString(R.string.pref_min_area_key))) {
            // Update min area
            presenter.updateAlgoMinArea(((Integer) value).doubleValue());
        } else if (preference.getKey().equals(getString(R.string.pref_max_area_key))) {
            // Update max area
            presenter.updateAlgoMaxArea(((Integer) value).doubleValue());
        } else if (preference.getKey().equals(getString(R.string.pref_zoom_key))) {
            // Update zoom
            presenter.updateAlgoZoom(Integer.parseInt((String) value));
        }
    }

    /**
     * Update the summary of the preference with the new value.
     *
     * @param preference preference to update summary.
     * @param value      new value.
     */
    private void updateSummary(Preference preference, Object value) {
        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values)
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue((String) value);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof VNTNumberPickerPreference) {
            // For number pickers, show the value
            preference.setSummary(Integer.toString((Integer) value));
        } else {
            // For other preferences, set the summary to the value's simple string representation
            preference.setSummary(value.toString());
        }
    }

    /**
     * Get blob size (This causes regions within an image get "thicker" or "thinner").
     *
     * @return blob size.
     */
    private BeesCounter.BlobSize getBlobSize() {
        // Get value
        String value = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_blob_size_key),
                        getString(R.string.pref_blob_size_normal));
        // Convert value
        return getBlobSize(value);

    }

    /**
     * Get blob size from string.
     *
     * @param val string with the value.
     * @return BlobSize,
     */
    private BeesCounter.BlobSize getBlobSize(String val) {
        BeesCounter.BlobSize size = BeesCounter.BlobSize.NORMAL;
        if (val.equals(getString(R.string.pref_blob_size_small))) {
            size = BeesCounter.BlobSize.SMALL;
        } else if (val.equals(getString(R.string.pref_blob_size_normal))) {
            size = BeesCounter.BlobSize.NORMAL;
        } else if (val.equals(getString(R.string.pref_blob_size_big))) {
            size = BeesCounter.BlobSize.BIG;
        }
        return size;
    }

    /**
     * Get min area. Smaller areas are not consider to be a bee.
     *
     * @return min area.
     */
    private double getMinArea() {
        // Get value
        return PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getInt(getString(R.string.pref_min_area_key),
                        getActivity().getResources().getInteger(R.integer.pref_min_area_default));
    }

    /**
     * Get max area. Greater areas are not consider to be a bee.
     *
     * @return max area.
     */
    private double getMaxArea() {
        // Get value
        return PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getInt(getString(R.string.pref_max_area_key),
                        getActivity().getResources().getInteger(R.integer.pref_max_area_default));
    }

    /**
     * Get zoom ratio (100 = x1, 200 = x2…).
     *
     * @return zoom ratio.
     */
    private int getZoomRatio() {
        // Get value
        String value = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_zoom_key), getString(R.string.pref_zoom_x1));
        // Convert
        return Integer.parseInt(value);
    }

    /**
     * Get frame ratio.
     *
     * @return frame ratio.
     */
    private long getFrameRate() {
        // Get value
        String value = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_frame_rate_key), getString(R.string.pref_frame_rate_1min));
        // Convert
        return Long.parseLong(value);
    }
}
