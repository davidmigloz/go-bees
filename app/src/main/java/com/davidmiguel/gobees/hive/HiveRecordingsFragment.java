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

package com.davidmiguel.gobees.hive;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.monitoring.MonitoringActivity;
import com.davidmiguel.gobees.monitoring.MonitoringFragment;
import com.davidmiguel.gobees.recording.RecordingActivity;
import com.davidmiguel.gobees.recording.RecordingFragment;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseTabFragment;

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
 * Dispaly a list of recordings.
 */
public class HiveRecordingsFragment extends Fragment
        implements BaseTabFragment, HiveContract.HiveRecordingsView, RecordingsAdapter.RecordingItemListener {

    public static final String ARGUMENT_APIARY_ID = "APIARY_ID";
    public static final String ARGUMENT_HIVE_ID = "HIVE_ID";
    public static final String ARGUMENT_MONITORING_ERROR = "MONITORING_ERROR";
    public static final int ERROR_SAVING_RECORDING = 0;
    public static final int ERROR_RECORDING_TOO_SHORT = 2;

    private HiveContract.Presenter presenter;
    private RecordingsAdapter listAdapter;
    private View noRecordingsView;
    private LinearLayout hivesView;
    private FloatingActionButton fab;

    public HiveRecordingsFragment() {
        // Requires empty public constructor
    }

    public static HiveRecordingsFragment newInstance() {
        return new HiveRecordingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new RecordingsAdapter(getContext(), getActivity().getMenuInflater(),
                new ArrayList<Recording>(0), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.hive_recordings_frag, container, false);

        // Set up recordings list view
        RecyclerView recyclerView = root.findViewById(R.id.recordings_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(listAdapter);
        hivesView = root.findViewById(R.id.recordingsLL);

        // Set up  no recordings view
        noRecordingsView = root.findViewById(R.id.no_recordings);

        // Set up floating action button
        fab = getActivity().findViewById(R.id.fab_new_recording);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startNewRecording();
            }
        });
        fab.setVisibility(View.VISIBLE);

        // Set up progress indicator
        AndroidUtils.setUpProgressIndicator(root, getContext(), recyclerView, presenter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        AndroidUtils.setLoadingIndicator(getView(), active);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode, data);
    }

    @Override
    public void showRecordings(@NonNull List<Recording> recordings) {
        listAdapter.replaceData(recordings);
        hivesView.setVisibility(View.VISIBLE);
        noRecordingsView.setVisibility(View.GONE);
    }

    @Override
    public void startNewRecording(long apiaryId, long hiveId) {
        Intent intent = new Intent(getContext(), MonitoringActivity.class);
        intent.putExtra(MonitoringFragment.ARGUMENT_APIARY_ID, apiaryId);
        intent.putExtra(MonitoringFragment.ARGUMENT_HIVE_ID, hiveId);
        startActivityForResult(intent, MonitoringActivity.REQUEST_MONITORING);
    }

    @Override
    public void showRecordingDetail(long apiaryId, long hiveId, Date date) {
        Intent intent = new Intent(getActivity(), RecordingActivity.class);
        intent.putExtra(RecordingFragment.ARGUMENT_APIARY_ID, apiaryId);
        intent.putExtra(RecordingFragment.ARGUMENT_HIVE_ID, hiveId);
        intent.putExtra(RecordingFragment.ARGUMENT_START_DATE, date.getTime());
        intent.putExtra(RecordingFragment.ARGUMENT_END_DATE, date.getTime());
        getActivity().startActivity(intent);
    }

    @Override
    public void showLoadingRecordingsError() {
        showMessage(getString(R.string.loading_recordings_error));
    }

    @Override
    public void showNoRecordings() {
        showNoRecordingsViews();
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_recording_message));
    }

    @Override
    public void showSaveErrorMessage() {
        showMessage(getString(R.string.save_recording_error_message));
    }

    @Override
    public void showRecordingTooShortErrorMessage() {
        showMessage(getString(R.string.recording_too_short_error_message));
    }

    @Override
    public void showSuccessfullyDeletedMessage() {
        showMessage(getString(R.string.successfully_deleted_recording_message));
    }

    @Override
    public void showDeletedErrorMessage() {
        showMessage(getString(R.string.deleted_recording_error_message));
    }

    @Override
    public void showTitle(@NonNull String title) {
        ActionBar ab = ((HiveActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }
    }

    @Override
    public boolean checkCameraPermission() {
        // Check camera permission
        if (PermissionUtils.isGranted(getActivity(), PermissionEnum.CAMERA)) {
            return true;
        }
        // Ask for permission
        PermissionManager.Builder()
                .permission(PermissionEnum.CAMERA)
                .askAgain(true)
                .askAgainCallback(new AskAgainCallback() {
                    @Override
                    public void showRequestPermission(final UserResponse response) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.permission_request_title))
                                .setMessage(getString(R.string.camera_permission_request_body))
                                .setPositiveButton(getString(R.string.permission_request_allow_button),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                response.result(true);
                                            }
                                        })
                                .setNegativeButton(getString(R.string.permission_request_deny_button),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
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
                        if (allPermissionsGranted) {
                            // Launch the feature
                            presenter.startNewRecording();
                        } else {
                            // Warn the user that it's not possible to use the feature
                            Toast.makeText(getActivity(), getString(R.string.permission_request_denied),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .ask(getActivity());
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void setPresenter(@NonNull HiveContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public int getTabName() {
        return R.string.hive_recordings_tab;
    }

    @Override
    public void onRecordingClick(Recording clickedRecording) {
        presenter.openRecordingsDetail(clickedRecording);
    }

    @Override
    public void onRecordingDelete(Recording clickedRecording) {
        presenter.deleteRecording(clickedRecording);
    }

    @Override
    public void onOpenMenuClick(View view) {
        getActivity().openContextMenu(view);
    }

    /**
     * Shows no recordings views.
     */
    private void showNoRecordingsViews() {
        hivesView.setVisibility(View.GONE);
        noRecordingsView.setVisibility(View.VISIBLE);
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
}
