package com.davidmiguel.gobees.utils;

import android.support.annotation.NonNull;

public interface BaseViewHolder<T> {

    /**
     * Load data from obj into the view holder.
     * @param obj object from which load data.
     */
    public void bind(@NonNull T obj);

}
