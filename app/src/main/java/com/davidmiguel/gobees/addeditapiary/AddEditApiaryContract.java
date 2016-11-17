package com.davidmiguel.gobees.addeditapiary;

import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public class AddEditApiaryContract {

    interface View extends BaseView<Presenter> {

        void showEmptyApiaryError();

        void showSaveApiaryError();

        void showApiariesList();

        void setName(String name);

        void setNotes(String notes);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveApiary(String name, String notes);

        void populateApiary();
    }

}
