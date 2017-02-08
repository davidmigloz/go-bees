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

package com.davidmiguel.gobees.hive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseTabFragment;
import com.google.common.base.Strings;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Displays hive info.
 */
public class HiveInfoFragment extends Fragment
        implements BaseTabFragment, HiveContract.HiveInfoView {

    private HiveContract.Presenter presenter;
    private FloatingActionButton fab;
    private TextView lastRevision;
    private TextView notes;

    public static HiveInfoFragment newInstance() {
        return new HiveInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.hive_info_frag, container, false);

        // Set up view
        LinearLayout info = (LinearLayout) root.findViewById(R.id.info);
        lastRevision = (TextView) root.findViewById(R.id.last_revision);
        notes = (TextView) root.findViewById(R.id.notes_content);

        // Set up floating action button
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_new_recording);

        // Set up progress indicator
        AndroidUtils.setUpProgressIndicator(root, getContext(), info, presenter);

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
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public int getTabName() {
        return R.string.hive_info_tab;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        AndroidUtils.setLoadingIndicator(getView(), active);
    }

    @Override
    public void showInfo(Hive hive) {
        // Last revision
        lastRevision.setText(DateUtils.getRelativeTimeSpanString(hive.getLastRevision().getTime(),
                (new Date()).getTime(), DateUtils.MINUTE_IN_MILLIS));
        // Notes
        if (Strings.isNullOrEmpty(hive.getNotes())) {
            notes.setText(getString(R.string.no_notes));
        } else {
            notes.setText(hive.getNotes());
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
}
