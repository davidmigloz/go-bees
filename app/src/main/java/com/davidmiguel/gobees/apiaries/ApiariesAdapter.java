package com.davidmiguel.gobees.apiaries;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Apiaries list adapter.
 */
public class ApiariesAdapter extends RecyclerView.Adapter<ApiariesAdapter.ViewHolder> {

    private List<Apiary> apiaries;
    private ApiaryItemListener listener;

    public ApiariesAdapter(List<Apiary> apiaries, ApiaryItemListener listener) {
        this.apiaries = checkNotNull(apiaries);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apiaries_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(apiaries.get(position));
    }

    @Override
    public int getItemCount() {
        return apiaries == null ? 0 : apiaries.size();
    }

    public void replaceData(List<Apiary> apiaries) {
        this.apiaries = checkNotNull(apiaries);
        notifyDataSetChanged();
    }


    public interface ApiaryItemListener {
        void onApiaryClick(Apiary clickedApiary);
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, ItemTouchHelperViewHolder {

        private TextView titleTV;

        ViewHolder(View itemView) {
            super(itemView);

            titleTV = (TextView) itemView.findViewById(R.id.item_title);
        }

        void bind(Apiary apiary) {
            titleTV.setText(apiary.getName());
        }

        @Override
        public void onClick(View view) {
            listener.onApiaryClick(apiaries.get(getAdapterPosition()));
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
