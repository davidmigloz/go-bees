package com.davidmiguel.gobees.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Format label in x m/s format.
 */
public class WindValueFormatter implements IValueFormatter {

    private Unit unit;

    public WindValueFormatter(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
        return Math.round(value) + unit.toString();
    }

    public enum Unit {
        MS;

        @Override
        public String toString() {
            switch (this) {
                case MS:
                    return "m/s";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
