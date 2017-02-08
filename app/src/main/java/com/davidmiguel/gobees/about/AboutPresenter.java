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

import com.davidmiguel.gobees.BuildConfig;
import com.davidmiguel.gobees.R;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Listens to user actions from the UI AboutFragment, retrieves the data and updates the
 * UI as required.
 */
class AboutPresenter implements AboutContract.Presenter {

    private AboutContract.View view;

    AboutPresenter(AboutContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        view.showVersion(BuildConfig.VERSION_NAME);
        view.showLibraries(getLibraries());
    }

    @Override
    public void onWebsiteClicked() {
        view.openWebsite(R.string.gobees_url);
    }

    @Override
    public void onChangelogClicked() {
        view.openChangelog(R.string.changelog_btn, R.string.changelog);
    }

    @Override
    public void onLicenseClicked(Library.License license) {
        switch (license) {
            case APACHE2:
                view.openLicence(license.toString(), R.string.apache_2);
                break;
            case GPL3:
                view.openLicence(license.toString(), R.string.gpl3);
                break;
            case BSD:
                view.openLicence(license.toString(), R.string.bsd);
                break;
            case CCBYSA4:
                view.openLicence(license.toString(), R.string.ccbysa4);
                break;
            case MIT:
                view.openLicence(license.toString(), R.string.mit);
                break;
            default:
        }
    }

    private List<Library> getLibraries() {
        return Lists.newArrayList(
                new Library("Android Support Library", Library.License.APACHE2),
                new Library("Google Play Services", Library.License.APACHE2),
                new Library("Guava", Library.License.APACHE2),
                new Library("Material Design Icons", Library.License.APACHE2),
                new Library("MPAndroidChart", Library.License.APACHE2),
                new Library("OpenCV", Library.License.BSD),
                new Library("OpenWeatherMap API", Library.License.CCBYSA4),
                new Library("Permission Utils", Library.License.MIT),
                new Library("Realm", Library.License.APACHE2),
                new Library("RoundedImageView", Library.License.APACHE2),
                new Library("Simple Weather Icons", Library.License.APACHE2),
                new Library("VNTNumberPicker Preference", Library.License.APACHE2)
        );
    }
}
