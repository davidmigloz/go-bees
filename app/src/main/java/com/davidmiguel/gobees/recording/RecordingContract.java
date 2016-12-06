package com.davidmiguel.gobees.recording;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface RecordingContract {

    interface View extends BaseView<Presenter> {

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Show recording details.
         *
         * @param recording recording.
         */
        public void showRecording(@NonNull Recording recording);

    }

    interface Presenter extends BasePresenter {

        /**
         * Delete the recording records.
         */
        void deleteRecording();
    }
}
