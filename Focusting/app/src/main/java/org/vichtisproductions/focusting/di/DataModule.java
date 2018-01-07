package org.vichtisproductions.focusting.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

import org.vichtisproductions.focusting.BuildConfig;
import org.vichtisproductions.focusting.R;

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

        String serverName="";

        FirebaseAuth mAuth;
        FirebaseAuth.AuthStateListener mAuthListener;
        mAuth = FirebaseAuth.getInstance();

        final String SP_KEY_INITIAL_FB_USERNAME = "FirebaseUsername";
        final String SP_KEY_INITIAL_FB_PASSWORD = "FirebasePassword";
        final String SP_NAME = "InitialStartupSP";
        SharedPreferences mPrefs;
        mPrefs = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        String fbAuthPasswordDefault, fbAuthUsernameDefault;
        fbAuthPasswordDefault = context.getResources().getString(R.string.ReleasePassword);
        fbAuthUsernameDefault = context.getResources().getString(R.string.ReleaseUsername);
        serverName = context.getResources().getString(R.string.ReleaseServer);
        if (BuildConfig.DEBUG) {
            fbAuthPasswordDefault = context.getResources().getString(R.string.DebugPassword);
            fbAuthUsernameDefault = context.getResources().getString(R.string.DebugUsername);
            serverName = context.getResources().getString(R.string.DebugServer);
        }

        String fbAuthUsername = mPrefs.getString(SP_KEY_INITIAL_FB_USERNAME, fbAuthUsernameDefault);
        String fbAuthPassword = mPrefs.getString(SP_KEY_INITIAL_FB_PASSWORD, fbAuthPasswordDefault);

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
        String firebaseURL = serverName;
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
                            Timber.w(task.getException(), "signInWithEmail");
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
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
                .name("focusting.realm")
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
