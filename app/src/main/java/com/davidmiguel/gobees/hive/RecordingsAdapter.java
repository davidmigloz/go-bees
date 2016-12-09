package com.davidmiguel.gobees.hive;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.BaseViewHolder;
import com.davidmiguel.gobees.utils.HourAxisValueFormatter;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;
import com.davidmiguel.gobees.utils.StringUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Recordings list adapter.
 */
class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.ViewHolder> {

    private Context context;
    private List<Recording> recordings;
    private RecordingItemListener listener;

    RecordingsAdapter(Context context, List<Recording> recordings, RecordingItemListener listener) {
        this.context = context;
        this.recordings = checkNotNull(recordings);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hive_recordings_list_item, parent, false);
        return new RecordingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(recordings.get(position));
    }

    @Override
    public int getItemCount() {
        return recordings == null ? 0 : recordings.size();
    }

    void replaceData(List<Recording> recordings) {
        this.recordings = checkNotNull(recordings);
        notifyDataSetChanged();
    }

    interface RecordingItemListener {
        void onRecordingClick(Recording clickedRecording);

        void onRecordingDelete(Recording clickedRecording);
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<Recording>, View.OnClickListener, ItemTouchHelperViewHolder {

        private CardView card;
        private TextView recordingDate;
        private LineChart chart;

        private Drawable background;
        private SimpleDateFormat formatter;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            card = (CardView) itemView.findViewById(R.id.card);
            recordingDate = (TextView) itemView.findViewById(R.id.recording_date);
            chart = (LineChart) itemView.findViewById(R.id.chart);

            background = card.getBackground();
            formatter = new SimpleDateFormat(
                    context.getString(R.string.hive_recordings_date_format), Locale.getDefault());
        }

        public void bind(@NonNull final Recording recording) {
            // Title
            String date = formatter.format(recording.getDate());
            recordingDate.setText(StringUtils.capitalize(date));

            // Chart
            if (recording.getRecords() != null && !recording.getRecords().isEmpty()) {
                long firstTimestamp = recording.getRecords().get(0).getTimestamp().getTime() / 1000;
                List<Entry> entries = getChartData(recording.getRecords(), firstTimestamp);
                LineData data = styleChartLines(entries);
                setupChart(chart, data, firstTimestamp);
            }
        }

        @Override
        public void onClick(View view) {
            listener.onRecordingClick(recordings.get(getAdapterPosition()));
        }

        @Override
        public void onItemSelected() {
            card.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            card.setBackground(background);
        }

        /**
         * Get and prepare the data to draw in the chart.
         *
         * @param recordsList    list of records.
         * @param firstTimestamp seconds timestamp of the first record (used as initial reference).
         * @return list of entries.
         */
        private List<Entry> getChartData(List<Record> recordsList, long firstTimestamp) {
            Record[] records = recordsList.toArray(new Record[recordsList.size()]);
            List<Entry> entries = new ArrayList<>();
            for (Record record : records) {
                // Convert timestamp to seconds and relative to first timestamp
                long timestamp = (record.getTimestamp().getTime() / 1000 - firstTimestamp);
                entries.add(new Entry(timestamp, record.getNumBees()));
            }
            return entries;
        }

        /**
         * Style char lines (type, color, etc.).
         *
         * @param entries list of entries.
         * @return line data chart.
         */
        private LineData styleChartLines(List<Entry> entries) {
            // Get primary dark color
            int color = ContextCompat.getColor(context, R.color.colorAccent);
            // Set styles
            LineDataSet lineDataSet = new LineDataSet(entries, "Recording");
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setCubicIntensity(0.2f);
            lineDataSet.setDrawValues(false);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setLineWidth(1.8f);
            lineDataSet.setColor(color);
            lineDataSet.setDrawFilled(true);
//            lineDataSet.setFillColor(color);
            lineDataSet.setFillAlpha(255);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_green);
            lineDataSet.setFillDrawable(drawable);
            return new LineData(lineDataSet);
        }

        /**
         * Setup chart (axis, grid, etc.).
         *
         * @param lineChart      chart to setup.
         * @param data           chart with the data.
         * @param firstTimestamp seconds timestamp of the first record (used as initial reference).
         */
        private void setupChart(LineChart lineChart, LineData data, long firstTimestamp) {
            // General setup
            lineChart.setDrawGridBackground(false);
            lineChart.setDrawBorders(false);
            lineChart.setViewPortOffsets(0, 0, 0, 0);
            lineChart.getDescription().setEnabled(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.setTouchEnabled(false);
            // X axis setup
            IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(firstTimestamp);
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(xAxisFormatter);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
            xAxis.setCenterAxisLabels(true);
            xAxis.setTextColor(Color.WHITE);
            // Y axis setup
            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setAxisMaximum(40);
            yAxis.setAxisMinimum(0);
            yAxis.setDrawGridLines(true);
            xAxis.setDrawAxisLine(false);
            lineChart.getAxisRight().setEnabled(false);
            // Add data
            lineChart.setData(data);
        }
    }
}
