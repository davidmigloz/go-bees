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

package com.davidmiguel.gobees.settings;

import android.content.Context;
import android.preference.Preference;

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
interface SettingsContract {

    interface View extends BaseView<SettingsContract.Presenter> {

        /**
         * Returns the context.
         *
         * @return context.
         */
        Context getContext();

        /**
         * Shows loading message.
         */
        void showLoadingMsg();

        /**
         * Shows data generated successfully message.
         */
        void showDataGeneratedMsg();

        /**
         * Shows data deleted successfully message.
         */
        void showDataDeletedMsg();
    }


    interface Presenter extends BasePresenter {

        /**
         * Attaches a listener that performs the specified action when the preference is clicked.
         *
         * @param preference preference to attach.
         */
        void bindPreferenceClickListener(Preference preference);

        /**
         * Attaches a listener so the summary is always updated with the preference value.
         * Also fires the listener once, to initialize the summary (so it shows up before the value
         * is changed).
         *
         * @param preference preference to attach.
         */
        void bindPreferenceSummaryToValue(Preference preference);

    }
}
