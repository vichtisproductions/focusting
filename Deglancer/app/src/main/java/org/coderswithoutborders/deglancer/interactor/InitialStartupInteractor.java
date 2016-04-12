package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.firebase.client.Firebase;

import org.coderswithoutborders.deglancer.model.UserInfo;

import java.util.Date;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public class InitialStartupInteractor implements IInitialStartupInteractor {
    private static final String SP_NAME = "InitialStartupSP";
    private static final String SP_KEY_INITIAL_SETUP_DONE = "InitialSetupDone";

    private Context mContext;
    private Firebase mFirebaseClient;
    private IUserInteractor mUserInteractor;

    SharedPreferences mPrefs;
    private RxSharedPreferences mRxPrefs;

    public InitialStartupInteractor(Context context, Firebase firebaseClient, IUserInteractor userInteractor) {
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
                    long initialStartTime = new Date().getTime();
                    String manufacturer = Build.MANUFACTURER;
                    String model = Build.MODEL;
                    String osVersion = Build.VERSION.RELEASE;

                    UserInfo ui = new UserInfo(instanceId, initialStartTime, manufacturer, model, osVersion);


                    Firebase ref = mFirebaseClient.child(instanceId);
                    ref.setValue(ui);

                    mRxPrefs.getBoolean(SP_KEY_INITIAL_SETUP_DONE).set(true);

                    return Observable.empty();
                })
                .subscribe(result -> {
                    String here = "";


                }, error -> {
                    //Already captured or some other error
                    String here = "";
                });
    }
}
