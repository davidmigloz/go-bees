package com.davidmiguel.gobees.data.source.local;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Concrete implementation of a data source as a Realm db.
 */
public class GoBeesLocalDataSource implements GoBeesDataSource {

    private static GoBeesLocalDataSource INSTANCE;
    private Realm realm;


    private GoBeesLocalDataSource() {
    }

    public static GoBeesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoBeesLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void openDb() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void closeDb() {
        realm.close();
    }

    @Override
    public void getApiaries(@NonNull GetApiariesCallback callback) {
        try {
            RealmResults<Apiary> apiaries = realm.where(Apiary.class).findAll();
            callback.onApiariesLoaded(new ArrayList<>(apiaries));
        } catch (Exception e) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getApiary(long apiaryId, @NonNull GetApiaryCallback callback) {
        try {
            Apiary apiary = realm.where(Apiary.class).equalTo("id", apiaryId).findFirst();
            callback.onApiaryLoaded(apiary);
        } catch (Exception e) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveApiary(@NonNull final Apiary apiary, @NonNull TaskCallback callback) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Save apiary
                    realm.copyToRealmOrUpdate(apiary);
                }
            });
            callback.onSuccess();
        } catch (Exception e) {
            callback.onFailure();
        }

    }

    @Override
    public void refreshApiaries() {
        // Not required because the GoBeesRepository handles the logic of refreshing the
        // data from all the available data sources
    }

    @Override
    public void deleteApiary(long apiaryId, @NonNull TaskCallback callback) {
        try {
            final Apiary apiary = realm.where(Apiary.class).equalTo("id", apiaryId).findFirst();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Delete apiary
                    apiary.deleteFromRealm();
                }
            });
            callback.onSuccess();
        } catch (Exception e) {
            callback.onFailure();
        }
    }

    @Override
    public void deleteAllApiaries(@NonNull TaskCallback callback) {
        try {
            final RealmResults<Apiary> apiaries = realm.where(Apiary.class).findAll();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Delete all apiaries
                    apiaries.deleteAllFromRealm();
                }
            });
            callback.onSuccess();
        } catch (Exception e) {
            callback.onFailure();
        }
    }

    @Override
    public void getNextApiaryId(@NonNull GetNextApiaryIdCallback callback) {
        Number nextId = realm.where(Apiary.class).max("id");
        callback.onNextApiaryIdLoaded(nextId != null ? nextId.longValue() + 1 : 0);
    }

    @Override
    public void getHives(long apiaryId, @NonNull GetHivesCallback callback) {

    }

    @Override
    public void refreshHives(long apiaryId) {

    }
}
