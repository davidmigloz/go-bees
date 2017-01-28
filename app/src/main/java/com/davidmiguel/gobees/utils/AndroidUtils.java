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

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.davidmiguel.gobees.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Android related utilities.
 */
public final class AndroidUtils {

    /**
     * Private constructor.
     */
    private AndroidUtils() {
    }

    /**
     * The fragment is added to the container view with id frameId. The operation is
     * performed by the fragmentManager.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * Closes / hides soft Android keyboard.
     *
     * @param activity current activity.
     */
    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Html.fromHtml wrapper supporting new and old versions.
     *
     * @param html html to parse.
     * @return Spanned text.
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    /**
     * Set up the loading indicator and its associated action.
     *
     * @param root      root view.
     * @param context   context.
     * @param childView child view when to attach the swipe.
     * @param presenter presenter.
     */
    public static void setUpProgressIndicator(View root, Context context, View childView,
                                              final BaseLoadDataPresenter presenter) {
        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorAccent),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(childView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(true);
            }
        });
    }

    /**
     * Displays or hide loading indicator.
     *
     * @param view   current view.
     * @param active true or false.
     */
    public static void setLoadingIndicator(View view, final boolean active) {
        if (view == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        // Make sure setRefreshing() is called after the layout is done with everything else
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    /**
     * Setup toolbar.
     *
     * @param act    current activity.
     * @param isHome if it is home activity.
     * @param title  title.
     * @return ActionBar.
     */
    public static ActionBar setUpToolbar(AppCompatActivity act, boolean isHome, int title) {
        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        act.setSupportActionBar(toolbar);
        ActionBar ab = act.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            if (isHome) {
                ab.setHomeAsUpIndicator(R.drawable.ic_menu);
                ab.setDisplayShowTitleEnabled(false);
            } else {
                ab.setDisplayShowHomeEnabled(true);
            }
            if (title != -1) {
                ab.setTitle(R.string.about_title);
            }
        }
        return ab;
    }

    /**
     * Setup toolbar.
     *
     * @param act    current activity.
     * @param isHome if it is home activity.
     * @return ActionBar.
     */
    public static ActionBar setUpToolbar(AppCompatActivity act, boolean isHome) {
        return AndroidUtils.setUpToolbar(act, isHome, -1);
    }
}
