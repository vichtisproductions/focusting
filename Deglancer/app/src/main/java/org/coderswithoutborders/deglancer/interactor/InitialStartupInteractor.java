package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;

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

import org.coderswithoutborders.deglancer.di.DataModule;
import org.coderswithoutborders.deglancer.model.UserInfo;
import org.joda.time.DateTime;

import timber.log.Timber;

import java.util.Date;
import java.util.UUID;

import rx.Observable;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

/**
 * Created by Renier on 2016/04/12.
 */
public class InitialStartupInteractor implements IInitialStartupInteractor {
    private static final String SP_NAME = "InitialStartupSP";
    private static final String SP_KEY_INSTANCE_ID = "InstanceId";
    private static final String SP_KEY_INITIAL_SETUP_DONE = "InitialSetupDone";
    private static final String SP_KEY_INITIAL_UPLOAD_SUCCEEDED = "InitialUploadSucceeded";
    private static final String SP_KEY_INITIAL_START_TIME = "InitialStartTime";
    // Extend Shared preferences to store all initial information in case uploading fails
    private static final String SP_KEY_INITIAL_MANUFACTURER = "Manufacturer";
    private static final String SP_KEY_INITIAL_MODEL = "Model";
    private static final String SP_KEY_INITIAL_OSVERSION = "osVersion";
    private static final String SP_KEY_INITIAL_FB_USERNAME = "FirebaseUsername";
    private static final String SP_KEY_INITIAL_FB_PASSWORD = "FirebasePassword";
    private String FirebaseUsername ="";
    private String FirebasePassword ="";

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
                    String manufacturer = Build.MANUFACTURER;
                    String model = Build.MODEL;
                    String osVersion = Build.VERSION.RELEASE;

                    mRxPrefs.getLong(SP_KEY_INITIAL_START_TIME).set(initialStartTime);
                    mRxPrefs.getString(SP_KEY_INITIAL_MANUFACTURER).set(manufacturer);
                    mRxPrefs.getString(SP_KEY_INITIAL_MODEL).set(model);
                    mRxPrefs.getString(SP_KEY_INITIAL_OSVERSION).set(osVersion);
                    mRxPrefs.getBoolean(SP_KEY_INITIAL_SETUP_DONE).set(true);

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
                                            Timber.d( "signInWithEmail:onComplete:" + task.isSuccessful());
                                            // Get the current user
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Timber.d("Updating Instance ID to " + user.getUid());
                                            mUserInteractor.setInstanceIdSynchronous(user.getUid());
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
                    //TODO - handle error
                    String here = "";
                });
        Timber.d("End creating user.");


        // Here be the part that tries to upload the content
        // Assume that the content is always there in the mRxPrefs
        // First read UPLOAD_SUCCEEDED, ...

        if (mPrefs.getBoolean(SP_KEY_INITIAL_UPLOAD_SUCCEEDED, false) == false) {
            // Otherwise upload it...
            // Upload it here
            // First you need to create new UserInfo object out of SharedPreferences
            Timber.d("Not uploaded before, let's go and do it.");
            String instanceId = mUserInteractor.getInstanceIdSynchronous();
            Timber.d("Using new InstanceID for the first time: " + instanceId);
            Long initialStartTimefromSP = mPrefs.getLong(SP_KEY_INITIAL_START_TIME, 0);
            String manufacturerfromSP = mPrefs.getString(SP_KEY_INITIAL_MANUFACTURER, "");
            String modelfromSP = mPrefs.getString(SP_KEY_INITIAL_MODEL, "");
            String osVersionfromSP = mPrefs.getString(SP_KEY_INITIAL_OSVERSION, "");
            UserInfo ui = new UserInfo(instanceId, initialStartTimefromSP, manufacturerfromSP, modelfromSP, osVersionfromSP);
            Timber.d("User info from SP: " + initialStartTimefromSP.toString() + ", " + manufacturerfromSP + ", " + modelfromSP + ", " + osVersionfromSP);

            Timber.d("Uploading UserInfo to FireBase");
            DatabaseReference ref = mFirebaseClient.child(instanceId).child("InitialInformation");

            // BEGIN New thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Timber.d("Sleeping for some seconds.");
                        // Let's give some time for the authentication.
                        Thread.sleep(5000);
                        Timber.d("Setting Userinfo now.");
                        ref.setValue(ui);
                        Timber.d("Userinfo now set.");
                    } catch (Exception e) {

                    }
                    Timber.d("Annnd, he woke up from a long sleep.");
                    // Read the values down and check if upload was successful and values are not null
                    // [START CHECK DATA]

                    final String userId = mUserInteractor.getInstanceIdSynchronous();
                    Timber.d("Instance ID is " + userId);
                    FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("InitialInformation")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user data from Firebase
                                    Timber.d("Getting user data from Firebase for comparison");
                                    UserInfo uiFromFirebase = dataSnapshot.getValue(UserInfo.class);
                                    String instanceIdFromFB = uiFromFirebase.getInstanceId();
                                    Long initialStartTimeFromFB = uiFromFirebase.getInitialStartTime();
                                    String manufacturer = uiFromFirebase.getManufacturer();
                                    String model = uiFromFirebase.getModel();
                                    String osVersion = uiFromFirebase.getOSVersion();
                                    Timber.d("User info from Firebase: Ins:" + instanceIdFromFB + ", Start:" + initialStartTimeFromFB.toString() + ", Manu: " + manufacturer + ", Model: " + model + ", OS:" + osVersion);

                                    if (instanceIdFromFB == null) {
                                        // User is null, error out
                                        Timber.e("User " + userId + " is unexpectedly null");
                                    } else {
                                        // This means is not null, compare to SP content
                                        Timber.d("Start from FB:" + initialStartTimeFromFB.toString());
                                        Timber.d("Start from SP:" + initialStartTimefromSP.toString());
                                        if (initialStartTimeFromFB.longValue() == initialStartTimefromSP.longValue()) {
                                            Timber.d("Initial start time matches between Shared Preferences and Firebase. All is well.");
                                            mRxPrefs.getBoolean(SP_KEY_INITIAL_UPLOAD_SUCCEEDED).set(true);
                                        } else {
                                            // if not, set SUCCEEDED as false. Otherwise set as true.
                                            Timber.d("Initial start time does NOT match between Shared Preferences and Firebase. All is belly up. Let's try again next time.");
                                            mRxPrefs.getBoolean(SP_KEY_INITIAL_UPLOAD_SUCCEEDED).set(false);
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Timber.w(databaseError.toException(), "getUser:onCancelled");

                                }
                                // End of Add valueevent listener
                            });
                    // END CHECK DATA
                }
            }).start();
            // END new Thread

        }
        // end of If (mPrefs = false)

    }

    @Override
    public long getInitialStartTime() {
        return mPrefs.getLong(SP_KEY_INITIAL_START_TIME, new DateTime().getMillis());
    }

    @Override
    public void overrideInitialStartTime(long newStartTime) {
        mPrefs.edit().putLong(SP_KEY_INITIAL_START_TIME, newStartTime).commit();
    }
}
