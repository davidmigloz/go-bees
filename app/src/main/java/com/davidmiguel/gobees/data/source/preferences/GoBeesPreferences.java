package com.davidmiguel.gobees.data.source.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.davidmiguel.gobees.R;

/**
 * Access to data stored in shared preferences.
 */
public class GoBeesPreferences {

    /**
     * Returns true if the user has selected metric temperature display.
     *
     * @param context Context used to get the SharedPreferences.
     * @return true if metric display should be used, false if imperial display should be used.
     */
    public static boolean isMetric(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUnits = context.getString(R.string.pref_temp_units_key);
        String defaultUnits = context.getString(R.string.pref_temp_units_metric);
        String preferredUnits = sp.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_temp_units_metric);

        return metric.equals(preferredUnits);
    }
}
