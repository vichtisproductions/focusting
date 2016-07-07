package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
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

import rx.Observable;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

/**
 * Created by Renier on 2016/04/12.
 */
public class InitialStartupInteractor implements IInitialStartupInteractor {
    private static final String SP_NAME = "InitialStartupSP";
    private static final String SP_KEY_INITIAL_SETUP_DONE = "InitialSetupDone";
    private static final String SP_KEY_INITIAL_UPLOAD_SUCCEEDED = "InitialUploadSucceeded";
    private static final String SP_KEY_INITIAL_START_TIME = "InitialStartTime";
    // Extend Shared preferences to store all initial information in case uploading fails
    private static final String SP_KEY_INITIAL_MANUFACTURER = "Manufacturer";
    private static final String SP_KEY_INITIAL_MODEL = "Model";
    private static final String SP_KEY_INITIAL_OSVERSION = "osVersion";


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

                    return Observable.empty();
                })
                .subscribe(result -> {
                    String here = "";
                }, error -> {
                    //Already captured or some other error
                    //TODO - handle error
                    String here = "";
                });

        // Here be the part that tries to upload the content
        // Assume that the content is always there in the mRxPrefs
        // First read UPLOAD_SUCCEEDED, ...

        if (mPrefs.getBoolean(SP_KEY_INITIAL_UPLOAD_SUCCEEDED, false) == false) {
            // Otherwise upload it...
            // Upload it here
            // First you need to create new UserInfo object out of SharedPreferences
            Timber.d("Not uploaded before, let's go and do it.");
            String instanceId = mUserInteractor.getInstanceIdSynchronous();
            Long initialStartTimefromSP = mPrefs.getLong(SP_KEY_INITIAL_START_TIME, 0);
            String manufacturerfromSP = mPrefs.getString(SP_KEY_INITIAL_MANUFACTURER, "");
            String modelfromSP = mPrefs.getString(SP_KEY_INITIAL_MODEL, "");
            String osVersionfromSP = mPrefs.getString(SP_KEY_INITIAL_OSVERSION, "");
            UserInfo ui = new UserInfo(instanceId, initialStartTimefromSP, manufacturerfromSP, modelfromSP, osVersionfromSP);
            Timber.d("User info from SP: " + initialStartTimefromSP.toString() + ", " + manufacturerfromSP + ", " + modelfromSP + ", " + osVersionfromSP);

            Timber.d("Uploading UserInfo to FireBase");
            DatabaseReference ref = mFirebaseClient.child(instanceId).child("InitialInformation");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Timber.d("Sleeping for 10 seconds.");
                        // Let's give some time for the authentication.
                        Thread.sleep(10000);
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

            // end of If (mPrefs = false)
        }

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
