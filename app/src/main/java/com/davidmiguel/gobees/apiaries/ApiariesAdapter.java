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

package com.davidmiguel.gobees.apiaries;

import android.content.Context;
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
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.BaseViewHolder;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;
import com.davidmiguel.gobees.utils.WeatherUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Apiaries list adapter.
 */
class ApiariesAdapter extends RecyclerView.Adapter<ApiariesAdapter.ApiaryViewHolder> {

    private final Context context;
    private MenuInflater menuInflater;
    private List<Apiary> apiaries;
    private ApiaryItemListener listener;

    ApiariesAdapter(Context context, MenuInflater menuInflater, List<Apiary> apiaries,
                    ApiaryItemListener listener) {
        this.context = context;
        this.menuInflater = menuInflater;
        this.apiaries = checkNotNull(apiaries);
        this.listener = listener;
    }

    @Override
    public ApiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apiaries_list_item, parent, false);
        return new ApiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApiaryViewHolder holder, int position) {
        holder.bind(apiaries.get(position));
    }

    @Override
    public int getItemCount() {
        return apiaries == null ? 0 : apiaries.size();
    }

    void replaceData(List<Apiary> apiaries) {
        this.apiaries = checkNotNull(apiaries);
        notifyDataSetChanged();
    }

    interface ApiaryItemListener {
        void onApiaryClick(Apiary apiary);

        void onApiaryDelete(Apiary apiary);

        void onApiaryEdit(Apiary apiary);

        void onOpenMenuClick(View view);
    }

    class ApiaryViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<Apiary>, View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,
            ItemTouchHelperViewHolder {

        private View view;
        private CardView card;
        private TextView apiaryName;
        private TextView numHives;
        private ImageView weatherIcon;
        private TextView temp;
        private ImageView moreIc;

        private Drawable background;

        ApiaryViewHolder(View itemView) {
            super(itemView);

            // Get views
            view = itemView;
            card = (CardView) itemView.findViewById(R.id.card);
            apiaryName = (TextView) itemView.findViewById(R.id.apiary_name);
            numHives = (TextView) itemView.findViewById(R.id.num_hives);
            weatherIcon = (ImageView) itemView.findViewById(R.id.weather_icon);
            temp = (TextView) itemView.findViewById(R.id.temp);
            moreIc = (ImageView) itemView.findViewById(R.id.more_icon);

            // Set listeners (click -> enter, long click -> menu, more ic click -> menu)
            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
            moreIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open Menu
                    listener.onOpenMenuClick(ApiaryViewHolder.this.view);
                }
            });

            background = card.getBackground();
        }

        @Override
        public void bind(@NonNull Apiary apiary) {
            // Set apiary name
            apiaryName.setText(apiary.getName());
            // Set number of hives
            if (apiary.getHives() != null) {
                numHives.setText(Integer.toString(apiary.getHives().size()));
            }
            if (apiary.getCurrentWeather() != null) {
                // Set weather icon
                String iconId = apiary.getCurrentWeather().getWeatherConditionIcon();
                weatherIcon.setImageResource(WeatherUtils.getWeatherIconResourceId(iconId));
                // Set temperature
                double temperature = apiary.getCurrentWeather().getTemperature();
                temp.setText(WeatherUtils.formatTemperature(context, temperature));
                // Show
                weatherIcon.setVisibility(View.VISIBLE);
                temp.setVisibility(View.VISIBLE);
            } else {
                // Hide
                weatherIcon.setVisibility(View.GONE);
                temp.setVisibility(View.GONE);
                temp.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
            // Inflate menu
            menuInflater.inflate(R.menu.apiary_item_menu, contextMenu);
            // Set click listener
            for (int i = 0; i < contextMenu.size(); i++) {
                contextMenu.getItem(i).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            listener.onApiaryClick(apiaries.get(getAdapterPosition()));
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_edit:
                    listener.onApiaryEdit(apiaries.get(getAdapterPosition()));
                    return true;
                case R.id.menu_delete:
                    listener.onApiaryDelete(apiaries.get(getAdapterPosition()));
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
