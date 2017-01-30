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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.about.AboutActivity;
import com.davidmiguel.gobees.help.HelpActivity;
import com.davidmiguel.gobees.settings.SettingsActivity;
import com.davidmiguel.gobees.utils.AndroidUtils;
import com.davidmiguel.gobees.utils.BaseActivity;

/**
 * Apiaries activity.
 */
public class ApiariesActivity extends BaseActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apiaries_act);

        // Set up the toolbar
        AndroidUtils.setUpToolbar(this, true);

        // Set up the navigation drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // Add fragment to the activity
        ApiariesFragment apiariesFragment =
                (ApiariesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (apiariesFragment == null) {
            // Create the fragment
            apiariesFragment = ApiariesFragment.newInstance();
            AndroidUtils.addFragmentToActivity(
                    getSupportFragmentManager(), apiariesFragment, R.id.contentFrame);
        }

        // Set default preferences values
        PreferenceManager.setDefaultValues(this, R.xml.general_settings, false);

        // Create the presenter
        new ApiariesPresenter(goBeesRepository, apiariesFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Open the navigation drawer when the home icon is selected from the toolbar
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set the actions to be carried out from the drawerLayout.
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.settings_navigation_menu_item:
                                // Settings
                                openSettings();
                                break;
                            case R.id.help_navigation_menu_item:
                                // Help
                                openHelp();
                                break;
                            case R.id.feedback_navigation_menu_item:
                                // Feedback
                                openSendFeedback();
                                break;
                            case R.id.share_app_navigation_menu_item:
                                // Share app
                                openShareApp();
                                break;
                            case R.id.about_navigation_menu_item:
                                // About
                                openAbout();
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(false);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    /**
     * Opens settings section.
     */
    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Opens help section.
     */
    private void openHelp() {
        Intent helpIntent =
                new Intent(ApiariesActivity.this, HelpActivity.class);
        startActivity(helpIntent);
    }

    /**
     * Opens send feedback option.
     */
    private void openSendFeedback() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + getString(R.string.gobees_email)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.gobees_email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.gobees_email_body));
        startActivity(Intent.createChooser(
                emailIntent, getString(R.string.feedback_title)));
    }

    /**
     * Opens share app option.
     */
    private void openShareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_app_text) +
                        Uri.parse(getString(R.string.share_app_url)));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(
                sendIntent, getString(R.string.share_app_title)));
    }

    /**
     * Opens about section.
     */
    private void openAbout() {
        Intent aboutIntent =
                new Intent(ApiariesActivity.this, AboutActivity.class);
        startActivity(aboutIntent);
    }
}
