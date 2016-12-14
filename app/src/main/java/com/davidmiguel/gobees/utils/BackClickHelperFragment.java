package com.davidmiguel.gobees.utils;

/**
 * Interface to notify a fragment when back button is clicked.
 */
public interface BackClickHelperFragment {

    /**
     * Called when the back button is pressed.
     * @return true to perform default action, false to ignore it.
     */
    boolean onBackPressed();
}
