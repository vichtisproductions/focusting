package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.Firebase;

import org.joda.time.DateTime;

import java.util.UUID;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public class UserInteractor implements IUserInteractor {
    private static final String SP_NAME = "UserInteractorSP";
    private static final String SP_KEY_INSTANCE_ID = "InstanceId";

    private Context mContext;
    private Firebase mFirebaseClient;

    public UserInteractor(Context context, Firebase firebaseClient) {
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
            prefs.edit().putString(SP_KEY_INSTANCE_ID, instanceId).apply();
        } else {
            instanceId = prefs.getString(SP_KEY_INSTANCE_ID, "");
        }

        return instanceId;
    }

    @Override
    public void logLastUserInteraction() {
        Observable.create(subscriber -> {
            Firebase ref = mFirebaseClient.child(getInstanceIdSynchronous()).child("LastUserInteraction");
            ref.setValue(new DateTime().toString("yyyy/MM/dd kk:mm:ss"));

            subscriber.onCompleted();
        })
                .subscribe(result -> {

                }, error -> {

                });
    }
}
