package com.davidmiguel.gobees.data.source.local;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getHives(long apiaryId, @NonNull GetHivesCallback callback) {
        try {
            Apiary apiary = realm.where(Apiary.class).equalTo("id", apiaryId).findFirst();
            callback.onHivesLoaded(new ArrayList<>(apiary.getHives()));
        } catch (Exception e) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getHive(long hiveId, @NonNull GetHiveCallback callback) {
        try {
            Hive hive = realm.where(Hive.class).equalTo("id", hiveId).findFirst();
            callback.onHiveLoaded(hive);
        } catch (Exception e) {
            callback.onDataNotAvailable();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getHiveWithRecordings(long hiveId, @NonNull GetHiveCallback callback) {
        try {
            // Get hive
            Hive hive = realm.where(Hive.class).equalTo("id", hiveId).findFirst();
            // Get records
            RealmResults<Record> records = hive.getRecords().where().findAll().sort("timestamp");
            // Clasify records by date into recordings
            Date day;                   // Actual date of the recording
            Date nextDay = new Date(0); // Next day to the recording
            RealmResults<Record> filteredRecords;
            List<Recording> recordings = new ArrayList<>();
            while(true){
                // Get all records greather than last recordings
                records = records.where().greaterThanOrEqualTo("timestamp", nextDay).findAll();
                if (records.isEmpty()) {
                    break;
                }
                // Get range of days to filter
                day = DateTimeUtil.getDateOnly(records.first().getTimestamp());
                nextDay = DateTimeUtil.getNextDay(day);
                // Filter records of that date and create recording
                filteredRecords = records.where()
                        .greaterThanOrEqualTo("timestamp", day)
                        .lessThan("timestamp", DateTimeUtil.getNextDay(nextDay))
                        .findAll();
                // Create recording
                recordings.add(new Recording(day, new ArrayList<>(filteredRecords)));
            }
            // Set recordings to hive
            hive.setRecordings(recordings);
            // Return hive
            callback.onHiveLoaded(hive);
        } catch (Exception e) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void refreshHives(long apiaryId) {
        // Not required because the GoBeesRepository handles the logic of refreshing the
        // data from all the available data sources
    }

    @Override
    public void saveHive(@NonNull final Hive hive, @NonNull TaskCallback callback) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Save hive
                    realm.copyToRealmOrUpdate(hive);
                }
            });
            callback.onSuccess();
        } catch (Exception e) {
            callback.onFailure();
        }
    }

    @Override
    public void getNextHiveId(@NonNull GetNextHiveIdCallback callback) {
        Number nextId = realm.where(Hive.class).max("id");
        callback.onNextHiveIdLoaded(nextId != null ? nextId.longValue() + 1 : 0);
    }

    @Override
    public void refreshRecordings(long hiveId) {
        // TODO
    }
}
