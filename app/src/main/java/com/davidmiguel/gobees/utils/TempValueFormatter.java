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
        return Math.round(convertValue(value)) + unit.toString();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return Math.round(value) + unit.toString();
    }

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
