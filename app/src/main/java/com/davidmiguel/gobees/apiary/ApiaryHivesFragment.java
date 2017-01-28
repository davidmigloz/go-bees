/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.davidmiguel.gobees.apiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.addedithive.AddEditHiveActivity;
import com.davidmiguel.gobees.addedithive.AddEditHiveFragment;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.hive.HiveActivity;
import com.davidmiguel.gobees.hive.HiveRecordingsFragment;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseTabFragment;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a list of hives.
 */
public class ApiaryHivesFragment extends Fragment
        implements BaseTabFragment, ApiaryContract.ApiaryHivesView, HivesAdapter.HiveItemListener {

    public static final String ARGUMENT_APIARY_ID = "APIARY_ID";

    private ApiaryContract.Presenter presenter;
    private HivesAdapter listAdapter;
    private View noHivesView;
    private LinearLayout hivesView;
    private FloatingActionButton fab;

    /**
     * Get ApiaryHivesFragment instance.
     *
     * @param apiaryId apiary id.
     * @return ApiaryHivesFragment instance.
     */
    public static ApiaryHivesFragment newInstance(long apiaryId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARGUMENT_APIARY_ID, apiaryId);
        ApiaryHivesFragment fragment = new ApiaryHivesFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new HivesAdapter(getActivity().getMenuInflater(),
                new ArrayList<Hive>(0), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.apiary_hives_frag, container, false);

        // Set up hives list view
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.hives_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(listAdapter);
        hivesView = (LinearLayout) root.findViewById(R.id.hivesLL);

        // Set up  no apiaries view
        noHivesView = root.findViewById(R.id.no_hives);

        // Set up floating action button
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_hive);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addEditHive(AddEditHiveActivity.NEW_HIVE);
            }
        });
        fab.setVisibility(View.VISIBLE);

        // Configure progress indicator
        AndroidUtils.setUpProgressIndicator(root, getContext(), recyclerView, presenter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public int getTabName() {
        return R.string.apiary_hives_tab;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        AndroidUtils.setLoadingIndicator(getView(), active);
    }

    @Override
    public void showHives(@NonNull List<Hive> hives) {
        listAdapter.replaceData(hives);
        hivesView.setVisibility(View.VISIBLE);
        noHivesView.setVisibility(View.GONE);
    }

    @Override
    public void showAddEditHive(long apiaryId, long hiveId) {
        Intent intent = new Intent(getContext(), AddEditHiveActivity.class);
        intent.putExtra(AddEditHiveFragment.ARGUMENT_EDIT_APIARY_ID, apiaryId);
        if (hiveId != AddEditHiveActivity.NEW_HIVE) {
            intent.putExtra(AddEditHiveFragment.ARGUMENT_EDIT_HIVE_ID, hiveId);
        }
        startActivityForResult(intent, AddEditHiveActivity.REQUEST_ADD_HIVE);
    }

    @Override
    public void showHiveDetail(long apiaryId, long hiveId) {
        Intent intent = new Intent(getActivity(), HiveActivity.class);
        intent.putExtra(HiveRecordingsFragment.ARGUMENT_APIARY_ID, apiaryId);
        intent.putExtra(HiveRecordingsFragment.ARGUMENT_HIVE_ID, hiveId);
        getActivity().startActivity(intent);
    }

    @Override
    public void showLoadingHivesError() {
        showMessage(getString(R.string.loading_hives_error));
    }

    @Override
    public void showNoHives() {
        showNoHivesViews();
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_hive_message));
    }

    @Override
    public void showSuccessfullyDeletedMessage() {
        showMessage(getString(R.string.successfully_deleted_hive_message));
    }

    @Override
    public void showDeletedErrorMessage() {
        showMessage(getString(R.string.deleted_hive_error_message));
    }

    @Override
    public void showTitle(@NonNull String title) {
        ActionBar ab = ((ApiaryActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }
    }

    @Override
    public void setPresenter(@NonNull ApiaryContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onHiveClick(Hive hive) {
        presenter.openHiveDetail(hive);
    }

    @Override
    public void onHiveDelete(Hive hive) {
        presenter.deleteHive(hive);
    }

    @Override
    public void onHiveEdit(Hive hive) {
        presenter.addEditHive(hive.getId());
    }

    @Override
    public void onOpenMenuClick(View view) {
        getActivity().openContextMenu(view);
    }

    /**
     * Shows no hives views.
     */
    private void showNoHivesViews() {
        hivesView.setVisibility(View.GONE);
        noHivesView.setVisibility(View.VISIBLE);
    }

    /**
     * Shows a snackbar with the given message.
     *
     * @param message message to show.
     */
    @SuppressWarnings("ConstantConditions")
    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
