package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.joda.time.DateTime;

import java.util.UUID;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public class UserInteractor implements IUserInteractor {
    private static final String SP_NAME = "UserInteractorSP";
    private static final String SP_KEY_INSTANCE_ID = "InstanceId";
    private static final String TAG ="UserInteractor";

    private Context mContext;
    private DatabaseReference mFirebaseClient;

    public UserInteractor(Context context, DatabaseReference firebaseClient) {
        mContext = context;
        mFirebaseClient = firebaseClient;
    }

    @Override
    public Observable<String> getInstanceId() {
        return Observable.defer(() -> Observable.create(subscriber -> {
            subscriber.onNext(getInstanceIdSynchronous());
            subscriber.onCompleted();
        }));
    }

    @Override
    public String getInstanceIdSynchronous() {
        SharedPreferences prefs = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        String instanceId;
        if (!prefs.contains(SP_KEY_INSTANCE_ID)) {
            instanceId = String.valueOf(UUID.randomUUID());
            // Use user.getUid() instead.
            // instanceId = getFirebaseUid();
            prefs.edit().putString(SP_KEY_INSTANCE_ID, instanceId).apply();
        } else {
            instanceId = prefs.getString(SP_KEY_INSTANCE_ID, "");
        }

        return instanceId;
    }

    @Override
    public void logLastUserInteraction() {
        Observable.create(subscriber -> {
            DatabaseReference ref = mFirebaseClient.child(getInstanceIdSynchronous()).child("LastUserInteraction");
            ref.setValue(new DateTime().toString("yyyy/MM/dd kk:mm:ss"));

            subscriber.onCompleted();
        })
                .subscribe(result -> {

                }, error -> {

                });
    }

    /*
    public FirebaseAuth.AuthStateListener authFirebase() {

        FirebaseAuth.AuthStateListener mAuthListener;

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        return mAuthListener;

    }
    */

    public String getFirebaseUid() {

        String username = "";

        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            username = user.getUid();
        } else {
            Log.d(TAG, "User not found");
        }
        return username;
    }

}
