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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.BaseTabFragment;
import com.davidmiguel.gobees.utils.ScrollChildSwipeRefreshLayout;
import com.google.common.base.Strings;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display apiary info.
 * TODO
 */
public class ApiaryInfoFragment extends Fragment
        implements BaseTabFragment, ApiaryContract.ApiaryInfoView {

    private ApiaryContract.Presenter presenter;
    private FloatingActionButton fab;
    private TextView location;
    private TextView numHives;
    private TextView lastRevision;
    private TextView notes;

    public static ApiaryInfoFragment newInstance() {
        return new ApiaryInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.apiary_info_frag, container, false);

        // Set up view
        LinearLayout info = (LinearLayout) root.findViewById(R.id.info);
        location = (TextView) root.findViewById(R.id.location);
        numHives = (TextView) root.findViewById(R.id.num_hives);
        lastRevision = (TextView) root.findViewById(R.id.last_revision);
        notes = (TextView) root.findViewById(R.id.notes_content);

        // Set up floating action button
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_hive);

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(info);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(false);
            }
        });
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public int getTabName() {
        return R.string.apiary_info_tab;
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showInfo(Apiary apiary, Date lastRevisionDate) {
        // Location
        String latLetter = (apiary.getLocationLat() > 0) ? "N" : "S";
        String lonLetter = (apiary.getLocationLong() > 0) ? "E" : "W";
        location.setText(apiary.getLocationLat() + latLetter + ", "
                + apiary.getLocationLong() + lonLetter);
        // Num hives
        int num = apiary.getHives().size();
        numHives.setText(getResources().getQuantityString(R.plurals.num_hives_plurals, num, num));
        // Last revision
        lastRevision.setText(DateUtils.getRelativeTimeSpanString(lastRevisionDate.getTime(),
                (new Date()).getTime(), DateUtils.MINUTE_IN_MILLIS));
        // Notes
        if (Strings.isNullOrEmpty(apiary.getNotes())) {
            notes.setText(getString(R.string.no_notes));
        } else {
            notes.setText(apiary.getNotes());
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
}
