package com.davidmiguel.gobees.hives;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidmiguel.gobees.data.model.Hive;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a list of hives.
 */
public class HivesFragment extends Fragment
        implements HivesContract.View {

    private HivesContract.Presenter presenter;

    public HivesFragment() {
        // Requires empty public constructor
    }

    public static HivesFragment newInstance() {
        return new HivesFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showHives(@NonNull List<Hive> hives) {

    }

    @Override
    public void showAddEditHive() {

    }

    @Override
    public void showHiveDetail(int hiveId) {

    }

    @Override
    public void showLoadingHivesError() {

    }

    @Override
    public void showNoHives() {

    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public void setPresenter(@NonNull HivesContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
