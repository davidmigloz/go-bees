package com.davidmiguel.gobees.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public class ApiariesPersistenceContract {

    private ApiariesPersistenceContract() {}

    public static abstract class ApiaryEntry implements BaseColumns {
        public static final String TABLE_NAME = "";
        public static final String COLUMN_NAME_ID = "";
    }
}
