package com.davidmiguel.gobees.apiaries;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Apiary;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Apiaries list adapter.
 */
public class ApiariesAdapter extends BaseAdapter {

    private List<Apiary> apiaries;
    private ApiaryItemListener itemListener;

    public ApiariesAdapter(List<Apiary> apiaries, ApiaryItemListener itemListener) {
        setList(apiaries);
        this.itemListener = itemListener;
    }

    public void replaceData(List<Apiary> apiaries) {
        setList(apiaries);
        notifyDataSetChanged();
    }

    private void setList(List<Apiary> apiaries) {
        this.apiaries = checkNotNull(apiaries);
    }

    @Override
    public int getCount() {
        return apiaries.size();
    }

    @Override
    public Apiary getItem(int i) {
        return apiaries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.apiary_item, viewGroup, false);
        }

        final Apiary apiary = getItem(i);

        TextView titleTV = (TextView) rowView.findViewById(R.id.item_title);
        titleTV.setText(apiary.getName());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onApiaryClick(apiary);
            }
        });

        return rowView;
    }

    public interface ApiaryItemListener {
        void onApiaryClick(Apiary clickedApiary);
    }
}
