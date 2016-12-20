package com.davidmiguel.gobees.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Format label in xmm format.
 */
public class RainValueFormatter implements IValueFormatter {

    private Unit unit;

    public RainValueFormatter(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
        return Math.round(value) + unit.toString();
    }

    public enum Unit {
        MM;

        @Override
        public String toString() {
            switch (this) {
                case MM:
                    return "mm";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
