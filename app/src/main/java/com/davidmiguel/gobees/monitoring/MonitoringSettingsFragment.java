package com.davidmiguel.gobees.monitoring;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.davidmiguel.gobees.R;
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
        // updated when the preference changes
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_blob_size_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_min_area_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_max_area_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_zoom_key)));
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

        // Trigger the listener immediately with the preference's current value
        onPreferenceChange(preference, null);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        if (preference instanceof ListPreference) {
            String stringValue = value != null ? value.toString() : PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), "");
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values)
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof VNTNumberPickerPreference) {
            int intValue = value != null ? (Integer) value : PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getInt(preference.getKey(), Integer.parseInt(preference.getSummary().toString()));
            preference.setSummary(Integer.toString(intValue));
        } else {
            // For other preferences, set the summary to the value's simple string representation
            String stringValue = value != null ? value.toString() : PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), "");
            preference.setSummary(stringValue);
        }
        return true;
    }
}
