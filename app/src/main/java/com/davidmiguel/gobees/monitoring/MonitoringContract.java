package com.davidmiguel.gobees.monitoring;

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import org.opencv.android.CameraBridgeViewBase;

/**
 * This specifies the contract between the view and the presenter.
 */
interface MonitoringContract {

    interface View extends BaseView<MonitoringContract.Presenter> {

        void initOpenCV(CameraBridgeViewBase.CvCameraViewListener2 listener);

        void enableCameraView();

        void setNumBees(int numBees);

    }

    interface Presenter extends BasePresenter {

        void onOpenCvConnected();

    }
}
