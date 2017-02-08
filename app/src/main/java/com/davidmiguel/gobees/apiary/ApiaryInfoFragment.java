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

package com.davidmiguel.gobees.apiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseTabFragment;
import com.davidmiguel.gobees.utils.WeatherUtils;
import com.google.common.base.Strings;

import java.util.Date;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display apiary info.
 */
public class ApiaryInfoFragment extends Fragment
        implements BaseTabFragment, ApiaryContract.ApiaryInfoView {

    private ApiaryContract.Presenter presenter;
    private FloatingActionButton fab;

    private TextView location;
    private ImageView map;
    private TextView numHives;
    private TextView lastRevision;
    private TextView notes;

    private CardView weatherCard;
    private ImageView weatherIcon;
    private TextView temperature;
    private TextView city;
    private TextView condition;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView rain;
    private TextView snow;
    private TextView updated;


    public static ApiaryInfoFragment newInstance() {
        return new ApiaryInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.apiary_info_frag, container, false);

        // Set up view
        ScrollView info = (ScrollView) root.findViewById(R.id.info);

        location = (TextView) root.findViewById(R.id.location);
        map = (ImageView) root.findViewById(R.id.map_icon);
        numHives = (TextView) root.findViewById(R.id.num_hives);
        lastRevision = (TextView) root.findViewById(R.id.last_revision);
        notes = (TextView) root.findViewById(R.id.notes_content);

        weatherCard = (CardView) root.findViewById(R.id.weather_card);
        weatherIcon = (ImageView) root.findViewById(R.id.weather_icon);
        temperature = (TextView) root.findViewById(R.id.temperature);
        city = (TextView) root.findViewById(R.id.city);
        condition = (TextView) root.findViewById(R.id.condition);
        humidity = (TextView) root.findViewById(R.id.humidity);
        pressure = (TextView) root.findViewById(R.id.pressure);
        wind = (TextView) root.findViewById(R.id.wind);
        rain = (TextView) root.findViewById(R.id.rain);
        snow = (TextView) root.findViewById(R.id.snow);
        updated = (TextView) root.findViewById(R.id.updated);

        // Set map intent
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onOpenMapClicked();
            }
        });

        // Set up floating action button
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_hive);

        // Set up progress indicator
        AndroidUtils.setUpProgressIndicator(root, getContext(), info, presenter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
        // Reset map icon color
        map.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public int getTabName() {
        return R.string.apiary_info_tab;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        AndroidUtils.setLoadingIndicator(getView(), active);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showInfo(Apiary apiary, Date lastRevisionDate) {
        // GENERAL INFO
        // Location
        if (apiary.hasLocation()) {
            String latLetter = (apiary.getLocationLat() > 0) ? "N" : "S";
            String lonLetter = (apiary.getLocationLong() > 0) ? "E" : "W";
            location.setText(apiary.getLocationLat() + latLetter + " / "
                    + apiary.getLocationLong() + lonLetter);
        }
        // Num hives
        int num = apiary.getHives().size();
        numHives.setText(getResources().getQuantityString(R.plurals.num_hives_plurals, num, num));
        // Last revision
        if (lastRevisionDate != null) {
            lastRevision.setText(DateUtils.getRelativeTimeSpanString(lastRevisionDate.getTime(),
                    (new Date()).getTime(), DateUtils.MINUTE_IN_MILLIS));
        }
        // Notes
        if (Strings.isNullOrEmpty(apiary.getNotes())) {
            notes.setText(getString(R.string.no_notes));
        } else {
            notes.setText(apiary.getNotes());
        }

        // WEATHER
        // Hide card if no weather data exists
        if (apiary.getCurrentWeather() == null) {
            weatherCard.setVisibility(View.GONE);
            return;
        }
        // Weather Icon
        String iconId = apiary.getCurrentWeather().getWeatherConditionIcon();
        weatherIcon.setImageResource(WeatherUtils.getWeatherIconResourceId(iconId));
        // Temperature
        double temp = apiary.getCurrentWeather().getTemperature();
        temperature.setText(WeatherUtils.formatTemperature(getContext(), temp));
        // City
        String cityName = apiary.getCurrentWeather().getCityName();
        city.setText(cityName);
        // Weather condition
        String weatherCondition = WeatherUtils.getStringForWeatherCondition(getContext(),
                apiary.getCurrentWeather().getWeatherCondition());
        condition.setText(weatherCondition);
        // Humidity
        double hum = apiary.getCurrentWeather().getHumidity();
        humidity.setText(WeatherUtils.formatHumidity(getContext(), hum));
        // Pressure
        double pre = apiary.getCurrentWeather().getPressure();
        pressure.setText(WeatherUtils.formatPressure(getContext(), pre));
        // Wind
        double windSpeed = apiary.getCurrentWeather().getWindSpeed();
        double windDegrees = apiary.getCurrentWeather().getWindDegrees();
        wind.setText(WeatherUtils.formatWind(getContext(), windSpeed, windDegrees));
        // Rain
        double rainVol = apiary.getCurrentWeather().getRain();
        rain.setText(WeatherUtils.formatRainSnow(getContext(), rainVol));
        // Snow
        double snowVol = apiary.getCurrentWeather().getSnow();
        snow.setText(WeatherUtils.formatRainSnow(getContext(), snowVol));
        // Last update
        String date = (String) DateUtils.getRelativeTimeSpanString(apiary.getCurrentWeather()
                .getTimestamp().getTime(), (new Date()).getTime(), DateUtils.MINUTE_IN_MILLIS);
        updated.setText(date);
    }

    @Override
    public void openMap(Apiary apiary) {
        if (apiary.hasLocation()) {
            // Change icon color
            map.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            // Open map
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f",
                    apiary.getLocationLat(), apiary.getLocationLong());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            getContext().startActivity(intent);
        }
    }

    @Override
    public void setPresenter(@NonNull ApiaryContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
