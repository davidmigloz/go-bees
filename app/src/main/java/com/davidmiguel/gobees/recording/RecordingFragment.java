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

package com.davidmiguel.gobees.recording;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.MeteoRecord;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.preferences.GoBeesPreferences;
import com.davidmiguel.gobees.utils.HourAxisValueFormatter;
import com.davidmiguel.gobees.utils.RainValueFormatter;
import com.davidmiguel.gobees.utils.ScrollChildSwipeRefreshLayout;
import com.davidmiguel.gobees.utils.StringUtils;
import com.davidmiguel.gobees.utils.TempValueFormatter;
import com.davidmiguel.gobees.utils.WeatherUtils;
import com.davidmiguel.gobees.utils.WindValueFormatter;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display recording detail.
 */
public class RecordingFragment extends Fragment implements RecordingContract.View {

    public static final String ARGUMENT_APIARY_ID = "APIARY_ID";
    public static final String ARGUMENT_HIVE_ID = "HIVE_ID";
    public static final String ARGUMENT_START_DATE = "START_DATE";
    public static final String ARGUMENT_END_DATE = "END_DATE";

    private RecordingContract.Presenter presenter;
    private LineChart beesChart;
    private LineChart tempChart;
    private LineChart rainChart;
    private LineChart windChart;
    private ImageView tempIcon;
    private ImageView rainIcon;
    private ImageView windIcon;

    private long referenceTimestamp;
    private long lastTimestamp;

    public RecordingFragment() {
        // Requires empty public constructor
    }

    public static RecordingFragment newInstance() {
        return new RecordingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recording_frag, container, false);

        // Get charts
        beesChart = (LineChart) root.findViewById(R.id.recording_chart);
        tempChart = (LineChart) root.findViewById(R.id.temp_chart);
        rainChart = (LineChart) root.findViewById(R.id.rain_chart);
        windChart = (LineChart) root.findViewById(R.id.wind_chart);

        // Get icons
        tempIcon = (ImageView) root.findViewById(R.id.temp_icon);
        rainIcon = (ImageView) root.findViewById(R.id.rain_icon);
        windIcon = (ImageView) root.findViewById(R.id.wind_icon);

