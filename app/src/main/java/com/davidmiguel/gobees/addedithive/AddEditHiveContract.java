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
