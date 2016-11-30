package com.davidmiguel.gobees.apiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.BaseTabFragment;

/**
 * Display apiary info.
 * TODO
 */
public class ApiaryInfoFragment extends Fragment implements BaseTabFragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static ApiaryInfoFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, 2);
        ApiaryInfoFragment fragment = new ApiaryInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apiary_info_frag, container, false);
        TextView textView = (TextView) view;
        textView.setText("Fragment #" + mPage);
        return view;
    }

    @Override
    public int getTabName() {
        return R.string.info_tab;
    }
}
