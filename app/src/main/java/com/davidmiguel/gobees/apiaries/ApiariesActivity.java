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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.about.AboutActivity;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.help.HelpActivity;
import com.davidmiguel.gobees.settings.SettingsActivity;
import com.davidmiguel.gobees.utils.AndroidUtils;

/**
 * Apiaries activity.
 */
public class ApiariesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private GoBeesRepository goBeesRepository;

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
            setupDrawerContent(navigationView, this);
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

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new ApiariesPresenter(goBeesRepository, apiariesFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database
        goBeesRepository.closeDb();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Set the actions to be carried out from the drawerLayout.
     */
    private void setupDrawerContent(NavigationView navigationView, final Context context) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.settings_navigation_menu_item:
                                // Settings
                                Intent intent = new Intent(context, SettingsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.help_navigation_menu_item:
                                // Help
                                Intent helpIntent =
                                        new Intent(ApiariesActivity.this, HelpActivity.class);
                                startActivity(helpIntent);
                                break;
                            case R.id.feedback_navigation_menu_item:
                                // Feedback
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                                        Uri.parse("mailto:" + getString(R.string.gobees_email)));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                        getString(R.string.gobees_email_subject));
                                emailIntent.putExtra(Intent.EXTRA_TEXT,
                                        getString(R.string.gobees_email_body));
                                startActivity(Intent.createChooser(
                                        emailIntent, getString(R.string.feedback_title)));
                                break;
                            case R.id.share_app_navigation_menu_item:
                                // Share app
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT,
                                        getString(R.string.share_app_text) +
                                                Uri.parse(getString(R.string.share_app_url)));
                                sendIntent.setType("text/plain");
                                startActivity(Intent.createChooser(
                                        sendIntent, getString(R.string.share_app_title)));
                                break;
                            case R.id.about_navigation_menu_item:
                                // About
                                Intent aboutIntent =
                                        new Intent(ApiariesActivity.this, AboutActivity.class);
                                startActivity(aboutIntent);
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
}
