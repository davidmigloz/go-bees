package com.davidmiguel.gobees.addeditapiary;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davidmiguel.gobees.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display add apiary form.
 */
public class AddEditApiaryFragment extends Fragment implements AddEditApiaryContract.View {

    public static final String ARGUMENT_EDIT_APIARY_ID = "EDIT_APIARY_ID";

    private AddEditApiaryContract.Presenter presenter;

    private TextView nameTextView;
    private TextView locationTextView;
    private ImageView getLocationIcon;
    private TextView notesTextView;

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
        nameTextView = (TextView) root.findViewById(R.id.add_apiary_name);
        locationTextView = (TextView) root.findViewById(R.id.add_apiary_location);
        getLocationIcon = (ImageView) root.findViewById(R.id.get_location_icon);
        notesTextView = (TextView) root.findViewById(R.id.add_apiary_notes);

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
                presenter.toogleLocation(getContext());
            }
        });

        // Configure floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_apiary);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.save(nameTextView.getText().toString(),
                        notesTextView.getText().toString());
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
        nameTextView.setText(name);
    }

    @Override
    public void setLocation(Location location) {
        String sb = "(" +
                String.valueOf(location.getLatitude()) +
                ", " +
                String.valueOf(location.getLongitude()) +
                ") ±" +
                Math.round(location.getAccuracy()) +
                "m";
        locationTextView.setText(sb);
    }

    @Override
    public void setNotes(String notes) {
        notesTextView.setText(notes);
    }

    @Override
    public void setLocationIcon(boolean active) {
        getLocationIcon.setColorFilter(active ?
                ContextCompat.getColor(getContext(), R.color.colorPrimaryDark) :
                ContextCompat.getColor(getContext(), R.color.colorAccent));
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
        showMessage(nameTextView, getString(R.string.empty_apiary_message));
    }

    @Override
    public void showGpsConnectionError() {
        showMessage(nameTextView, getString(R.string.gps_error_message));
    }

    @Override
    public void showSaveApiaryError() {
        showMessage(getView(), getString(R.string.save_apiary_error_message));
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
