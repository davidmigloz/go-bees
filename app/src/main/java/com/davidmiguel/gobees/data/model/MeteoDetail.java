package com.davidmiguel.gobees.data.model;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Model class for saving the meteorology of a specific moment in time.
 */
@SuppressWarnings("unused")
public class MeteoDetail extends RealmObject {

    @PrimaryKey
    private long id;

    /**
     * Time of data calculation.
     */
    @Required
    private Date timestamp;

    /**
     * Weather icon id, e.g: "04n".
     */
    @Nullable
    private String icon;

    /**
     * Current temperature in Kelvin, e.g: 285.95.
     */
    private double temperature;

    /**
     * Percentage of cloudiness, e.g: 56.
     */
    private int clouds;

    /**
     * Rain volume (mm) for the last 3 hours, e.g: 3.25.
     */
    private double rain;

    /**
     * Snow volume (mm) for the last 3 hours, e.g: 2.3.
     */
    private double snow;

    /**
     * Percentage of humidity, e.g: 18.
     */
    private int humidity;

    /**
     * Wind speed in meter/sec, e.g: 6.17.
     */
    private double windSpeed;

    /**
     * Wind direction in degrees (meteorological), e.g: 209.5.
     */
    private double windDegrees;

    public MeteoDetail() {
        // Needed by Realm
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Nullable
    public String getIcon() {
        return icon;
    }

    public void setIcon(@Nullable String icon) {
        this.icon = icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(double windDegrees) {
        this.windDegrees = windDegrees;
    }
}
