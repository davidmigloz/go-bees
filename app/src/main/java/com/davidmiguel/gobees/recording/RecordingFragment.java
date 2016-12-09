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
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.HourAxisValueFormatter;
import com.davidmiguel.gobees.utils.RainValueFormatter;
import com.davidmiguel.gobees.utils.ScrollChildSwipeRefreshLayout;
import com.davidmiguel.gobees.utils.StringUtils;
import com.davidmiguel.gobees.utils.TempValueFormatter;
import com.davidmiguel.gobees.utils.WindValueFormatter;
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
                break;
        }
        return true;
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
        setupTempChart();
        setupRainChart();
        setupWindChart();
        // Show temp chart by default
        showTempChart();
    }

    @Override
    public void showLoadingRecordingError() {
        showMessage(getString(R.string.loading_recording_error));
    }

    @Override
    public void showNoRecords() {
        // TODO
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
        tempIcon.setColorFilter(chartVisible.equals("temp") ?
                ContextCompat.getColor(getContext(), R.color.colorTempIcon) : defaultColor);
        rainIcon.setColorFilter(chartVisible.equals("rain") ?
                ContextCompat.getColor(getContext(), R.color.colorRainIcon) : defaultColor);
        windIcon.setColorFilter(chartVisible.equals("wind") ?
                ContextCompat.getColor(getContext(), R.color.colorWindIcon) : defaultColor);
    }

    /**
     * Configure bees chart and the data.
     *
     * @param recordsList list of records.
     */
    private void setupBeesChart(List<Record> recordsList) {
        // Setup data
        long firstTimestamp = recordsList.get(0).getTimestamp().getTime() / 1000;
        Record[] records = recordsList.toArray(new Record[recordsList.size()]);
        List<Entry> entries = new ArrayList<>();
        for (Record record : records) {
            // Convert timestamp to seconds and relative to first timestamp
            long timestamp = (record.getTimestamp().getTime() / 1000 - firstTimestamp);
            entries.add(new Entry(timestamp, record.getNumBees()));
        }
        // Style char lines (type, color, etc.)
        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.num_bees));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        LineData data = new LineData(lineDataSet);
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
        // X axis setup
        IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(firstTimestamp);
        XAxis xAxis = beesChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextColor(Color.BLACK);
        // Y axis setup
        YAxis leftAxis = beesChart.getAxisLeft();
        leftAxis.setAxisMaximum(40);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = beesChart.getAxisRight();
        rightAxis.setAxisMaximum(40);
        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawGridLines(true);
        rightAxis.setDrawAxisLine(false);
        // Add data
        beesChart.setData(data);
    }

    /**
     * Configure temperature chart and the data.
     */
    private void setupTempChart() {
        // Setup data
        int[] degrees = {16, 20, 30, 33, 25};
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < degrees.length; i++) {
            entries.add(new Entry(i, degrees[i]));
        }
        // Style char lines (type, color, etc.)
        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.temperature));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueFormatter(new TempValueFormatter(TempValueFormatter.Unit.CELSIUS));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLineTempChart));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.colorFillTempChart));
        lineDataSet.setFillAlpha(255);
        LineData data = new LineData(lineDataSet);
        // General setup
        tempChart.setDrawGridBackground(false);
        tempChart.setDrawBorders(false);
        tempChart.setViewPortOffsets(0, 0, 0, 0);
        tempChart.getDescription().setEnabled(false);
        tempChart.getLegend().setEnabled(false);
        tempChart.setTouchEnabled(false);
        // X axis setup
        tempChart.getXAxis().setEnabled(false);
        // Y axis setup
        tempChart.getAxisLeft().setAxisMinimum(0);
        tempChart.getAxisLeft().setEnabled(false);
        tempChart.getAxisRight().setEnabled(false);
        // Add data
        tempChart.setData(data);
    }


    /**
     * Configure rain chart and the data.
     */
    private void setupRainChart() {
        // Setup data
        int[] degrees = {1, 1, 10, 3, 2};
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < degrees.length; i++) {
            entries.add(new Entry(i, degrees[i]));
        }
        // Style char lines (type, color, etc.)
        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.rain));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueFormatter(new RainValueFormatter(RainValueFormatter.Unit.MM));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLineRainChart));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.colorFillRainChart));
        lineDataSet.setFillAlpha(255);
        LineData data = new LineData(lineDataSet);
        // General setup
        rainChart.setDrawGridBackground(false);
        rainChart.setDrawBorders(false);
        rainChart.setViewPortOffsets(0, 0, 0, 0);
        rainChart.getDescription().setEnabled(false);
        rainChart.getLegend().setEnabled(false);
        rainChart.setTouchEnabled(false);
        // X axis setup
        rainChart.getXAxis().setEnabled(false);
        // Y axis setup
        rainChart.getAxisLeft().setAxisMinimum(0);
        rainChart.getAxisLeft().setEnabled(false);
        rainChart.getAxisRight().setEnabled(false);
        // Add data
        rainChart.setData(data);
    }


    /**
     * Configure wind chart and the data.
     */
    private void setupWindChart() {
        // Setup data
        int[] degrees = {5, 5, 5, 1, 1};
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < degrees.length; i++) {
            entries.add(new Entry(i, degrees[i]));
        }
        // Style char lines (type, color, etc.)
        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.wind));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueFormatter(new WindValueFormatter(WindValueFormatter.Unit.MS));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLineWindChart));
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
        windChart.getXAxis().setEnabled(false);
        // Y axis setup
        windChart.getAxisLeft().setAxisMinimum(0);
        windChart.getAxisLeft().setEnabled(false);
        windChart.getAxisRight().setEnabled(false);
        // Add data
        windChart.setData(data);
    }
}
