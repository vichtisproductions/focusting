package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.firebase.database.DatabaseReference;

import org.coderswithoutborders.deglancer.di.DataModule;
import org.coderswithoutborders.deglancer.model.UserInfo;
import org.joda.time.DateTime;

import java.util.Date;

import rx.Observable;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

/**
 * Created by Renier on 2016/04/12.
 */
public class InitialStartupInteractor implements IInitialStartupInteractor {
    private static final String SP_NAME = "InitialStartupSP";
    private static final String SP_KEY_INITIAL_SETUP_DONE = "InitialSetupDone";
    private static final String SP_KEY_INITIAL_START_TIME = "InitialStartTime";

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
                    long initialStartTime = new DateTime().getMillis();
                    String manufacturer = Build.MANUFACTURER;
                    String model = Build.MODEL;
                    String osVersion = Build.VERSION.RELEASE;

                    UserInfo ui = new UserInfo(instanceId, initialStartTime, manufacturer, model, osVersion);

                    Log.d("InitialStartup","Here we go...");
                    // DatabaseReference ref = mFirebaseClient.child(instanceId);

                    // Wait 10 seconds
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DatabaseReference ref = mFirebaseClient.child(instanceId);
                                Log.d("InitialStartup", "Sleeping for 10 seconds.");
                                // Let's give some time for the authentication.
                                Thread.sleep(10000);
                                Log.d("InitialStartup","Setting Userinfo now.");
                                ref.setValue(ui);
                                Log.d("InitialStartup","Userinfo now set.");
                            } catch (Exception e) {

                            }
                            Log.d("InitialStartup", "Annnd, he woke up.");
                        }
                    }).start();

                    // String firebaseURL = "https://flickering-heat-4815.firebaseio.com/users/";
                    // DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL).child(instanceId);
                    // Log.d("InitialStartup","child is " + firebaseURL + "/" + instanceId);

                    mRxPrefs.getBoolean(SP_KEY_INITIAL_SETUP_DONE).set(true);
                    mRxPrefs.getLong(SP_KEY_INITIAL_START_TIME).set(initialStartTime);

                    return Observable.empty();
                })
                .subscribe(result -> {
                    String here = "";


                }, error -> {
                    //Already captured or some other error
                    //TODO - handle error
                    String here = "";
                });
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
