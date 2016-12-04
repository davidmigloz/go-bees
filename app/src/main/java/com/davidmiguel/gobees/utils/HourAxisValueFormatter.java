package com.davidmiguel.gobees.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Format label in (HH:mm) format from a timestamp (relative to a reference timestamp).
 */
public class HourAxisValueFormatter implements IAxisValueFormatter {

    private long referenceTimestamp;
    private DateFormat mDataFormat;
    private Date mDate;

    /**
     * Constructor of the formatter.
     *
     * @param referenceTimestamp minimum timestamp of the data set (in seconds).
     */
    public HourAxisValueFormatter(long referenceTimestamp) {
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        this.mDate = new Date();
    }

    /**
     * Called when a value from an axis is to be formatted before being drawn.
     *
     * @param value the value to be formatted.
     * @param axis  the axis the value belongs to.
     * @return timestamp formatted.
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // relativeTimestamp = originalTimestamp - referenceTimestamp
        long relativeTimestamp = (long) value;
        // Retrieve absolute timestamp (referenceTimestamp + relativeTimestamp)
        long originalTimestamp = referenceTimestamp + relativeTimestamp;
        // Convert timestamp to hour:minute
        return getHour(originalTimestamp);
    }

    /**
     * Get formatted hour from a timestamp.
     *
     * @param timestamp timestamp to format.
     * @return formatted hour.
     */
    private String getHour(long timestamp) {
        try {
            mDate.setTime(timestamp * 1000); // Convert from seconds to milliseconds
            return mDataFormat.format(mDate);
        } catch (Exception ex) {
            return "--:--";
        }
    }
}
