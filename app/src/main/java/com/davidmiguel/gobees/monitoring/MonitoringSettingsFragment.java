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
import com.davidmiguel.gobees.video.BeesCounter;
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
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                settingsLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        settingsLayout.startAnimation(fadeOut);
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
        if (value == null) {
            if (preference instanceof VNTNumberPickerPreference) {
                value = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getInt(preference.getKey(), Integer.parseInt(preference.getSummary().toString()));
            } else if (preference instanceof TwoStatePreference) {
                value = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), true);
            } else {
                value = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), "");
            }
        }
        // Update preference
        updateAlgorithm(preference, value);
        updateSummary(preference, value);
    }

    /**
     * Update parameter of the bee counter algorithm.
     *
     * @param preference parameter to update.
     * @param value      new value.
     */
    void updateAlgorithm(Preference preference, Object value) {
        if (preference.getKey().equals(getString(R.string.pref_show_algo_output_key))) {
            // Show algo output
            Boolean val = (Boolean) value;
            presenter.showAlgoOutput(val);
        } else if (preference.getKey().equals(getString(R.string.pref_blob_size_key))) {
            // Update blob size
            String val = (String) value;
            BeesCounter.BlobSize size = BeesCounter.BlobSize.NORMAL;
            if (val.equals(getString(R.string.pref_blob_size_small))) {
                size = BeesCounter.BlobSize.SMALL;
            } else if (val.equals(getString(R.string.pref_blob_size_normal))) {
                size = BeesCounter.BlobSize.NORMAL;
            } else if (val.equals(getString(R.string.pref_blob_size_big))) {
                size = BeesCounter.BlobSize.BIG;
            }
            presenter.updateAlgoBlobSize(size);
        } else if (preference.getKey().equals(getString(R.string.pref_min_area_key))) {
            // Update min area
            presenter.updateAlgoMinArea(Double.valueOf((Integer) value));
        } else if (preference.getKey().equals(getString(R.string.pref_max_area_key))) {
            // Update max area
            presenter.updateAlgoMaxArea((Double.valueOf((Integer) value)));
        } else if (preference.getKey().equals(getString(R.string.pref_zoom_key))) {
            // Update zoom
            presenter.updateAlgoZoom((Integer.parseInt((String) value)));
        }
    }

    /**
     * Update the summary of the preference with the new value.
     *
     * @param preference preference to update summary.
     * @param value      new value.
     */
    void updateSummary(Preference preference, Object value) {
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
}
