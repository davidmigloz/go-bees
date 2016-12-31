package com.davidmiguel.gobees.apiaries;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ApiariesContract {

    interface View extends BaseView<Presenter> {

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Shows list of apiaries.
         *
         * @param apiaries apiaries to show (list cannot be empty).
         */
        void showApiaries(@NonNull List<Apiary> apiaries);

        /**
         * Notify that the apiaries data has changed to update the list.
         */
        void notifyApiariesUpdated();

        /**
         * Opens activity to add or edit an apiary.
         *
         * @param apiaryId apiary id (or -1 for creating a new one).
         */
        void showAddEditApiary(long apiaryId);

        /**
         * Opens activity to show the details of the given apiary.
         *
         * @param apiaryId apiary to show.
         */
        void showApiaryDetail(long apiaryId);

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

        /**
         * Shows successfully deleted message.
         */
        void showSuccessfullyDeletedMessage();

        /**
         * Shows error while deleting apiary message.
         */
        void showDeletedErrorMessage();

        /**
         * Shows successfully current weather updated message.
         */
        void showSuccessfullyWeatherUpdatedMessage();

        /**
         * Shows error while updating current weather message.
         */
        void showWeatherUpdateErrorMessage();
    }

    interface Presenter extends BasePresenter {

        /**
         * Shows a snackbar showing whether an apiary was successfully added or not.
         *
         * @param requestCode request code from the intent.
         * @param resultCode  result code from the intent.
         */
        void result(int requestCode, int resultCode);

        /**
         * Load apiaries from repository.
         *
         * @param forceUpdate force cache update.
         */
        void loadApiaries(boolean forceUpdate);

        /**
         * Orders to open activity to add or edit an apiary.
         *
         * @param apiaryId apiary id (or -1 for creating a new one).
         */
        void addEditApiary(long apiaryId);

        /**
         * Opens activity to show the details of the given apiary.
         *
         * @param requestedApiary apiary to show.
         */
        void openApiaryDetail(@NonNull Apiary requestedApiary);

        /**
         * Deletes given apiary.
         *
         * @param apiary apiary to delete.
         */
        void deleteApiary(@NonNull Apiary apiary);

        // TODO eliminar generar y eliminar datos
        void generateData();

        void deleteData();
    }
}
