package com.davidmiguel.gobees.apiaries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.apiaries.ApiariesAdapter.ApiaryItemListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a list of apiaries.
 */
public class ApiariesFragment extends Fragment implements ApiariesContract.View {

    private ApiariesContract.Presenter presenter;
    private ApiariesAdapter listAdapter;
    private View noApiariesView;
    private ImageView noApiariesIcon;
    private TextView noApiarieskTextView;
    private TextView noApiariesAddView;
    private LinearLayout apiarieView;
    ApiaryItemListener itemListener = new ApiaryItemListener() {
        @Override
        public void onApiaryClick(Apiary clickedApiary) {
            presenter.openApiaryDetail(clickedApiary);
        }
    };

    public ApiariesFragment() {
        // Requires empty public constructor
    }

    public static ApiariesFragment newInstance() {
        return new ApiariesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new ApiariesAdapter(new ArrayList<Apiary>(0), itemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.apiaries_frag, container, false);

        // Set up tasks view
        ListView listView = (ListView) root.findViewById(R.id.apiaries_list);
        listView.setAdapter(listAdapter);
        apiarieView = (LinearLayout) root.findViewById(R.id.apiariesLL);

        // Set up  no tasks view
        noApiariesView = root.findViewById(R.id.no_apiaries);
        noApiariesIcon = (ImageView) root.findViewById(R.id.no_apiaries_icon);
        noApiarieskTextView = (TextView) root.findViewById(R.id.no_apiaries_text);
        noApiariesAddView = (TextView) root.findViewById(R.id.no_apiaries_add);
        noApiariesAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddApiary();
            }
        });

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_apiary);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addNewApiary();
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
        swipeRefreshLayout.setScrollUpChild(listView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadApiaries(false);
            }
        });

        // Listen menu options
        setHasOptionsMenu(true);

        // Load data
        presenter.loadApiaries(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.apiaries_frag_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                presenter.loadApiaries(true);
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
        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showApiaries(List<Apiary> apiaries) {
        listAdapter.replaceData(apiaries);
        apiarieView.setVisibility(View.VISIBLE);
        noApiariesView.setVisibility(View.GONE);
    }

    @Override
    public void showAddApiary() {

    }

    @Override
    public void showApiaryDetail(int apiaryId) {

    }

    @Override
    public void showLoadingApiariesError() {
        showMessage(getString(R.string.loading_apiaries_error));
    }

    @Override
    public void showNoApiaries() {
        showNoApiariesViews(
                getResources().getString(R.string.no_apiaries),
                R.drawable.ic_add_circle_outline,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(@NonNull ApiariesContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    private void showNoApiariesViews(String mainText, int iconRes, boolean showAddView) {
        apiarieView.setVisibility(View.GONE);
        noApiariesView.setVisibility(View.VISIBLE);
        // Set details
        noApiarieskTextView.setText(mainText);
        noApiariesIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), iconRes, null));
        noApiariesAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @SuppressWarnings("ConstantConditions")
    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}