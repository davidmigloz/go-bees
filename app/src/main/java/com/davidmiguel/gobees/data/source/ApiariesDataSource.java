package com.davidmiguel.gobees.data.source;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;

import java.util.List;

/**
 * Main entry point for accessing apiaries data.
 */
public interface ApiariesDataSource {

    void getApiaries(@NonNull GetApiariesCallback callback);

    void getApiary(int apiaryId, @NonNull GetApiaryCallback callback);

    void saveApiary(@NonNull Apiary apiary, @NonNull TaskCallback callback);

    void refreshApiaries();

    void deleteApiary(int apiaryId, @NonNull TaskCallback callback);

    interface GetApiariesCallback {
        void onApiariesLoaded(List<Apiary> apiaries);

        void onDataNotAvailable();
    }

    interface GetApiaryCallback {
        void onApiaryLoaded(Apiary apiary);

        void onDataNotAvailable();
    }

    interface TaskCallback {
        void onSuccess();

        void onFailure();
    }
}
