package com.davidmiguel.gobees.apiary;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.utils.BaseViewHolder;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Hives list adapter.
 */
class HivesAdapter extends RecyclerView.Adapter<HivesAdapter.ViewHolder> {

    private MenuInflater menuInflater;
    private List<Hive> hives;
    private HivesAdapter.HiveItemListener listener;

    HivesAdapter(MenuInflater menuInflater, List<Hive> hives,
                 HivesAdapter.HiveItemListener listener) {
        this.menuInflater = menuInflater;
        this.hives = checkNotNull(hives);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apiary_hives_list_item, parent, false);
        return new HivesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(hives.get(position));
    }

    @Override
    public int getItemCount() {
        return hives == null ? 0 : hives.size();
    }

    void replaceData(List<Hive> hives) {
        this.hives = checkNotNull(hives);
        notifyDataSetChanged();
    }

    interface HiveItemListener {
        void onHiveClick(Hive clickedHive);

        void onHiveDelete(Hive clickedHive);

        void onOpenMenuClick(View view);
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<Hive>, View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,
            ItemTouchHelperViewHolder {

        private View viewHolder;
        private CardView card;
        private TextView hiveName;
        private ImageView moreIcon;

        private Drawable background;

        ViewHolder(View itemView) {
            super(itemView);

            // Get views
            viewHolder = itemView;
            card = (CardView) itemView.findViewById(R.id.card);
            hiveName = (TextView) itemView.findViewById(R.id.hive_name);
            moreIcon = (ImageView) itemView.findViewById(R.id.more_icon);

            // Set listeners
            viewHolder.setOnClickListener(this);
            viewHolder.setOnCreateContextMenuListener(this);
            moreIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open Menu
                    listener.onOpenMenuClick(viewHolder);
                }
            });

            background = card.getBackground();
        }

        public void bind(@NonNull Hive hive) {
            hiveName.setText(hive.getName());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
            // Inflate menu
            menuInflater.inflate(R.menu.hive_item_menu, contextMenu);
            // Set click listener
            for (int i = 0; i < contextMenu.size(); i++) {
                contextMenu.getItem(i).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            listener.onHiveClick(hives.get(getAdapterPosition()));
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_edit:
                    // TODO
                    return true;
                case R.id.menu_delete:
                    listener.onHiveDelete(hives.get(getAdapterPosition()));
                    return true;
                default:
                    return false;
            }
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
