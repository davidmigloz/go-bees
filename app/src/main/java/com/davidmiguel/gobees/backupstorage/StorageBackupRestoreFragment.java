package com.davidmiguel.gobees.backupstorage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rebus.permissionutils.AskAgainCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;
import rebus.permissionutils.SimpleCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * View layer.
 * Display storage backup and restore options.
 */
public class StorageBackupRestoreFragment extends Fragment
        implements StorageBackupRestoreContract.View,
        StorageBackupAdapter.StorageBackupItemListener {

    private StorageBackupRestoreContract.Presenter presenter;
    private StorageBackupAdapter storageBackupAdapter;


    public StorageBackupRestoreFragment() {
        // Requires empty public constructor
    }

    public static StorageBackupRestoreFragment newInstance() {
        return new StorageBackupRestoreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.storagebackup_frag, container, false);

        // Backup button
        Button backupBtn = (Button) root.findViewById(R.id.backup_btn);
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackupClicked();
            }
        });

        // Backups list
        RecyclerView backupListRV = (RecyclerView) root.findViewById(R.id.backup_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        backupListRV.setLayoutManager(llm);
        backupListRV.setAdapter(storageBackupAdapter);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageBackupAdapter = new StorageBackupAdapter(getContext(),
                new ArrayList<StorageBackup>(0), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull StorageBackupRestoreContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public boolean checkReadWritePermission() {
        // Check read/write permission
        if (PermissionUtils.isGranted(getActivity(),
                PermissionEnum.READ_EXTERNAL_STORAGE, PermissionEnum.WRITE_EXTERNAL_STORAGE)) {
            return true;
        }
        // Ask for permission if they are not granted
        PermissionManager.Builder()
                .permission(PermissionEnum.READ_EXTERNAL_STORAGE,
                        PermissionEnum.WRITE_EXTERNAL_STORAGE)
                .askAgain(true)
                .askAgainCallback(new AskAgainCallback() {
                    @Override
                    public void showRequestPermission(final UserResponse response) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.permission_request_title))
                                .setMessage(getString(R.string.read_write_permissions_request_body))
                                .setPositiveButton(
                                        getString(R.string.permission_request_allow_button),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface,
                                                                int which) {
                                                response.result(true);
                                            }
                                        })
                                .setNegativeButton(
                                        getString(R.string.permission_request_deny_button),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface,
                                                                int which) {
                                                response.result(false);
                                            }
                                        })
                                .setCancelable(false)
                                .show();
                    }
                })
                .callback(new SimpleCallback() {
                    @Override
                    public void result(boolean allPermissionsGranted) {
                        if (!allPermissionsGranted) {
                            // Warn the user that it's not possible to use the feature
                            Toast.makeText(getActivity(),
                                    getString(R.string.permission_request_denied),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .ask(getActivity());
        return false;
    }

    @Override
    public File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    @Override
    public void showBackupError() {
        AndroidUtils.showSnackMsg(getView(), getString(R.string.backup_error_msg));
    }

    @Override
    public void showSuccessfullyBackupMessage() {
        AndroidUtils.showSnackMsg(getView(), getString(R.string.backup_completed_msg));
    }

    @Override
    public void showAvailableBackups(List<StorageBackup> backups) {
        storageBackupAdapter.replaceData(backups);
    }

    @Override
    public void onRestoreClicked(Date date) {

    }
}
