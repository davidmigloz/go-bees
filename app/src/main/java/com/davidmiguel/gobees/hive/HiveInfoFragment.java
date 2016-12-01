package com.davidmiguel.gobees.hive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.BaseTabFragment;

/**
 * Display hive info.
 * // TODO
 */
public class HiveInfoFragment extends Fragment implements BaseTabFragment {

    public static HiveInfoFragment newInstance() {
        return new HiveInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apiary_info_frag, container, false);
        TextView textView = (TextView) view;
        textView.setText("Fragment #INFO");
        return view;
    }

    @Override
    public int getTabName() {
        return R.string.hive_info_tab;
    }
}
