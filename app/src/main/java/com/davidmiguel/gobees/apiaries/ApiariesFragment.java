package com.davidmiguel.gobees.apiaries;

import com.davidmiguel.gobees.data.model.Apiary;

import java.util.List;

/**
 * Display a list of apiaries.
 */
public class ApiariesFragment implements ApiariesContract.View {
    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showApiaries(List<Apiary> apiaries) {

    }

    @Override
    public void showAddApiary() {

    }

    @Override
    public void showApiaryDetail(int apiaryId) {

    }

    @Override
    public void showLoadingApiariesError() {

    }

    @Override
    public void showNoApiaries() {

    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setPresenter(ApiariesContract.Presenter presenter) {

    }
}
