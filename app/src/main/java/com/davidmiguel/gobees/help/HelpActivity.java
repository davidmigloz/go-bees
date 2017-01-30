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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.utils.AndroidUtils;

/**
 * Help activity (webview that shows GoBees help webpage).
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_act);

        // Set up the toolbar
        AndroidUtils.setUpToolbar(this, false, R.string.help_title);

        // Add fragment to the activity
        HelpFragment helpFragment =
                (HelpFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (helpFragment == null) {
            // Create the fragment
            helpFragment = HelpFragment.newInstance();
            AndroidUtils.addFragmentToActivity(
                    getSupportFragmentManager(), helpFragment, R.id.contentFrame);
        }

        // Create the presenter
        new HelpPresenter(helpFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
