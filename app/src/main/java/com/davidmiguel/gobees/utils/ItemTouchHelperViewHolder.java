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

package com.davidmiguel.gobees.utils;

/**
 * Interface to notify an item ViewHolder of relevant callbacks from
 * android.support.v7.widget.helper.ItemTouchHelper.Callback.
 */
public interface ItemTouchHelperViewHolder {
    /**
     * Called when the ItemTouchHelper first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();

    /**
     * Called when the ItemTouchHelper has completed the move or swipe, and the active item
     * state should be cleared.
     */
    void onItemClear();
}