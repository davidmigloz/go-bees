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

package com.davidmiguel.gobees.apiary;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
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

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Hives list adapter.
 */
class HivesAdapter extends RecyclerView.Adapter<HivesAdapter.HivesViewHolder> {

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
    public HivesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apiary_hives_list_item, parent, false);
        return new HivesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HivesViewHolder holder, int position) {
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
        void onHiveClick(Hive hive);

        void onHiveDelete(Hive hive);

        void onHiveEdit(Hive hive);

        void onOpenMenuClick(View view);
    }

    class HivesViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<Hive>, View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,
            ItemTouchHelperViewHolder {

        private View viewHolder;
        private CardView card;
        private TextView hiveName;
        private TextView lastRevision;
        private ImageView moreIcon;

        private Drawable background;

        HivesViewHolder(View itemView) {
            super(itemView);

            // Get views
            viewHolder = itemView;
            card = (CardView) itemView.findViewById(R.id.card);
            hiveName = (TextView) itemView.findViewById(R.id.hive_name);
            lastRevision = (TextView) itemView.findViewById(R.id.last_revision);
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

        @Override
        public void bind(@NonNull Hive hive) {
            hiveName.setText(hive.getName());
            lastRevision.setText(
                    DateUtils.getRelativeTimeSpanString(hive.getLastRevision().getTime(),
                            (new Date()).getTime(), DateUtils.MINUTE_IN_MILLIS));
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
                    listener.onHiveEdit(hives.get(getAdapterPosition()));
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
