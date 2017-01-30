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

package com.davidmiguel.gobees.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Format label in x m/s format.
 */
public class WindValueFormatter implements IValueFormatter, IAxisValueFormatter {

    private Unit unit;

    public WindValueFormatter(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
        return formatWind(value);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return formatWind(value);
    }

    /**
     * Format wind.
     *
     * @param value wind value.
     * @return wind formatted.
     */
    private String formatWind(float value) {
        return Math.round(value) + unit.toString();
    }

    public enum Unit {
        MS;

        @Override
        public String toString() {
            if (this == MS) {
                return "m/s";
            }
            return "";
        }
    }
}
