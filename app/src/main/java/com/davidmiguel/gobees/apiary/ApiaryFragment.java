package com.davidmiguel.gobees.apiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.addedithive.AddEditHiveActivity;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.utils.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a list of hives.
 */
public class ApiaryFragment extends Fragment
        implements ApiaryContract.View, ApiaryAdapter.HiveItemListener {

    public static final String ARGUMENT_APIARY_ID = "APIARY_ID";

    private ApiaryContract.Presenter presenter;
    private ApiaryAdapter listAdapter;
    private View noHivesView;
    private ImageView noHivesIcon;
    private TextView noHivesTextView;
    private TextView noHivesAddView;
    private LinearLayout hivesView;

    public ApiaryFragment() {
        // Requires empty public constructor
    }

    public static ApiaryFragment newInstance(long apiaryId) {
        Bundle bundle = new Bundle();
        bundle.putString(ApiaryFragment.ARGUMENT_APIARY_ID, apiaryId + "");
        ApiaryFragment apiaryFragment = new ApiaryFragment();
        apiaryFragment.setArguments(bundle);
        return apiaryFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new ApiaryAdapter(new ArrayList<Hive>(0), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.apiary_frag, container, false);

        // Set up hives list view
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.hives_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(listAdapter);
        hivesView = (LinearLayout) root.findViewById(R.id.hivesLL);

        // Set up  no apiaries view
        noHivesView = root.findViewById(R.id.no_hives);
        noHivesIcon = (ImageView) root.findViewById(R.id.no_hives_icon);
        noHivesTextView = (TextView) root.findViewById(R.id.no_hives_text);
        noHivesAddView = (TextView) root.findViewById(R.id.no_hives_add);
        noHivesAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEditHive();
            }
        });

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_hive);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addEditHive();
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
                presenter.loadHives(false);
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
        inflater.inflate(R.menu.hives_frag_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.menu_refresh:
                presenter.loadHives(true);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
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
    public void showHives(@NonNull List<Hive> hives) {
        listAdapter.replaceData(hives);
        hivesView.setVisibility(View.VISIBLE);
        noHivesView.setVisibility(View.GONE);
    }

    @Override
    public void showAddEditHive() {
        Intent intent = new Intent(getContext(), AddEditHiveActivity.class);
        startActivityForResult(intent, AddEditHiveActivity.REQUEST_ADD_HIVE);
    }

    @Override
    public void showHiveDetail(long hiveId) {
        // TODO
    }

    @Override
    public void showLoadingHivesError() {
        showMessage(getString(R.string.loading_hives_error));
    }

    @Override
    public void showNoHives() {
        showNoHivesViews(
                getResources().getString(R.string.no_hives),
                R.drawable.ic_add_circle_outline,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_hive_message));
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
    public void onHiveClick(Hive clickedHive) {
        presenter.openHiveDetail(clickedHive);
    }

    @Override
    public void onHiveDelete(Hive clickedHive) {
        // TODO delete hive
    }

    /**
     * Shows no hives views.
     *
     * @param mainText    text to show.
     * @param iconRes     icon to show.
     * @param showAddView whether show add view option or not.
     */
    private void showNoHivesViews(String mainText, int iconRes, boolean showAddView) {
        hivesView.setVisibility(View.GONE);
        noHivesView.setVisibility(View.VISIBLE);
        // Set details
        noHivesTextView.setText(mainText);
        noHivesIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), iconRes, null));
        noHivesAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
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
