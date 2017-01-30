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

package com.davidmiguel.gobees.about;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.BaseViewHolder;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Libraries list adapter.
 */
class LibsAdapter extends RecyclerView.Adapter<LibsAdapter.LibViewHolder> {

    private List<Library> libraries;
    private LibItemListener listener;

    LibsAdapter(List<Library> libraries, LibItemListener listener) {
        this.libraries = checkNotNull(libraries);
        this.listener = listener;
    }

    @Override
    public LibViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.about_libs_list_item, parent, false);
        return new LibViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LibViewHolder holder, int position) {
        holder.bind(libraries.get(position));
    }

    @Override
    public int getItemCount() {
        return libraries == null ? 0 : libraries.size();
    }

    void replaceData(List<Library> libraries) {
        this.libraries = checkNotNull(libraries);
        notifyDataSetChanged();
    }

    interface LibItemListener {
        void onLibraryClicked(Library.License license);
    }

    class LibViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<Library>, View.OnClickListener {

        private TextView libraryTv;
        private Button license;

        LibViewHolder(View itemView) {
            super(itemView);

            // Get views
            libraryTv = (TextView) itemView.findViewById(R.id.lib_name);
            license = (Button) itemView.findViewById(R.id.license);

            // Set listeners
            license.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull Library library) {
            // Set library name
            libraryTv.setText(library.getName());
            // Set library license
            license.setText(library.getLicense().toString());
        }

        @Override
        public void onClick(View view) {
            listener.onLibraryClicked(libraries.get(getAdapterPosition()).getLicense());
        }
    }
}
