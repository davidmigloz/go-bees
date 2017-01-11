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

package com.davidmiguel.gobees.addedithive;

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
interface AddEditHiveContract {

    interface View extends BaseView<AddEditHiveContract.Presenter> {

        /**
         * Shows message warning that the hive cannot be empty.
         */
        void showEmptyHiveError();

        /**
         * Shows save error message.
         */
        void showSaveHiveError();

        /**
         * Goes back to hives activity.
         */
        void showHivesList();

        /**
         * Sets hive name in the text view.
         *
         * @param name hive name.
         */
        void setName(String name);

        /**
         * Sets hive notes in the text view.
         *
         * @param notes hive notes.
         */
        void setNotes(String notes);

        /**
         * Closes / hides soft Android keyboard.
         */
        void closeKeyboard();
    }

    interface Presenter extends BasePresenter {

        /**
         * Saves or updates a hive in the repository.
         *
         * @param name  hive name.
         * @param notes hive notes.
         */
        void save(String name, String notes);

        /**
         * Fill hive data (the hive must already exist in the repository).
         */
        void populateHive();
    }
}
