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

package com.davidmiguel.gobees.data.model;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import java.util.Date;
import java.util.List;

/**
 * Model class for a recording. A recording contains all the records from one day.
 * A recording is not stored in the db, just the records.
 */
public class Recording implements Comparable<Recording> {

    /**
     * Data of the recording.
     */
    private Date date;

    /**
     * List of all records of that date.
     */
    private List<Record> records;

    /**
     * List of meteorological records.
     */
    private List<MeteoRecord> meteo;

    public Recording(Date date, List<Record> records, List<MeteoRecord> meteo) {
        this.date = date;
        this.records = records;
        this.meteo = meteo;
    }

    public Recording(Date date, List<Record> records) {
        this.date = date;
        this.records = records;
    }

    public Date getDate() {
        return date;
    }

    public List<Record> getRecords() {
        return records;
    }

    public List<MeteoRecord> getMeteo() {
        return meteo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Recording recording = (Recording) obj;
        return Objects.equal(date, recording.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }

    @Override
    public int compareTo(@NonNull Recording recording) {
        return this.date.compareTo(recording.getDate());
    }
}
