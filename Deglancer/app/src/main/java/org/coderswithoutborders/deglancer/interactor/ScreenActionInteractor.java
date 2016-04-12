package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.Firebase;

import org.coderswithoutborders.deglancer.bus.events.ActionEvent;
import org.coderswithoutborders.deglancer.model.ScreenAction;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by Renier on 2016/04/04.
 */
public class ScreenActionInteractor implements IScreenActionInteractor {

    private static final String TAG = "ScreenActionInteractor";

    private Context mContext;
    private Firebase mFirebaseClient;
    private IStageInteractor mStageInteractor;
    private Realm mRealm;
    private IUserInteractor mUserInteractor;

    public ScreenActionInteractor(Context context, Firebase firebaseClient, IStageInteractor stageInteractor, Realm realm, IUserInteractor userInteractor) {
        mContext = context;
        mFirebaseClient = firebaseClient;
        mStageInteractor = stageInteractor;
        mRealm = realm;
        mUserInteractor = userInteractor;
    }

    @Override
    public void handleScreenAction(ActionEvent action) {
        mStageInteractor.getCurrentStage()
                .subscribe(stage -> {
                    ScreenAction screenAction = new ScreenAction(
                            String.valueOf(UUID.randomUUID()),
                            action.getAction(),
                            new Date().getTime(),
                            stage
                            );

                    mRealm.beginTransaction();
                    mRealm.copyToRealm(screenAction);
                    mRealm.commitTransaction();

                    Firebase ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("ScreenEvents");
                    ref.push().setValue(screenAction);

                    //TODO - add delta times here

                    //TODO - show toasts here (depending on stage)


                }, error -> {
                    //TODO - Handle error
                });


        if (action.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i(TAG, "Screen Event - " + "LOCKED");
        } else if (action.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i(TAG, "Screen Event - " + "SCREEN_ON");
        } else if (action.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.i(TAG, "Screen Event - " + "USER_PRESENT");
        }  else if (action.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Log.i(TAG, "Screen Event - " + "POWER_CONNECTED");
        } else if (action.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Log.i(TAG, "Screen Event - " + "POWER_DISCONNECTED");
        }
    }
}
