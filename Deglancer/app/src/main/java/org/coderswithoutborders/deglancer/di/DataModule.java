package org.coderswithoutborders.deglancer.di;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

/**
 * Created by Renier on 2016/04/04.
 */
@Module
public class DataModule {
    @Singleton
    @Provides
    DatabaseReference provideFirebaseClient(Context context) {

        FirebaseAuth mAuth;
        FirebaseAuth.AuthStateListener mAuthListener;
        mAuth = getInstance();

        String fbAuthUsername="lauri.palokangas@gmail.com";
        String fbAuthPassword="9Kvn0m9PiWc&Zqrd%@GnAbMd";

        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Deglancer.auth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Deglancer.auth", "onAuthStateChanged:signed_out");
                }
            }
        };
        */

        // mAuthListener = new FirebaseAuth.AuthStateListener();
        // mAuth.addAuthStateListener(mAuthListener);

        /*
        mAuth.signInWithEmailAndPassword(fbAuthUsername, fbAuthPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Deglancer.auth", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("Deglancer.auth", "signInWithEmail", task.getException());
                        }
                    }
                });
        */

        mAuth.signInWithEmailAndPassword(fbAuthUsername, fbAuthPassword);

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
