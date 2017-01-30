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

package com.davidmiguel.gobees.about;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display about GoBees info and open source licenses.
 */
public class AboutFragment extends Fragment
        implements AboutContract.View, LibsAdapter.LibItemListener {

    private AboutContract.Presenter presenter;
    private LibsAdapter libsAdapter;

    private TextView versionTv;

    public AboutFragment() {
        // Requires empty public constructor
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.about_frag, container, false);

        versionTv = (TextView) root.findViewById(R.id.version);
        Button websiteBtn = (Button) root.findViewById(R.id.website_btn);
        Button licenseBtn = (Button) root.findViewById(R.id.license_btn);
        Button changelogBtn = (Button) root.findViewById(R.id.changelog_btn);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        // Set btn listeners
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onWebsiteClicked();
            }
        });
        licenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLicenseClicked(Library.License.GPL3);
            }
        });
        changelogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onChangelogClicked();
            }
        });

        // Set libs list
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(libsAdapter);


        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libsAdapter = new LibsAdapter(new ArrayList<Library>(0), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull AboutContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showVersion(String version) {
        versionTv.setText(getString(R.string.version) + " " + version);
    }

    @Override
    public void openWebsite(int url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(url)));
        startActivity(intent);
    }

    @Override
    public void openChangelog(int title, int changelog) {
        showDialog(getString(title), getString(changelog));
    }

    @Override
    public void openLicence(String title, int license) {
        showDialog(title, getString(license));
    }

    @Override
    public void showLibraries(List<Library> libraries) {
        libsAdapter.replaceData(libraries);
    }

    /**
     * Shows a dialog with given data.
     *
     * @param title dialog title.
     * @param body  dialog text.
     */
    private void showDialog(String title, String body) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(AndroidUtils.fromHtml(body));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.close_btn),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onLibraryClicked(Library.License license) {
        presenter.onLicenseClicked(license);
    }
}
