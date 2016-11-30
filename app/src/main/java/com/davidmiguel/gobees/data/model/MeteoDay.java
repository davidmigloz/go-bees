package com.davidmiguel.gobees.data.model;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Model class for saving the general meteorology of a specific day.
 */
@SuppressWarnings("unused")
public class MeteoDay extends RealmObject {

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
     * Weather condition description, e.g: "overcast clouds".
     */
    @Nullable
    private String description;

    /**
     * Temperature in Kelvin, e.g: 274.03.
     */
    private double temperature;

    /**
     * Minimum temperature, e.g: 273.15.
     */
    private double temperatureMin;

    /**
     * Maximum temperature in Kelvin, e.g: 274.82.
     */
    private double temperatureMax;

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

    public MeteoDay() {
        // Needed by Realm
    }

    public MeteoDay(long id, Date timestamp, @Nullable String icon, @Nullable String description, double temperature,
                    double temperatureMin, double temperatureMax, int clouds, double rain,
                    double snow, int humidity, double windSpeed, double windDegrees) {
        this.id = id;
        this.timestamp = timestamp;
        this.icon = icon;
        this.description = description;
        this.temperature = temperature;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.clouds = clouds;
        this.rain = rain;
        this.snow = snow;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDegrees = windDegrees;
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

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
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