        // Set icons listeners
        tempIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openTempChart();
            }
        });
        rainIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openRainChart();
            }
        });
        windIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openWindChart();
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        LinearLayout ll = (LinearLayout) root.findViewById(R.id.linear_layout);
        swipeRefreshLayout.setScrollUpChild(ll);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLoadingIndicator(false);
            }
        });

        // Listen menu options
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recording_frag_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.menu_refresh:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);
        // Make sure setRefreshing() is called after the layout is done with everything else
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showRecording(@NonNull Recording recording) {
        // Setup charts
        setupBeesChart(recording.getRecords());
        if (recording.getMeteo() != null && recording.getMeteo().size() > 0) {
            setupTempChart(recording.getMeteo());
            setupRainChart(recording.getMeteo());
            setupWindChart(recording.getMeteo());
        } else {
            showNoWeatherData();
        }
        // Show temp chart by default
        showTempChart();
    }

    @Override
    public void showLoadingRecordingError() {
        showMessage(getString(R.string.loading_recording_error));
    }

    @Override
    public void showNoRecords() {
        showMessage(getString(R.string.no_records_error));
    }

    @Override
    public void showTempChart() {
        setChartsVisibility("temp");
        highlightChartIcon("temp");
    }

    @Override
    public void showRainChart() {
        setChartsVisibility("rain");
        highlightChartIcon("rain");
    }

    @Override
    public void showWindChart() {
        setChartsVisibility("wind");
        highlightChartIcon("wind");
    }

    @Override
    public void showTitle(@NonNull Date date) {
        // Format date
        SimpleDateFormat formatter = new SimpleDateFormat(
                getString(R.string.recording_date_format), Locale.getDefault());
        String title = StringUtils.capitalize(formatter.format(date));
        // Set title
        ActionBar ab = ((RecordingActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }
    }

    @Override
    public void setPresenter(@NonNull RecordingContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * Shows a snackbar with the given message.
     *
     * @param message message to show.
     */
    @SuppressWarnings("ConstantConditions")
    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Makes visible given chart and hides the others.
     *
     * @param chartVisible chart to make visible.
     */
    private void setChartsVisibility(String chartVisible) {
        tempChart.setVisibility(chartVisible.equals("temp") ? View.VISIBLE : View.GONE);
        rainChart.setVisibility(chartVisible.equals("rain") ? View.VISIBLE : View.GONE);
        windChart.setVisibility(chartVisible.equals("wind") ? View.VISIBLE : View.GONE);
    }

    /**
     * Highlightes given chart icon and softs the others.
     *
     * @param chartVisible chart to make icon visible.
     */
    private void highlightChartIcon(String chartVisible) {
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorDivider);
        tempIcon.setColorFilter(chartVisible.equals("temp")
                ? ContextCompat.getColor(getContext(), R.color.colorTempIcon) : defaultColor);
        rainIcon.setColorFilter(chartVisible.equals("rain")
                ? ContextCompat.getColor(getContext(), R.color.colorRainIcon) : defaultColor);
        windIcon.setColorFilter(chartVisible.equals("wind")
                ? ContextCompat.getColor(getContext(), R.color.colorWindIcon) : defaultColor);
    }

    /**
     * Configure bees chart and the data.
     *
     * @param recordsList list of records.
     */
    private void setupBeesChart(List<Record> recordsList) {
        // Setup data
        referenceTimestamp = recordsList.get(0).getTimestamp().getTime() / 1000;
        Record[] records = recordsList.toArray(new Record[recordsList.size()]);
        List<Entry> entries = new ArrayList<>();
        int maxNumBees = 0;
        for (Record record : records) {
            // Convert timestamp to seconds and relative to first timestamp
            long timestamp = (record.getTimestamp().getTime() / 1000 - referenceTimestamp);
            int numBees = record.getNumBees();
            entries.add(new Entry(timestamp, numBees));
            // Get max num of bees
            if (numBees > maxNumBees) {
                maxNumBees = numBees;
            }
        }
        lastTimestamp = (long) entries.get(entries.size() - 1).getX();
        // Style char lines (type, color, etc.)
        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.num_bees));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        // General setup
        beesChart.setDrawGridBackground(false);
        beesChart.setDrawBorders(false);
        beesChart.setViewPortOffsets(80, 40, 80, 40);
        beesChart.getDescription().setEnabled(false);
        beesChart.getLegend().setEnabled(false);
        beesChart.setTouchEnabled(true);
        beesChart.setDragEnabled(false);
        beesChart.setScaleEnabled(false);
        beesChart.setPinchZoom(false);
        BeesMarkerView mv = new BeesMarkerView(getContext(), R.layout.recording_bees_marker_vew);
        mv.setChartView(beesChart);
        beesChart.setMarker(mv);
        beesChart.setNoDataText(getString(R.string.no_flight_act_data_available));
        // X axis setup
        IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(referenceTimestamp);
        XAxis xAxis = beesChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextColor(Color.BLACK);
        // Y axis setup
        YAxis leftAxis = beesChart.getAxisLeft();
        leftAxis.setAxisMaximum(maxNumBees > 40 ? maxNumBees + 2 : 40);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = beesChart.getAxisRight();
        rightAxis.setAxisMaximum(maxNumBees > 40 ? maxNumBees + 2 : 40);
        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawGridLines(true);
        rightAxis.setDrawAxisLine(false);
        // Add data
        beesChart.setData(new LineData(lineDataSet));
    }

    /**
     * Configure temperature chart and the data.
     *
     * @param meteo meteo records.
     */
    private void setupTempChart(List<MeteoRecord> meteo) {
        // Setup data
        List<Entry> entries = new ArrayList<>();
        // Add as first entry a copy of the first temperature record
        // First relative timestamp is 0 (-5 to don't show the value in the chart)
        entries.add(new Entry(-5, (float) meteo.get(0).getTemperature()));
        // Add all temperature records
        float maxTemp = Float.MIN_VALUE;
        float minTemp = Float.MAX_VALUE;
        for (MeteoRecord meteoRecord : meteo) {
            // Convert timestamp to seconds and relative to first timestamp
            long timestamp = (meteoRecord.getTimestamp().getTime() / 1000 - referenceTimestamp);
            float temperature = (float) meteoRecord.getTemperature();
            entries.add(new Entry(timestamp, temperature));
            // Get max and min temperature
            if (temperature > maxTemp) {
                maxTemp = temperature;
            }
            if (temperature < minTemp) {
                minTemp = temperature;
            }
        }
        // Add as last entry a copy of the last temperature record (+5 to don't show the value in the chart)
        entries.add(new Entry(lastTimestamp + 5, (float) meteo.get(meteo.size() - 1).getTemperature()));
        // Style char lines (type, color, etc.)
        TempValueFormatter tempValueFormatter = new TempValueFormatter(
                GoBeesPreferences.isMetric(getContext()) ?
                        TempValueFormatter.Unit.CELSIUS : TempValueFormatter.Unit.FAHRENHEIT);
        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.temperature));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueFormatter(tempValueFormatter);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLineTempChart));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.colorFillTempChart));
        lineDataSet.setFillAlpha(255);
        // General setup
        tempChart.setDrawGridBackground(false);
        tempChart.setDrawBorders(false);
        tempChart.setViewPortOffsets(0, 0, 0, 0);
        tempChart.getDescription().setEnabled(false);
        tempChart.getLegend().setEnabled(false);
        tempChart.setTouchEnabled(false);
        // X axis setup
        XAxis xAxis = tempChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(lastTimestamp);
        // Y axis setup
        YAxis leftAxis = tempChart.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setAxisMaximum(maxTemp + 5);
        leftAxis.setAxisMinimum(minTemp - 5);
        tempChart.getAxisRight().setEnabled(false);
        // Add data
        tempChart.setData(new LineData(lineDataSet));
    }


    /**
     * Configure rain chart and the data.
     *
     * @param meteo meteo records.
     */
    private void setupRainChart(List<MeteoRecord> meteo) {
        // Setup data
        List<Entry> entries = new ArrayList<>();
        // Add as first entry a copy of the first rain record
        // First relative timestamp is 0 (-5 to don't show the value in the chart)
        entries.add(new Entry(-5, (float) meteo.get(0).getRain()));
        // Add all rain records
        float maxRain = Float.MIN_VALUE;
        for (MeteoRecord meteoRecord : meteo) {
            // Convert timestamp to seconds and relative to first timestamp
            long timestamp = (meteoRecord.getTimestamp().getTime() / 1000 - referenceTimestamp);
            float rain = (float) meteoRecord.getRain();
            entries.add(new Entry(timestamp, rain));
            // Get max and min temperature
            if (rain > maxRain) {
                maxRain = rain;
            }
        }
        // Add as last entry a copy of the last rain record (+5 to don't show the value in the chart)
        entries.add(new Entry(lastTimestamp + 5, (float) meteo.get(meteo.size() - 1).getRain()));
        // Style char lines (type, color, etc.)
        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.rain));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueFormatter(new RainValueFormatter(RainValueFormatter.Unit.MM));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLineRainChart));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.colorFillRainChart));
        lineDataSet.setFillAlpha(255);
        // General setup
        rainChart.setDrawGridBackground(false);
        rainChart.setDrawBorders(false);
        rainChart.setViewPortOffsets(0, 0, 0, 0);
        rainChart.getDescription().setEnabled(false);
        rainChart.getLegend().setEnabled(false);
        rainChart.setTouchEnabled(false);
        // X axis setup
        XAxis xAxis = rainChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(lastTimestamp);
        // Y axis setup
        YAxis leftAxis = rainChart.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setAxisMaximum(maxRain + 1);
        leftAxis.setAxisMinimum(0);
        rainChart.getAxisRight().setEnabled(false);
        // Add data
        rainChart.setData(new LineData(lineDataSet));
    }


    /**
     * Configure wind chart and the data.
     *
     * @param meteo meteo records.
     */
    private void setupWindChart(List<MeteoRecord> meteo) {
        // Setup data
        List<Entry> entries = new ArrayList<>();
        // Add as first entry a copy of the first wind record
        // First relative timestamp is 0 (-5 to don't show the value in the chart)
        entries.add(new Entry(-5, (float) meteo.get(0).getWindSpeed()));
        // Add all wind records
        float maxWind = Float.MIN_VALUE;
        for (MeteoRecord meteoRecord : meteo) {
            // Convert timestamp to seconds and relative to first timestamp
            long timestamp = (meteoRecord.getTimestamp().getTime() / 1000 - referenceTimestamp);
            float wind = (float) meteoRecord.getWindSpeed();
            entries.add(new Entry(timestamp, wind));
            // Get max and min temperature
            if (wind > maxWind) {
                maxWind = wind;
            }
        }
        // Add as last entry a copy of the last wind record (+5 to don't show the value in the chart)
        entries.add(new Entry(lastTimestamp + 5, (float) meteo.get(meteo.size() - 1).getWindSpeed()));
        // Style char lines (type, color, etc.)
        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.wind));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueFormatter(new WindValueFormatter(WindValueFormatter.Unit.MS));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLineWindChart));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.colorFillWindChart));
        lineDataSet.setFillAlpha(255);
        LineData data = new LineData(lineDataSet);
        // General setup
        windChart.setDrawGridBackground(false);
        windChart.setDrawBorders(false);
        windChart.setViewPortOffsets(0, 0, 0, 0);
        windChart.getDescription().setEnabled(false);
        windChart.getLegend().setEnabled(false);
        windChart.setTouchEnabled(false);
        // X axis setup
        XAxis xAxis = windChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(lastTimestamp);
        // Y axis setup
        YAxis leftAxis = windChart.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setAxisMaximum(maxWind + 1);
        leftAxis.setAxisMinimum(0);
        windChart.getAxisRight().setEnabled(false);
        // Add data
        windChart.setData(data);
    }

    private void showNoWeatherData() {
        float textSize = WeatherUtils.convertDpToPixel(getResources(), 12);
        tempChart.setNoDataText(getString(R.string.no_weather_data_available));
        tempChart.getPaint(Chart.PAINT_INFO).setTextSize(textSize);
        rainChart.setNoDataText(getString(R.string.no_weather_data_available));
        rainChart.getPaint(Chart.PAINT_INFO).setTextSize(textSize);
        windChart.setNoDataText(getString(R.string.no_weather_data_available));
        windChart.getPaint(Chart.PAINT_INFO).setTextSize(textSize);
    }
}
