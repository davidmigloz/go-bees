package com.davidmiguel.gobees.monitoring;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.BackClickHelperFragment;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Displays a feed from the camera processed by the CV algorithm.
 */
public class MonitoringFragment extends Fragment implements MonitoringContract.View,
        BackClickHelperFragment {

    public static final String ARGUMENT_HIVE_ID = "HIVE_ID";

    private MonitoringContract.Presenter presenter;

    private BaseLoaderCallback loaderCallback;
    private CameraBridgeViewBase cameraView;
    private TextView numBeesTV;
    private RelativeLayout settingsLayout;


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
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                        presenter.onOpenCvConnected();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };
        // Don't switch off screen
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Configure view
        cameraView = (JavaCameraView) root.findViewById(R.id.camera_view);
        cameraView.setCameraIndex(0);
        cameraView.setMaxFrameSize(640, 480);
        numBeesTV = (TextView) root.findViewById(R.id.num_bees);
        ImageView settingsIcon = (ImageView) root.findViewById(R.id.settings_icon);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openSettings();
            }
        });
        settingsLayout = (RelativeLayout) getActivity().findViewById(R.id.settings);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
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
        if (cameraView != null) {
            cameraView.disableView();
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
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, getContext(), loaderCallback);
        cameraView.setCvCameraViewListener(listener);
    }

    @Override
    public void enableCameraView() {
        cameraView.enableView();
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
    public boolean onBackPressed() {
        if (settingsLayout.getVisibility() == View.VISIBLE) {
            presenter.closeSettings();
            return false;
        }
        return true;
    }
}
