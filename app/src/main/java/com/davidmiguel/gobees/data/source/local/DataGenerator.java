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

package com.davidmiguel.gobees.data.source.local;

import android.content.Context;
import android.location.Location;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.MeteoRecord;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.utils.DateTimeUtils;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Generate sample data: 1 apiary with 3 hives. Each hive with 3 recordings:
 * - normal activity - good weather.
 * - very low activity - bad weather.
 * - swarming activity - good weather.
 */
public class DataGenerator implements GoBeesDataSource.SaveRecordingCallback {

    private static final double LAT = 42.352083;
    private static final double LON = -3.697586;
    // SAMPLE 1: normal activity - good weather
    private static final int[] GOOD_NUM_BEES = {
            0, 0, 0, 0, 0, 0, 1, 0, 1, 2, 0, 1, 0, // 7h
            1, 1, 2, 1, 2, 2, 2, 3, 3, 4, 2, 4, // 8h
            4, 5, 7, 6, 6, 7, 5, 4, 6, 5, 4, 7, // 9h
            10, 9, 11, 12, 11, 11, 10, 12, 11, 10, 11, 11, // 10h
            12, 13, 13, 13, 12, 13, 13, 12, 12, 11, 15, 14, // 11h
            14, 15, 15, 14, 15, 16, 15, 15, 17, 21, 19, 25, // 12h
            26, 25, 28, 32, 30, 26, 22, 17, 16, 17, 15, 14, // 13h
            14, 10, 15, 14, 14, 11, 13, 12, 10, 9, 7, 8,  // 14h
            8, 7, 8, 8, 8, 9, 9, 8, 8, 9, 8, 7, // 15h
            6, 5, 7, 5, 5, 5, 6, 5, 5, 7, 6, 5, // 16h
            5, 4, 4, 4, 6, 4, 5, 4, 4, 4, 5, 4, // 17h
            3, 3, 4, 2, 2, 4, 4, 3, 2, 2, 2, 2, // 18h
            1, 2, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, //19h
            0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0 // 20h
    };
    private static final double[] GOOD_WEATHER_TEMP = {
            7.0, 10.0, 11.0, 15.0, 18.0, 21.0, 25.0, 26.0, 26.0, 26.0, 22.0, 20.0, 15.0, 14.0
    };
    private static final double[] GOOD_WEATHER_RAIN = {
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.2, 0.5
    };
    private static final double[] GOOD_WEATHER_WIND = {
            3.0, 2.0, 2.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 3.0, 4.0, 4.0
    };
    // SAMPLE 2: very low activity - bad weather
    private static final int[] VERY_LOW_NUM_BEES = {
            0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, // 7h
            0, 0, 1, 1, 0, 0, 1, 1, 2, 1, 0, 0, // 8h
            0, 1, 1, 1, 1, 2, 2, 1, 0, 0, 0, 0, // 9h
            1, 1, 1, 2, 2, 1, 3, 1, 1, 0, 1, 1, // 10h
            2, 3, 3, 3, 2, 3, 3, 2, 2, 1, 0, 0, // 11h
            0, 1, 1, 1, 2, 2, 1, 1, 1, 1, 2, 2, // 12h
            1, 1, 0, 2, 2, 2, 2, 3, 3, 3, 4, 3, // 13h
            4, 3, 5, 4, 4, 3, 3, 2, 1, 3, 2, 2,  // 14h
            2, 1, 2, 3, 3, 2, 1, 2, 2, 2, 0, 0, // 15h
            1, 1, 1, 2, 2, 1, 3, 1, 1, 1, 0, 1, // 16h
            0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, // 17h
            0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, // 18h
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //19h
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 // 20h
    };
    private static final double[] BAD_WEATHER_TEMP = {
            1.0, 1.0, 2.0, 3.0, 3.0, 3.0, 4.0, 4.0, 4.0, 3.0, 2.0, 1.0, 0.0, 0.0
    };
    private static final double[] BAD_WEATHER_RAIN = {
            1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 5.0, 2.0, 1.0
    };
    private static final double[] BAD_WEATHER_WIND = {
            10.0, 8.0, 8.0, 5.0, 4.0, 4.0, 4.0, 5.0, 5.0, 10.0, 6.0, 1.0, 4.0, 4.0
    };
    // SAMPLE 3: swarming activity - good weather
    private static final int[] SWARMING_NUM_BEES = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 7h
            1, 2, 1, 1, 1, 2, 2, 3, 1, 1, 2, 1, // 8h
            2, 3, 2, 4, 6, 7, 5, 4, 6, 5, 4, 7, // 9h
            10, 9, 11, 12, 30, 38, 39, 39, 10, 9, 11, 10, // 10h
            10, 9, 8, 10, 12, 13, 13, 12, 12, 11, 10, 10, // 11h
            8, 7, 9, 9, 12, 13, 12, 12, 10, 11, 10, 9, // 12h
            9, 8, 8, 7, 8, 6, 8, 8, 9, 10, 11, 12, // 13h
            11, 12, 13, 14, 13, 12, 13, 12, 10, 9, 7, 6,  // 14h
            6, 7, 8, 7, 8, 9, 7, 8, 7, 9, 7, 7, // 15h
            5, 5, 6, 4, 5, 5, 4, 5, 5, 7, 4, 5, // 16h
            3, 4, 5, 4, 6, 3, 5, 4, 3, 4, 3, 4, // 17h
            3, 1, 2, 2, 2, 3, 2, 3, 2, 2, 1, 2, // 18h
            2, 1, 2, 0, 2, 1, 0, 2, 0, 1, 0, 1, //19h
            1, 0, 2, 1, 1, 0, 1, 1, 2, 1, 0, 0 // 20h
    };
    private static final double[] SWARMING_WEATHER_TEMP = {
            17.0, 18.0, 20.0, 22.0, 25.0, 28.0, 30.0, 33.0, 30.0, 28.0, 25.0, 25.0, 20.0, 20.0
    };
    private static final double[] SWARMING_WEATHER_RAIN = {
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
    };
    private static final double[] SWARMING_WEATHER_WIND = {
            1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 2.0
    };

    private Context context;
    private GoBeesDataSource goBeesDataSource;

    public DataGenerator(Context context, GoBeesDataSource goBeesDataSource) {
        this.context = context;
        this.goBeesDataSource = goBeesDataSource;
    }

    public void generateData() {
        // Generate apiary
        Apiary apiary = generateApiary();
        // Generate hives
        List<Hive> hives = Lists.newArrayList(
                generateHive(apiary, 1),
                generateHive(apiary, 2),
                generateHive(apiary, 3)
        );
        // Generate recordings
        Date date;
        // SAMPLE 1: normal activity - good weather
        date = new Date(1474347600000L); // 20/09/16 7:00
        for (Hive hive : hives) {
            generateRecording(hive, date, GOOD_NUM_BEES);
        }
        generateWeatherData(apiary, date, GOOD_WEATHER_TEMP, GOOD_WEATHER_RAIN, GOOD_WEATHER_WIND);
        // SAMPLE 2: very low activity - bad weather
        date = new Date(1483596000000L); // 05/01/17 7:00
        for (Hive hive : hives) {
            generateRecording(hive, date, VERY_LOW_NUM_BEES);
        }
        generateWeatherData(apiary, date, BAD_WEATHER_TEMP, BAD_WEATHER_RAIN, BAD_WEATHER_WIND);
        // SAMPLE 3: swarming activity - good weather
        date = new Date(1474779600000L); // 25/09/16 7:00
        for (Hive hive : hives) {
            generateRecording(hive, date, SWARMING_NUM_BEES);
        }
        generateWeatherData(apiary, date, SWARMING_WEATHER_TEMP,
                SWARMING_WEATHER_RAIN, SWARMING_WEATHER_WIND);
    }

    /**
     * Generate a new apiary.
     *
     * @return new apiary.
     */
    private Apiary generateApiary() {
        Apiary apiary = new Apiary();
        // Set id
        final long[] id = new long[1];
        goBeesDataSource.getNextApiaryId(new GoBeesDataSource.GetNextApiaryIdCallback() {
            @Override
            public void onNextApiaryIdLoaded(long apiaryId) {
                id[0] = apiaryId;
            }
        });
        apiary.setId(id[0]);
        // Set name
        apiary.setName(context.getString(R.string.sample_apiary_name));
        // Set random location
        Location location = getRandomNearLocation(LAT, LON);
        apiary.setLocationLat(location.getLatitude());
        apiary.setLocationLong(location.getLongitude());
        // Set notes
        apiary.setNotes(context.getString(R.string.sample_apiary_notes));
        // Save apiary
        goBeesDataSource.saveApiary(apiary, this);
        goBeesDataSource.refreshApiaries();
        return apiary;
    }

    /**
     * Generate a new hive.
     *
     * @param apiary apiary which it belongs.
     * @param number number to generate name.
     * @return new hive.
     */
    private Hive generateHive(Apiary apiary, int number) {
        Hive hive = new Hive();
        // Set id
        final long[] id = new long[1];
        goBeesDataSource.getNextHiveId(new GoBeesDataSource.GetNextHiveIdCallback() {
            @Override
            public void onNextHiveIdLoaded(long hiveId) {
                id[0] = hiveId;
            }
        });
        hive.setId(id[0]);
        // Set name
        hive.setName(String.format(context.getString(R.string.sample_hive_name), number));
        // Set notes
        hive.setNotes(context.getString(R.string.sample_hive_notes));
        // Set last revision
        hive.setLastRevision(new Date());
        // Save hive
        goBeesDataSource.saveHive(apiary.getId(), hive, this);
        return hive;
    }

    private Recording generateRecording(Hive hive, Date date, int[] numBees) {
        Date hour = new Date(date.getTime());
        List<Record> records = new ArrayList<>(numBees.length);
        // Generate records
        for (int numBee : numBees) {
            records.add(new Record(hour, numBee));
            hour = DateTimeUtils.sumTimeToDate(hour, 0, 5, 0);
        }
        // Save recording
        goBeesDataSource.saveRecords(hive.getId(), records, this);
        return new Recording(date, records);
    }

    private void generateWeatherData(Apiary apiary, Date date,
                                     double[] temp, double[] rain, double[] wind) {
        Date hour = new Date(date.getTime());
        // Generate meteo record
        List<MeteoRecord> meteoRecords = new ArrayList<>(temp.length);
        for (int i = 0; i < temp.length; i++) {
            meteoRecords.add(new MeteoRecord(hour, "", -1, "", temp[i], -1, -1, -1, -1,
                    wind[i], -1, -1, rain[i], -1));
            hour = DateTimeUtils.sumTimeToDate(hour, 1, 0, 0);
        }
        // Save meteo records
        goBeesDataSource.saveMeteoRecords(apiary.getId(), meteoRecords);
    }

    /**
     * Generate random location.
     *
     * @param latitude  center latitude.
     * @param longitude center longitude.
     * @return random location.
     */
    private Location getRandomNearLocation(double latitude, double longitude) {
        Random random = new Random();
        double lat = BigDecimal.valueOf(latitude + random.nextInt(100) / 100d)
                .setScale(7, RoundingMode.HALF_UP).doubleValue();
        double lon = BigDecimal.valueOf(longitude + random.nextInt(100) / 100d)
                .setScale(7, RoundingMode.HALF_UP).doubleValue();
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);
        return location;
    }

    @Override
    public void onSuccess() {
        // Nothing to do
    }

    @Override
    public void onFailure() {
        // Nothing to do
    }

    @Override
    public void onRecordingTooShort() {
        // Nothing to do
    }
}
