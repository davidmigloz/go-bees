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

package com.davidmiguel.gobees.data.source.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.davidmiguel.gobees.R;

/**
 * Access to data stored in shared preferences.
 */
public class GoBeesPreferences {

    private GoBeesPreferences() {
    }

    /**
     * Returns true if the user has selected metric temperature display.
     *
     * @param context Context used to get the Strings.
     * @return true if metric display should be used, false if imperial display should be used.
     */
    public static boolean isMetric(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForUnits = context.getString(R.string.pref_weather_units_key);
        String defaultUnits = context.getString(R.string.pref_weather_units_metric);
        String preferredUnits = sp.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_weather_units_metric);
        return metric.equals(preferredUnits);
    }
}
