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
        void setLoadingIndicator(boolean active);

        void showApiaries(List<Apiary> apiaries);

        void showAddApiary();

        void showApiaryDetail(int apiaryId);

        void showLoadingApiariesError();

        void showNoApiaries();

        void showSuccessfullySavedMessage();
    }

    interface Presenter extends BasePresenter {
        void result(int requestCode, int resultCode);

        void loadApiaries(boolean forceUpdate);

        void addNewApiary();

        void openApiaryDetail(@NonNull Apiary requestedApiary);
    }
}
