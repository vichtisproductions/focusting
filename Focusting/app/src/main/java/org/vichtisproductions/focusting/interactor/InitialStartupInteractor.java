package org.vichtisproductions.focusting.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.format.Time;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.vichtisproductions.focusting.di.DataModule;
import org.vichtisproductions.focusting.model.UserInfo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import timber.log.Timber;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import rx.Observable;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

/**
 * Created by Renier on 2016/04/12.
 */
public class InitialStartupInteractor implements IInitialStartupInteractor {

    // Randomize participants to one of the three groups here
    int min = 1;
    int max = 3;
    Random r = new Random();
    int FocustingGroupNumber = r.nextInt(max - min + 1) + min;
    // String FocustingGroupNumber = String.valueOf(intFocustingGroupNumber);

    private static final String SP_NAME = "InitialStartupSP";
    private static final String SP_KEY_INSTANCE_ID = "InstanceId";
    private static final String SP_KEY_INITIAL_SETUP_DONE = "InitialSetupDone";
    private static final String SP_KEY_INITIAL_UPLOAD_SUCCEEDED = "InitialUploadSucceeded";
    private static final String SP_KEY_INITIAL_START_TIME = "InitialStartTime";
    private static final String SP_KEY_INITIAL_TIMEZONE = "InitialTimezone";
    private static final String SP_KEY_INITIAL_START_TIME_ZERO_HOUR = "InitialStartTimeZeroHour";
    // Extend Shared preferences to store all initial information in case uploading fails
    private static final String SP_KEY_INITIAL_MANUFACTURER = "Manufacturer";
    private static final String SP_KEY_INITIAL_MODEL = "Model";
    private static final String SP_KEY_INITIAL_OSVERSION = "osVersion";
    private static final String SP_KEY_INITIAL_FB_USERNAME = "FirebaseUsername";
    private static final String SP_KEY_INITIAL_FB_PASSWORD = "FirebasePassword";
    private static final String SP_KEY_FOCUSTING_GROUP_NUMBER = "FocustingGroupNumber";
    private String FirebaseUsername = "";
    private String FirebasePassword = "";

    private Context mContext;
    private DatabaseReference mFirebaseClient;
    private IUserInteractor mUserInteractor;
    private IDatabaseInteractor mDatabaseInteractor;

    SharedPreferences mPrefs;
    private RxSharedPreferences mRxPrefs;

    public InitialStartupInteractor(Context context, DatabaseReference firebaseClient, IUserInteractor userInteractor) {
        mContext = context;
        mFirebaseClient = firebaseClient;
        mUserInteractor = userInteractor;
        mPrefs = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mRxPrefs = RxSharedPreferences.create(mPrefs);
    }

    @Override
    public void captureInitialDataIfNotCaptured() {
        mRxPrefs.getBoolean(SP_KEY_INITIAL_SETUP_DONE, false)
                .asObservable()
                .flatMap(isCaptured -> {
                    if (!isCaptured) {
                        return mUserInteractor.getInstanceId();
                    } else {
                        return Observable.error(new Throwable("Already captured"));
                    }
                })
                .flatMap(instanceId -> {

                    // Get the data and store it away. You're done.
                    long initialStartTime = new DateTime().getMillis();
                    String userTimezone = Time.getCurrentTimezone();
                    // Determining the beginning of the day
                    DateTimeZone timeZone = DateTimeZone.forID( userTimezone );
                    DateTime now = DateTime.now( timeZone );
                    Timber.d("Timezone is " + userTimezone);
                    Timber.d("Initial start time: " + Long.toString(initialStartTime));
                    DateTime todayStart = now.withTimeAtStartOfDay();
                    Timber.d("Beginning of the day : " + Long.toString(todayStart.getMillis()));
                    Timber.d("Now overriding");
                    initialStartTime = todayStart.getMillis();
                    Timber.d("Initial start time now: " + Long.toString(initialStartTime));

                    String manufacturer = Build.MANUFACTURER;
                    String model = Build.MODEL;
                    String osVersion = Build.VERSION.RELEASE;

                    mRxPrefs.getLong(SP_KEY_INITIAL_START_TIME).set(initialStartTime);
                    mRxPrefs.getString(SP_KEY_INITIAL_MANUFACTURER).set(manufacturer);
                    mRxPrefs.getString(SP_KEY_INITIAL_MODEL).set(model);
                    mRxPrefs.getString(SP_KEY_INITIAL_OSVERSION).set(osVersion);
                    mRxPrefs.getBoolean(SP_KEY_INITIAL_SETUP_DONE).set(true);
                    mRxPrefs.getString(SP_KEY_INITIAL_TIMEZONE).set(userTimezone);
                    mRxPrefs.getLong(SP_KEY_INITIAL_START_TIME_ZERO_HOUR).set(initialStartTime);
                    mRxPrefs.getInteger(SP_KEY_FOCUSTING_GROUP_NUMBER).set(FocustingGroupNumber);

                    FirebaseUsername = mUserInteractor.getInstanceIdSynchronous() + "@bogusresearchusers.com";
                    mRxPrefs.getString(SP_KEY_INITIAL_FB_USERNAME).set(FirebaseUsername);
                    FirebasePassword = String.valueOf(UUID.randomUUID());
                    mRxPrefs.getString(SP_KEY_INITIAL_FB_PASSWORD).set(FirebasePassword);

                    // Oh boy, gotta create user
                    Timber.d("New user: " + FirebaseUsername + ", pwd: " + FirebasePassword);
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    // [END declare_auth]

                    // [START declare_auth_listener]
                    FirebaseAuth.AuthStateListener mAuthListener;
                    // [START auth_state_listener]
                    mAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                // User is signed in
                                Timber.d("onAuthStateChanged:signed_in:" + user.getUid());
                            } else {
                                // User is signed out
                                Timber.d("onAuthStateChanged:signed_out");
                            }
                        }
                    };
                    // [END auth_state_listener]

