package com.davidmiguel.gobees.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Format label in xºC format.
 */
public class TempValueFormatter implements IValueFormatter {

    private Unit unit;

    public TempValueFormatter(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
        return Math.round(value) + unit.toString();
    }

    public enum Unit {
        CELSIUS, FAHRENHEIT;

        @Override
        public String toString() {
            switch (this) {
                case CELSIUS:
                    return "ºC";
                case FAHRENHEIT:
                    return "ºF";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
