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

import org.coderswithoutborders.deglancer.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import timber.log.Timber;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

/**
 * Created by Renier on 2016/04/04.
 */
@Module
public class DataModule {
    @Singleton
    @Provides
    DatabaseReference provideFirebaseClient(Context context) {

        String fbAuthUsername="lauri.palokangas@gmail.com";
        String fbAuthPassword="<PASSWORD_HERE>";
        String TAG="DatabaseReference";

        FirebaseAuth mAuth;
        FirebaseAuth.AuthStateListener mAuthListener;
        mAuth = FirebaseAuth.getInstance();

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Timber.d( "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Timber.d( "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                // updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

        mAuth.addAuthStateListener(mAuthListener);

        // [START sign_in_with_email]

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        String firebaseURL = "https://deglancer-f6fa5.firebaseio.com/users/";
        if (BuildConfig.DEBUG) {
            firebaseURL = "https://flickering-heat-4815.firebaseio.com/users/";
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);

        mAuth.signInWithEmailAndPassword(fbAuthUsername, fbAuthPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Timber.d( "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "signInWithEmail", task.getException());
                            Timber.w(task.getException(), "signInWithEmail");
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]

        /*
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseAuth.signInWithEmailAndPassword(fbAuthUsername, fbAuthPassword);
                // setCurrentUser(mAuth.getCurrentUser());
            }
        });
        mAuth.signInWithEmailAndPassword(fbAuthUsername, fbAuthPassword);
         */

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
