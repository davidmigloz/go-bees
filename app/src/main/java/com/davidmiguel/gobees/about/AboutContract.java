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

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface AboutContract {

    interface View extends BaseView<AboutContract.Presenter> {

        /**
         * Shows app verion.
         *
         * @param version vresion number.
         */
        void showVersion(String version);

        /**
         * Opens website.
         *
         * @param url website url.
         */
        void openWebsite(int url);

        /**
         * Opens a modal window with the changelog.
         *
         * @param title     title.
         * @param changelog data.
         */
        void openChangelog(int title, int changelog);

        /**
         * Opens a modal window with the license text.
         *
         * @param title   modal title.
         * @param license license text.
         */
        void openLicence(String title, int license);

        /**
         * Shows list of libraries used in the app.
         *
         * @param libraries list of libraries.
         */
        void showLibraries(List<Library> libraries);

    }

    interface Presenter extends BasePresenter {

        /**
         * When website button is clicked.
         */
        void onWebsiteClicked();

        /**
         * When changelog button is clicked.
         */
        void onChangelogClicked();

        /**
         * When a license button is clicked.
         *
         * @param license type of license.
         */
        void onLicenseClicked(Library.License license);

    }
}
