package com.davidmiguel.gobees.backupstorage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Storage backups adapter.
 */
class StorageBackupAdapter
        extends RecyclerView.Adapter<StorageBackupAdapter.StorageBackupViewHolder> {

    private Context context;
    private List<StorageBackup> storageBackups;
    private StorageBackupItemListener listener;

    StorageBackupAdapter(Context context, List<StorageBackup> storageBackups,
                      StorageBackupItemListener listener) {
        this.context = context;
        this.storageBackups = checkNotNull(storageBackups);
        this.listener = listener;
    }

    @Override
    public StorageBackupAdapter.StorageBackupViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.storagebackup_list_item, parent, false);
        return new StorageBackupAdapter.StorageBackupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StorageBackupAdapter.StorageBackupViewHolder holder, int position) {
        holder.bind(storageBackups.get(position));
    }

    @Override
    public int getItemCount() {
        return storageBackups == null ? 0 : storageBackups.size();
    }

    void replaceData(List<StorageBackup> storageBackups) {
        this.storageBackups = checkNotNull(storageBackups);
        notifyDataSetChanged();
    }

    interface StorageBackupItemListener {
        void onRestoreClicked(Date date);
    }

    class StorageBackupViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<StorageBackup>, View.OnClickListener {

        private TextView backupDateTv;
        private TextView backupSizeTv;
        private Button restoreBtn;

        private SimpleDateFormat formatter;

        StorageBackupViewHolder(View itemView) {
            super(itemView);

            // Get views
            backupDateTv = (TextView) itemView.findViewById(R.id.backup_date);
            backupSizeTv = (TextView) itemView.findViewById(R.id.backup_size);
            restoreBtn = (Button) itemView.findViewById(R.id.restore_btn);

            // Set listeners
            restoreBtn.setOnClickListener(this);

            // Date formatter
            formatter = new SimpleDateFormat(
                    context.getString(R.string.hive_recordings_date_format), Locale.getDefault());
        }

        @Override
        public void bind(@NonNull StorageBackup backup) {
            // Set backup details
            backupDateTv.setText(formatter.format(backup.getBackupDate()));
            backupSizeTv.setText(Formatter.formatFileSize(context, backup.getBackupSize()));
        }

        @Override
        public void onClick(View view) {
            listener.onRestoreClicked(storageBackups.get(getAdapterPosition()).getBackupDate());
        }
    }
}
