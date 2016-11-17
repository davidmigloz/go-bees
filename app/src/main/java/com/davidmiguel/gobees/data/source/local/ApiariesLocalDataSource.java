package com.davidmiguel.gobees.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.ApiariesDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class ApiariesLocalDataSource implements ApiariesDataSource {

    private static ApiariesLocalDataSource INSTANCE;

    private ApiariesDbHelper dbHelper;

    private ApiariesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        dbHelper = new ApiariesDbHelper(context);
    }

    public static ApiariesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ApiariesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getApiaries(@NonNull GetApiariesCallback callback) {

    }

    @Override
    public void getApiary(int apiaryId, @NonNull GetApiaryCallback callback) {

    }

    @Override
    public void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback) {

    }

    @Override
    public void refreshApiaries() {

    }

    @Override
    public void deleteApiary(int apiaryId, @NonNull TaskCallback callback) {

    }

    @Override
    public void deleteAllApiaries(@NonNull TaskCallback callback) {

    }
}
