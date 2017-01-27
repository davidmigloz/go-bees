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

package com.davidmiguel.gobees.help;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.davidmiguel.gobees.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display GoBees help webpage.
 */
public class HelpFragment extends Fragment implements HelpContract.View {

    private HelpContract.Presenter presenter;

    private WebView webView;

    public HelpFragment() {
        // Requires empty public constructor
    }

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.help_frag, container, false);

        webView = (WebView) root.findViewById(R.id.webview);

        // Configure related browser settings
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new MyBrowser());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull HelpContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    /**
     * Manages the behavior when URLs are loaded.
     */
    private class MyBrowser extends WebViewClient {

        /**
         * Give the host application a chance to take over the control when a new url is about to
         * be loaded in the current WebView. If WebViewClient is not provided, by default WebView
         * will ask Activity Manager to choose the proper handler for the url.
         * If WebViewClient is provided, return true means the host application handles the url,
         * while return false means the current WebView handles the url.
         * This method is not called for requests using the POST "method".
         *
         * @param view web-view
         * @param url  url to load.
         * @return True if the host application wants to leave the current WebView
         * and handle the url itself, otherwise return false.
         * @deprecated It's deprecated for versions from Android N and up.
         */
        @SuppressWarnings("deprecation")
        @Deprecated
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
