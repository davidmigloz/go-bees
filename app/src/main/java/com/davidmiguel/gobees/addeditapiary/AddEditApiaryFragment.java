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

package com.davidmiguel.gobees.addeditapiary;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;

import java.util.Locale;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;
import rebus.permissionutils.SimpleCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display add apiary form.
 */
public class AddEditApiaryFragment extends Fragment implements AddEditApiaryContract.View {

    public static final String ARGUMENT_EDIT_APIARY_ID = "EDIT_APIARY_ID";

    private AddEditApiaryContract.Presenter presenter;

    private EditText nameEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private ImageView getLocationIcon;
    private TextView locationPrecisionTextView;
    private EditText notesEditText;

    public AddEditApiaryFragment() {
        // Requires empty public constructor
    }

    public static AddEditApiaryFragment newInstance() {
        return new AddEditApiaryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addeditapiary_frag, container, false);
        nameEditText = (EditText) root.findViewById(R.id.add_apiary_name);
        latitudeEditText = (EditText) root.findViewById(R.id.add_apiary_latitude);
        longitudeEditText = (EditText) root.findViewById(R.id.add_apiary_longitude);
        getLocationIcon = (ImageView) root.findViewById(R.id.get_location_icon);
        locationPrecisionTextView = (TextView) root.findViewById(R.id.location_precision);
        notesEditText = (EditText) root.findViewById(R.id.add_apiary_notes);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Configure get location icon
        getLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toogleLocation();
            }
        });

        // Configure floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_apiary);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.save(nameEditText.getText().toString(),
                        latitudeEditText.getText().toString(),
                        longitudeEditText.getText().toString(),
                        notesEditText.getText().toString());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setName(String name) {
        nameEditText.setText(name);
    }

    @Override
    public void setLocation(Location location) {
        latitudeEditText.setText(String.valueOf(location.getLatitude()));
        longitudeEditText.setText(String.valueOf(location.getLongitude()));
        long precision = Math.round(location.getAccuracy());
        if (precision != 0) {
            locationPrecisionTextView.setText(String.format(
                    Locale.getDefault(), "±%dm", precision));
        } else {
            locationPrecisionTextView.setText("");
        }
    }

    @Override
    public void setNotes(String notes) {
        notesEditText.setText(notes);
    }

    @Override
    public void setLocationIcon(boolean active) {
        getLocationIcon.setColorFilter(active
                ? ContextCompat.getColor(getContext(), R.color.colorPrimaryDark) :
                ContextCompat.getColor(getContext(), R.color.colorAccent));
        locationPrecisionTextView.setText("");
    }

    @Override
    public void showApiariesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showSearchingGpsMsg() {
        Toast.makeText(getContext(), getString(R.string.searching_gps_message),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyApiaryError() {
        showMessage(nameEditText, getString(R.string.empty_apiary_message));
    }

    @Override
    public void showInvalidLocationError() {
        showMessage(nameEditText, getString(R.string.invalid_location_message));
    }

    @Override
    public void showGpsConnectionError() {
        showMessage(nameEditText, getString(R.string.gps_error_message));
    }

    @Override
    public void showSaveApiaryError() {
        showMessage(getView(), getString(R.string.save_apiary_error_message));
    }

    @Override
    public boolean checkLocationPermission() {
        // Check location permission
        if (PermissionUtils.isGranted(getActivity(), PermissionEnum.ACCESS_FINE_LOCATION)) {
            return true;
        }
        // Ask for permission
        PermissionManager.with(getActivity())
                .permission(PermissionEnum.ACCESS_FINE_LOCATION)
                .askagain(true)
                .askagainCallback(new AskagainCallback() {
                    @Override
                    public void showRequestPermission(final UserResponse response) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.permission_request_title))
                                .setMessage(getString(R.string.location_permission_request_body))
                                .setPositiveButton(
                                        getString(R.string.permission_request_allow_button),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface,
                                                                int which) {
                                                response.result(true);
                                            }
                                        })
                                .setNegativeButton(
                                        getString(R.string.permission_request_deny_button),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface,
                                                                int which) {
                                                response.result(false);
                                            }
                                        })
                                .setCancelable(false)
                                .show();
                    }
                })
                .callback(new SimpleCallback() {
                    @Override
                    public void result(boolean allPermissionsGranted) {
                        if (allPermissionsGranted) {
                            // Launch the feature
                            presenter.toogleLocation();
                        } else {
                            // Warn the user that it's not possible to use the feature
                            Toast.makeText(getActivity(),
                                    getString(R.string.permission_request_denied),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .ask();
        return false;
    }

    @Override
    public void closeKeyboard() {
        AndroidUtils.closeKeyboard(getActivity());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(@NonNull AddEditApiaryContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    /**
     * Shows a snackbar with the given message.
     *
     * @param view    view.
     * @param message message to show.
     */
    @SuppressWarnings("ConstantConditions")
    private void showMessage(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