                    mAuth.addAuthStateListener(mAuthListener);

                    // [START create_user_with_email]
                    mAuth.createUserWithEmailAndPassword(FirebaseUsername, FirebasePassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Timber.d("createUserWithEmail:onComplete:" + task.isSuccessful());


                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Timber.d("Authentication failed.");
                                    }
                                }
                            });
                    // Make sure we sign out from the generic user.

                    // BEGIN New thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mAuth.signOut();
                                Timber.d("Sleeping to sign out.");
                                // Let's give some time for the authentication.
                                Thread.sleep(5000);
                            } catch (Exception e) {

                            }
                            Timber.d("Woke up from sign out.");
                            // Continuing

                            // Then log in with the new user
                            String fbAuthUsername = mPrefs.getString(SP_KEY_INITIAL_FB_USERNAME, "");
                            String fbAuthPassword = mPrefs.getString(SP_KEY_INITIAL_FB_PASSWORD, "");
                            mAuth.signInWithEmailAndPassword(fbAuthUsername, fbAuthPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Timber.d("signInWithEmail:onComplete:" + task.isSuccessful());
                                            // Get the current user
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Timber.d("Updating Instance ID to " + user.getUid());
                                            mUserInteractor.setInstanceIdSynchronous(user.getUid());

                                            // Upload data here -- create new UserInfo object out of SharedPreferences
                                            String instanceId = mUserInteractor.getInstanceIdSynchronous();
                                            Timber.d("Using new InstanceID for the first time: " + instanceId);
                                            Long initialStartTimefromSP = mPrefs.getLong(SP_KEY_INITIAL_START_TIME, 0);
                                            String manufacturerfromSP = mPrefs.getString(SP_KEY_INITIAL_MANUFACTURER, "");
                                            String modelfromSP = mPrefs.getString(SP_KEY_INITIAL_MODEL, "");
                                            String osVersionfromSP = mPrefs.getString(SP_KEY_INITIAL_OSVERSION, "");
                                            String userTimezonefromSP = mPrefs.getString(SP_KEY_INITIAL_TIMEZONE, "");
                                            int FocustingGroupNumberfromSP = mPrefs.getInt(SP_KEY_FOCUSTING_GROUP_NUMBER, 0);
                                            UserInfo ui = new UserInfo(instanceId, initialStartTimefromSP, manufacturerfromSP, modelfromSP, osVersionfromSP, userTimezonefromSP, FocustingGroupNumberfromSP);
                                            // Timber.d("User info from SP: " + instanceId + " " + initialStartTimefromSP.toString() + ", " + manufacturerfromSP + ", " + modelfromSP + ", " + osVersionfromSP);

                                            // Timber.d("Uploading UserInfo to FireBase");
                                            DatabaseReference ref = mFirebaseClient.child(instanceId).child("InitialInformation");
                                            ref.setValue(ui);

                                            // END Upload

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


                            // [END create_user_with_email]

                        }
                    }).start();
                    // END new Thread


                    return Observable.empty();


                })
                .subscribe(result -> {
                    String here = "";
                }, error -> {
                    //Already captured or some other error
                    //Handle error
                    String here = "";
                });
        // Timber.d("End creating user and uploading initial data.");

    }

    @Override
    public long getInitialStartTime() {
        return mPrefs.getLong(SP_KEY_INITIAL_START_TIME, new DateTime().getMillis());
    }

    @Override
    public int getGroupNumber() {
        return mPrefs.getInt(SP_KEY_FOCUSTING_GROUP_NUMBER, 0);
    }

    @Override
    public void overrideInitialStartTime(long newStartTime) {
        mPrefs.edit().putLong(SP_KEY_INITIAL_START_TIME, newStartTime).commit();
    }
}
