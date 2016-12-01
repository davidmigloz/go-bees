package com.davidmiguel.gobees.premonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.video.BeesCounter;
import com.davidmiguel.gobees.video.ContourBeesCounter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

/**
 * TODO refactor to MPV.
 */
public class PreMonitoringActivity extends AppCompatActivity implements CvCameraViewListener2 {

    public static final int REQUEST_RECORD_HIVE = 1;

    private static final String TAG = "PreMonitoringActivity";

    // Bees counter
    private BeesCounter bc;
    // Camera view
    private CameraBridgeViewBase cameraView;
    // Processed frame
    private Mat processedFrame;
    // Num bees textview
    private TextView numBeesTV;
    // OpenCV loader callback
    private BaseLoaderCallback loaderCallback =
            new BaseLoaderCallback(this) {
                @Override
                public void onManagerConnected(final int status) {
                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS:
                            Log.d(TAG, "OpenCV loaded successfully");
                            cameraView.enableView();
                            break;
                        default:
                            super.onManagerConnected(status);
                            break;
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Don't switch off screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Configure view
        cameraView = (JavaCameraView) findViewById(R.id.camera_view);
        cameraView.setCameraIndex(0);
        cameraView.setMaxFrameSize(640, 480);
        cameraView.setCvCameraViewListener(this);
        numBeesTV = (TextView) findViewById(R.id.num_bees);
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, loaderCallback);
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
    public void onCameraViewStarted(int width, int height) {
        bc = new ContourBeesCounter();
        processedFrame = new Mat();
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        int numBees = bc.countBees(inputFrame.gray());
        setNumBees(numBees);
        bc.getProcessedFrame().copyTo(processedFrame);
        bc.getProcessedFrame().release();
        return processedFrame;
    }

    private void setNumBees(final int n) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                numBeesTV.setText(Integer.toString(n));
            }
        });
    }
}
