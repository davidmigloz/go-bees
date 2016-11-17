package com.davidmiguel.gobees.addeditapiary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.gobees.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display add apiary form.
 */
public class AddEditApiaryFragment extends Fragment implements AddEditApiaryContract.View {

    public static final String ARGUMENT_EDIT_APIARY_ID = "EDIT_APIARY_ID";

    private AddEditApiaryContract.Presenter presenter;

    private TextView nameTextView;

    private TextView notesTextView;

    public AddEditApiaryFragment() {
        // Requires empty public constructor
    }

    public static AddEditApiaryFragment newInstance() {
        return new AddEditApiaryFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Configure floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_apiary);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveApiary(nameTextView.getText().toString(),
                        notesTextView.getText().toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addeditapiary_frag, container, false);
        nameTextView = (TextView) root.findViewById(R.id.add_apiary_name);
        notesTextView = (TextView) root.findViewById(R.id.add_apiary_notes);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showEmptyApiaryError() {
        showMessage(nameTextView, getString(R.string.empty_apiary_message));
    }

    @Override
    public void showSaveApiaryError() {
        showMessage(getView(), getString(R.string.save_apiary_error_message));
    }

    @Override
    public void showApiariesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setName(String name) {
        nameTextView.setText(name);
    }

    @Override
    public void setNotes(String notes) {
        nameTextView.setText(notes);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(@NonNull AddEditApiaryContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @SuppressWarnings("ConstantConditions")
    private void showMessage(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
