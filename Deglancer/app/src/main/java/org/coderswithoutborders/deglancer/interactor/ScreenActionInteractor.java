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
    private IDatabaseInteractor mDatabaseInteractor;

    public ScreenActionInteractor(Context context, Firebase firebaseClient, IStageInteractor stageInteractor, Realm realm, IUserInteractor userInteractor, IDatabaseInteractor databaseInteractor) {
        mContext = context;
        mFirebaseClient = firebaseClient;
        mStageInteractor = stageInteractor;
        mRealm = realm;
        mUserInteractor = userInteractor;
        mDatabaseInteractor = databaseInteractor;
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
                        duration = getDurationFromLastEventOfType(Intent.ACTION_SCREEN_OFF, dateTime);
                    } else if (action.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF)) {
                        duration = getDurationFromLastEventOfType(Intent.ACTION_SCREEN_ON, dateTime);
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

                    mDatabaseInteractor.commitScreenAction(screenAction);

                    Firebase ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("ScreenEvents");
                    ref.push().setValue(screenAction);

                    if (mStageInteractor.getCurrentStageHandler() != null) {
                        mStageInteractor.getCurrentStageHandler().handleScreenAction(screenAction);
                    }

                    mUserInteractor.logLastUserInteraction();
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

    private long getDurationFromLastEventOfType(String action, long newEventDateTime) {
        ScreenAction screenAction = mDatabaseInteractor.getLastScreenActionOfType(action);

        if (screenAction != null) {
            return newEventDateTime - screenAction.getEventDateTime();
        } else {
            return -1;
        }
    }

    private void updateAveragesIfStageHasChanged(Stage stage) {
        ScreenAction action = mDatabaseInteractor.getLastScreenAction();

        if (action != null) {
            if (action.getStage() != stage.getStage() || action.getDay() != stage.getDay() || action.getHour() != stage.getHour()) {
                //The stage has changed, update the averages
                long unlockCount = mDatabaseInteractor.getUnlockCountForStage(action.getStage(), action.getDay(), action.getHour());
                double sft = mDatabaseInteractor.getAverageSFTForStage(action.getStage(), action.getDay(), action.getHour());
                double sot = mDatabaseInteractor.getAverageSOTForStage(action.getStage(), action.getDay(), action.getHour());

                Averages avg = new Averages(
                        UUID.randomUUID().toString(),
                        action.getStage(),
                        action.getDay(),
                        action.getHour(),
                        unlockCount,
                        sft,
                        sot
                );
                mDatabaseInteractor.commitAverages(avg);

                Firebase ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("Averages");
                ref.push().setValue(avg);
            }
        }
    }
}
