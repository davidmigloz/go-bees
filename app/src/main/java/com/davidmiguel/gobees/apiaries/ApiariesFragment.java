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

package com.davidmiguel.gobees.apiaries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.addeditapiary.AddEditApiaryActivity;
import com.davidmiguel.gobees.addeditapiary.AddEditApiaryFragment;
import com.davidmiguel.gobees.apiaries.ApiariesAdapter.ApiaryItemListener;
import com.davidmiguel.gobees.apiary.ApiaryActivity;
import com.davidmiguel.gobees.apiary.ApiaryHivesFragment;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a list of apiaries.
 */
public class ApiariesFragment extends Fragment
        implements ApiariesContract.View, ApiaryItemListener {

    private ApiariesContract.LoadDataPresenter presenter;
    private ApiariesAdapter listAdapter;
    private View noApiariesView;
    private LinearLayout apiariesView;

    public ApiariesFragment() {
        // Requires empty public constructor
    }

    public static ApiariesFragment newInstance() {
        return new ApiariesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new ApiariesAdapter(getContext(), getActivity().getMenuInflater(),
                new ArrayList<Apiary>(0), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.apiaries_frag, container, false);

        // Set up apiaries list view
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.apiaries_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(listAdapter);
        apiariesView = (LinearLayout) root.findViewById(R.id.apiariesLL);

        // Set up  no apiaries view
        noApiariesView = root.findViewById(R.id.no_apiaries);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_apiary);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addEditApiary(AddEditApiaryActivity.NEW_APIARY);
            }
        });
        fab.setVisibility(View.VISIBLE);

        // Set up progress indicator
        AndroidUtils.setUpProgressIndicator(root, getContext(), recyclerView, presenter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        AndroidUtils.setLoadingIndicator(getView(), active);
    }

    @Override
    public void showApiaries(@NonNull List<Apiary> apiaries) {
        listAdapter.replaceData(apiaries);
        apiariesView.setVisibility(View.VISIBLE);
        noApiariesView.setVisibility(View.GONE);
    }

    @Override
    public void notifyApiariesUpdated() {
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddEditApiary(long apiaryId) {
        Intent intent = new Intent(getContext(), AddEditApiaryActivity.class);
        if (apiaryId != AddEditApiaryActivity.NEW_APIARY) {
            intent.putExtra(AddEditApiaryFragment.ARGUMENT_EDIT_APIARY_ID, apiaryId);
        }
        startActivityForResult(intent, AddEditApiaryActivity.REQUEST_ADD_APIARY);
    }

    @Override
    public void showApiaryDetail(long apiaryId) {
        Intent intent = new Intent(getActivity(), ApiaryActivity.class);
        intent.putExtra(ApiaryHivesFragment.ARGUMENT_APIARY_ID, apiaryId);
        getActivity().startActivity(intent);
    }

    @Override
    public void showLoadingApiariesError() {
        showMessage(getString(R.string.loading_apiaries_error));
    }

    @Override
    public void showNoApiaries() {
        showNoApiariesViews();
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_apiary_message));
    }

    @Override
    public void showSuccessfullyDeletedMessage() {
        showMessage(getString(R.string.successfully_deleted_apiary_message));
    }

    @Override
    public void showDeletedErrorMessage() {
        showMessage(getString(R.string.deleted_apiary_error_message));
    }

    @Override
    public void showWeatherUpdateErrorMessage() {
        Toast.makeText(getActivity(), getString(R.string.weather_update_error_message),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(@NonNull ApiariesContract.LoadDataPresenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onApiaryClick(Apiary apiary) {
        presenter.openApiaryDetail(apiary);
    }

    @Override
    public void onApiaryDelete(Apiary apiary) {
        presenter.deleteApiary(apiary);
    }

    @Override
    public void onApiaryEdit(Apiary apiary) {
        presenter.addEditApiary(apiary.getId());
    }

    @Override
    public void onOpenMenuClick(View view) {
        getActivity().openContextMenu(view);
    }

    /**
     * Shows no apiaries views.
     */
    private void showNoApiariesViews() {
        apiariesView.setVisibility(View.GONE);
        noApiariesView.setVisibility(View.VISIBLE);
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