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

package com.davidmiguel.gobees.monitoring;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.hive.HiveRecordingsFragment;
import com.davidmiguel.gobees.monitoring.MonitoringService.MonitoringBinder;
import com.davidmiguel.gobees.monitoring.camera.CameraView;
import com.davidmiguel.gobees.utils.BackClickHelperFragment;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Displays a feed from the camera processed by the CV algorithm.
 */
public class MonitoringFragment extends Fragment implements MonitoringContract.View,
        BackClickHelperFragment {

    public static final String ARGUMENT_APIARY_ID = "APIARY_ID";
    public static final String ARGUMENT_HIVE_ID = "HIVE_ID";

    private static final int MAX_HEIGHT = 480;
    private static final int MAX_WIDTH = 640;

    private MonitoringContract.Presenter presenter;

    private BaseLoaderCallback loaderCallback;
    private CameraView cameraView;
    private TextView numBeesTV;
    private RelativeLayout settingsLayout;
    private ImageView settingsIcon;
    private ImageView recordIcon;
    private Chronometer chronometer;

    private MonitoringService mService;
    private ServiceConnection mConnection;


    public MonitoringFragment() {
        // Requires empty public constructor
    }

    public static MonitoringFragment newInstance() {
        return new MonitoringFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.monitoring_frag, container, false);

        // Configure OpenCV callback
        loaderCallback = new BaseLoaderCallback(getContext()) {
            @Override
            public void onManagerConnected(final int status) {
                if (status == LoaderCallbackInterface.SUCCESS) {
                    presenter.onOpenCvConnected();
                } else {
                    super.onManagerConnected(status);
                }
            }
        };

        // Don't switch off screen
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Configure camera
        cameraView = (CameraView) root.findViewById(R.id.camera_view);
        cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
        cameraView.setMaxFrameSize(MAX_WIDTH, MAX_HEIGHT);
        // Configure view
        numBeesTV = (TextView) root.findViewById(R.id.num_bees);
        settingsLayout = (RelativeLayout) getActivity().findViewById(R.id.settings);
        chronometer = (Chronometer) root.findViewById(R.id.chronometer);

        // Configure icons
        settingsIcon = (ImageView) root.findViewById(R.id.settings_icon);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openSettings();
            }
        });
        recordIcon = (ImageView) root.findViewById(R.id.record_icon);
        recordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.startMonitoring();
            }
        });

        // Configure service connection
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                MonitoringBinder binder = (MonitoringBinder) service;
                mService = binder.getService(new GoBeesDataSource.SaveRecordingCallback() {
                    @Override
                    public void onRecordingTooShort() {
                        // Finish activity with error
                        Intent intent = new Intent();
                        intent.putExtra(HiveRecordingsFragment.ARGUMENT_MONITORING_ERROR,
                                HiveRecordingsFragment.ERROR_RECORDING_TOO_SHORT);
                        getActivity().setResult(Activity.RESULT_CANCELED, intent);
                        getActivity().finish();

                    }

                    @Override
                    public void onSuccess() {
                        // Finish activity with OK
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure() {
                        // Finish activity with error
                        Intent intent = new Intent();
                        intent.putExtra(HiveRecordingsFragment.ARGUMENT_MONITORING_ERROR,
                                HiveRecordingsFragment.ERROR_SAVING_RECORDING);
                        getActivity().setResult(Activity.RESULT_CANCELED, intent);
                        getActivity().finish();
                    }
                });
                // Set chronometer
                chronometer.setBase(mService.getStartTime());
                chronometer.start();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
            }
        };
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start(MonitoringService.isRunning());
    }

    @Override
    public void onPause() {
        if (cameraView != null) {
            cameraView.disableView();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Disable camera view
        if (cameraView != null) {
            cameraView.disableView();
        }
        // Unbind service
        if (mService != null) {
            getActivity().unbindService(mConnection);
        }
        super.onDestroy();
    }

    @Override
    public void setPresenter(@NonNull MonitoringContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void initOpenCV(CvCameraViewListener2 listener) {
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, getContext(), loaderCallback);
        cameraView.setCvCameraViewListener(listener);
    }

    @Override
    public void enableCameraView() {
        cameraView.enableView();
    }

    @Override
    public void updateAlgoZoom(int ratio) {
        cameraView.setZoom(ratio);
    }

    @Override
    public void setNumBees(final int numBees) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                numBeesTV.setText(Integer.toString(numBees));
            }
        });
    }

    @Override
    public void startMonitoringgService(MonitoringSettings ms) {
        // Start service
        Intent intent = new Intent(getActivity(), MonitoringService.class);
        intent.setAction(MonitoringService.START_ACTION);
        intent.putExtra(MonitoringService.ARGUMENT_MON_SETTINGS, ms);
        getActivity().startService(intent);
    }

    @Override
    public void stopMonitoringService() {
        // Stop service
        Intent stopIntent = new Intent(getActivity(), MonitoringService.class);
        stopIntent.setAction(MonitoringService.STOP_ACTION);
        getActivity().startService(stopIntent);
    }

    @Override
    public void bindMonitoringService() {
        Intent intent = new Intent(getActivity(), MonitoringService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void hideCameraView() {
        // Hide camera view
        if (cameraView != null) {
            cameraView.disableView();
            cameraView.setVisibility(View.GONE);
            loaderCallback = null;
        }
        // Hide controls
        numBeesTV.setVisibility(View.GONE);
        settingsIcon.setVisibility(View.GONE);
        recordIcon.setOnClickListener(null);

    }

    @Override
    public void showMonitoringView() {
        // Show red stop button
        recordIcon.setImageResource(R.drawable.ic_stop);
        recordIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorRecordIcon));
        // Stop listener
        recordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop recording service
                presenter.stopMonitoring();
            }
        });
    }

    @Override
    public void showNumBeesView(boolean active) {
        if (active) {
            numBeesTV.setVisibility(View.VISIBLE);
        } else {
            numBeesTV.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (settingsLayout.getVisibility() == View.VISIBLE) {
            presenter.closeSettings();
            return false;
        }
        return true;
    }
}
