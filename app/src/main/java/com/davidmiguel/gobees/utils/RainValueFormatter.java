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

import java.util.Locale;

/**
 * Format label in xmm format.
 */
public class RainValueFormatter implements IValueFormatter, IAxisValueFormatter {

    private Unit unit;

    public RainValueFormatter(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
        return formatRain(value);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return formatRain(value);
    }

    /**
     * Format rain.
     *
     * @param value rain value.
     * @return rain formatted.
     */
    private String formatRain(float value) {
        return String.format(Locale.getDefault(), "%.1f", value) + unit.toString();
    }

    public enum Unit {
        MM;

        @Override
        public String toString() {
            if (this == MM) {
                return "mm";
            }
            return "";
        }
    }
}
