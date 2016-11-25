package com.davidmiguel.gobees.apiaries;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ApiariesContract {

    interface View extends BaseView<Presenter> {

        /**
         * Displays or hide loading indicator.
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Shows list of apiaries.
         * @param apiaries apiaries to show (list cannot be empty).
         */
        void showApiaries(@NonNull List<Apiary> apiaries);

        /**
         * Opens activity to add or edit an apiary.
         */
        void showAddEditApiary();

        /**
         * Opens activity to show the details of the given apiary.
         * @param apiaryId apiary to show.
         */
        void showApiaryDetail(int apiaryId);

        /**
         * Shows loading apiaries error message.
         */
        void showLoadingApiariesError();

        /**
         * Makes visible the no apiaries view.
         */
        void showNoApiaries();

        /**
         * Shows successfully saved message.
         */
        void showSuccessfullySavedMessage();
    }

    interface Presenter extends BasePresenter {

        /**
         * Shows a snackbar showing whether an apiary was successfully added or not.
         * @param requestCode request code from the intent.
         * @param resultCode result code from the intent.
         */
        void result(int requestCode, int resultCode);

        /**
         * Load apiaries from repository.
         * @param forceUpdate force cache update.
         */
        void loadApiaries(boolean forceUpdate);

        /**
         * Orders to open activity to add or edit an apiary.
         */
        void addEditApiary();

        /**
         * Opens activity to show the details of the given apiary.
         * @param requestedApiary apiary to show.
         */
        void openApiaryDetail(@NonNull Apiary requestedApiary);
    }
}
