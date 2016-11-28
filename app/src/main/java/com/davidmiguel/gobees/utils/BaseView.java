package com.davidmiguel.gobees.utils;

import android.support.annotation.NonNull;

public interface BaseView<T> {

    /**
     * Set presenter to the view.
     * @param presenter presenter.
     */
    void setPresenter(@NonNull T presenter);

    /**
     * Returns true if the fragment is added to the activity.
     * @return true if active.
     */
    boolean isActive();

}
