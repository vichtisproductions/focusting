package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.Firebase;

import org.coderswithoutborders.deglancer.bus.events.ActionEvent;
import org.coderswithoutborders.deglancer.model.Averages;
import org.coderswithoutborders.deglancer.model.ScreenAction;
import org.coderswithoutborders.deglancer.model.Stage;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

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

                    updateAveragesIfStageHasChanged(stage);

                    long dateTime = new DateTime().getMillis();
                    long duration = -1;

                    //We only care about screen on / screen off when calculating SOT or SFT. The other events (charging, not charging, etc) are logged for possible future usage
                    if (action.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_ON)) {
                        duration = getDurationFromLastEventOfTypeInStage(Intent.ACTION_SCREEN_OFF, dateTime);
                    } else if (action.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF)) {
                        duration = getDurationFromLastEventOfTypeInStage(Intent.ACTION_SCREEN_ON, dateTime);
                    }

                    ScreenAction screenAction = new ScreenAction(
                            String.valueOf(UUID.randomUUID()),
                            action.getAction(),
                            dateTime,
                            stage.getStage(),
                            stage.getDay(),
                            stage.getHour(),
                            duration
                            );

                    mRealm.beginTransaction();
                    mRealm.copyToRealm(screenAction);
                    mRealm.commitTransaction();

                    Firebase ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("ScreenEvents");
                    ref.push().setValue(screenAction);



                    //TODO - show toasts here (depending on stage)


                }, error -> {
                    //TODO - Handle error
                    String here = "";
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

    private long getDurationFromLastEventOfTypeInStage(String action, long newEventDateTime) {
        RealmResults<ScreenAction> results = mRealm
                .where(ScreenAction.class)
                .equalTo("mEventType", action)
                .findAllSorted("mEventDateTime", Sort.DESCENDING);

        if (results.isValid() && results.size() > 0 && results.first() != null) {
            return newEventDateTime - results.first().getEventDateTime();
        } else {
            return -1;
        }
    }

    private void updateAveragesIfStageHasChanged(Stage stage) {
        RealmResults<ScreenAction> results = mRealm
                .where(ScreenAction.class)
                .findAllSorted("mEventDateTime", Sort.DESCENDING);

        if (results.isValid() && results.size() > 0 && results.first() != null) {
            ScreenAction action = results.first();

            if (action.getStage() != stage.getStage() || action.getDay() != stage.getDay() || action.getHour() != stage.getHour()) {
                //The stage has changed, update the averages
                long unlockCount = mRealm.where(ScreenAction.class)
                        .equalTo("mStage", action.getStage())
                        .equalTo("mDay", action.getDay())
                        .equalTo("mHour", action.getHour())
                        .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                        .count();

                double sft = mRealm.where(ScreenAction.class)
                        .equalTo("mStage", action.getStage())
                        .equalTo("mDay", action.getDay())
                        .equalTo("mHour", action.getHour())
                        .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                        .average("mDuration");

                double sot = mRealm.where(ScreenAction.class)
                        .equalTo("mStage", action.getStage())
                        .equalTo("mDay", action.getDay())
                        .equalTo("mHour", action.getHour())
                        .equalTo("mEventType", Intent.ACTION_SCREEN_OFF)
                        .average("mDuration");

                Averages avg = new Averages(
                        UUID.randomUUID().toString(),
                        stage.getStage(),
                        stage.getDay(),
                        stage.getHour(),
                        unlockCount,
                        sft,
                        sot
                );

                mRealm.beginTransaction();
                mRealm.copyToRealm(avg);
                mRealm.commitTransaction();

                Firebase ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("Averages");
                ref.push().setValue(avg);
            }
        }
    }
}
