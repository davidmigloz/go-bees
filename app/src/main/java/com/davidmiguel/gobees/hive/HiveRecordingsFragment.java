package com.davidmiguel.gobees.hive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.addedithive.AddEditHiveActivity;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.monitoring.MonitoringActivity;
import com.davidmiguel.gobees.monitoring.MonitoringFragment;
import com.davidmiguel.gobees.recording.RecordingActivity;
import com.davidmiguel.gobees.recording.RecordingFragment;
import com.davidmiguel.gobees.utils.BaseTabFragment;
import com.davidmiguel.gobees.utils.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Dispaly a list of recordings.
 */
public class HiveRecordingsFragment extends Fragment
        implements BaseTabFragment, HiveContract.View, RecordingsAdapter.RecordingItemListener {

    public static final String ARGUMENT_HIVE_ID = "HIVE_ID";

    private HiveContract.Presenter presenter;
    private RecordingsAdapter listAdapter;
    private View noRecordingsView;
    private LinearLayout hivesView;

    public HiveRecordingsFragment() {
        // Requires empty public constructor
    }

    public static HiveRecordingsFragment newInstance() {
        return new HiveRecordingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new RecordingsAdapter(getContext(), new ArrayList<Recording>(0), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.hive_recordings_frag, container, false);

        // Set up recordings list view
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recordings_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(listAdapter);
        hivesView = (LinearLayout) root.findViewById(R.id.recordingsLL);

        // Set up  no recordings view
        noRecordingsView = root.findViewById(R.id.no_recordings);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_new_recording);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startNewRecording();
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadRecordings(false);
            }
        });

        // Listen menu options
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.hive_frag_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.menu_refresh:
                presenter.loadRecordings(true);
                break;
        }
        return true;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);
        // Make sure setRefreshing() is called after the layout is done with everything else
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showRecordings(@NonNull List<Recording> recordings) {
        listAdapter.replaceData(recordings);
        hivesView.setVisibility(View.VISIBLE);
        noRecordingsView.setVisibility(View.GONE);
    }

    @Override
    public void startNewRecording(long hiveId) {
        Intent intent = new Intent(getContext(), MonitoringActivity.class);
        intent.putExtra(MonitoringFragment.ARGUMENT_HIVE_ID, hiveId);
        getActivity().startActivity(intent);
    }

    @Override
    public void showRecordingDetail(long hiveId, Date date) {
        Intent intent = new Intent(getActivity(), RecordingActivity.class);
        intent.putExtra(RecordingFragment.ARGUMENT_HIVE_ID, hiveId);
        intent.putExtra(RecordingFragment.ARGUMENT_START_DATE, date.getTime());
        intent.putExtra(RecordingFragment.ARGUMENT_END_DATE, date.getTime());
        getActivity().startActivity(intent);
    }

    @Override
    public void showLoadingRecordingsError() {
        showMessage(getString(R.string.loading_recordings_error));
    }

    @Override
    public void showNoRecordings() {
        showNoRecordingsViews();
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_recording_message));
    }

    @Override
    public void showTitle(@NonNull String title) {
        ActionBar ab = ((HiveActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }
    }

    @Override
    public void setPresenter(@NonNull HiveContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public int getTabName() {
        return R.string.hive_recordings_tab;
    }

    @Override
    public void onRecordingClick(Recording clickedRecording) {
        presenter.openRecordingsDetail(clickedRecording);
    }

    @Override
    public void onRecordingDelete(Recording clickedRecording) {
        // TODO delete recording
    }

    /**
     * Shows no recordings views.
     */
    private void showNoRecordingsViews() {
        hivesView.setVisibility(View.GONE);
        noRecordingsView.setVisibility(View.VISIBLE);
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
