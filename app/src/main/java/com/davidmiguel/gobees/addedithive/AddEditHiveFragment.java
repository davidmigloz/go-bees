package com.davidmiguel.gobees.addedithive;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.davidmiguel.gobees.addeditapiary.AddEditApiaryFragment;

/**
 * Display add hive form.
 */
public class AddEditHiveFragment extends Fragment implements AddEditHiveContract.View {

    public AddEditHiveFragment() {
        // Requires empty public constructor
    }

    public static AddEditHiveFragment newInstance() {
        return new AddEditHiveFragment();
    }

    @Override
    public void showEmptyHiveError() {

    }

    @Override
    public void showSaveApiaryError() {

    }

    @Override
    public void showHivesList() {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setNotes(String notes) {

    }

    @Override
    public void setPresenter(@NonNull AddEditHiveContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
