package com.davidmiguel.gobees.addeditapiary;

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditApiaryContract {

    interface View extends BaseView<Presenter> {

        /**
         * Shows message warning that the apiary cannot be empty.
         */
        void showEmptyApiaryError();

        /**
         * Shows save error message.
         */
        void showSaveApiaryError();

        /**
         * Goes back to apiaries activity.
         */
        void showApiariesList();

        /**
         * Sets apiary name in the text view.
         *
         * @param name apiary name.
         */
        void setName(String name);

        /**
         * Sets apiary notes in the text view.
         *
         * @param notes apiary notes.
         */
        void setNotes(String notes);
    }

    interface Presenter extends BasePresenter {

        /**
         * Saves or updates an apiary in the repository.
         *
         * @param name  apiary name.
         * @param notes apiary notes.
         */
        void saveApiary(String name, String notes);

        /**
         * Fill apiary data (the apiary must already exist in the repository).
         */
        void populateApiary();
    }
}
