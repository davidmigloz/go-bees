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
        mDate.setTime(timestamp * 1000); // Convert from seconds to milliseconds
        return mDataFormat.format(mDate);
    }
}
