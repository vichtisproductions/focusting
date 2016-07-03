package org.coderswithoutborders.deglancer.di;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by Renier on 2016/04/04.
 */
@Module
public class DataModule {
    @Singleton
    @Provides
    DatabaseReference provideFirebaseClient(Context context) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String firebaseURL = "https://flickering-heat-4815.firebaseio.com/users/";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);

        return ref;
    }
    @Singleton
    @Provides
    Realm provideRealm(RealmConfiguration config) {
        Realm.setDefaultConfiguration(config);
        return Realm.getDefaultInstance();
    }


    @Singleton
    @Provides
    RealmConfiguration provideRealmConfiguration(Context context) {
        return new RealmConfiguration.Builder(context)
                .name("deglancer.realm")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

                    }
                })
                .build();
    }
}
