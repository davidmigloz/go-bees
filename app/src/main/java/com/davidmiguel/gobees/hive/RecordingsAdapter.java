package com.davidmiguel.gobees.hive;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.BaseViewHolder;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Recording list adapter.
 */
class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.ViewHolder> {

    private List<Recording> recordings;
    private RecordingItemListener listener;

    RecordingsAdapter(List<Recording> recordings, RecordingItemListener listener) {
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
        private Drawable background;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            card = (CardView) itemView.findViewById(R.id.card);
            recordingDate = (TextView) itemView.findViewById(R.id.recording_date);
            background = card.getBackground();
        }

        public void bind(@NonNull Recording recording) {
            recordingDate.setText(recording.getDate().toString());
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
    }
}
