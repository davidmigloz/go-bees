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
 * Format label in xºC format.
 */
public class TempValueFormatter implements IValueFormatter, IAxisValueFormatter {

    private Unit unit;

    public TempValueFormatter(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
        return formatTemperature(convertValue(value));
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return formatTemperature(convertValue(value));
    }

    /**
     * Format temperature.
     *
     * @param value temperature value.
     * @return temperature formatted.
     */
    private String formatTemperature(float value) {
        return Math.round(value) + unit.toString();
    }

    /**
     * Convert temperature units.
     *
     * @param value temperature in celsius.
     * @return value converted.
     */
    private float convertValue(float value) {
        switch (unit) {
            case CELSIUS:
                return value;
            case FAHRENHEIT:
                return (float) WeatherUtils.celsiusToFahrenheit(value);
            default:
                throw new IllegalArgumentException();
        }
    }

    public enum Unit {
        CELSIUS, FAHRENHEIT;

        @Override
        public String toString() {
            switch (this) {
                case CELSIUS:
                    return "\u00B0C";
                case FAHRENHEIT:
                    return "\u00B0F";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
