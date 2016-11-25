package com.davidmiguel.gobees.apiaries;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.ItemTouchHelperAdapter;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Apiaries list adapter.
 */
public class ApiariesAdapter extends RecyclerView.Adapter<ApiariesAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(apiaries, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        Apiary apiary = apiaries.get(position);
        listener.onApiaryDelete(apiary);
        apiaries.remove(position);
        notifyItemRemoved(position);
    }


    public interface ApiaryItemListener {
        void onApiaryClick(Apiary clickedApiary);

        void onApiaryDelete(Apiary clickedApiary);
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, ItemTouchHelperViewHolder {

        private CardView card;
        private TextView titleTV;
        private Drawable background;

        ViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card);
            titleTV = (TextView) itemView.findViewById(R.id.apiary_name);
            background = card.getBackground();
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
            card.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            card.setBackground(background);
        }
    }
}
