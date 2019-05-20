package org.vichtisproductions.focusting.interactor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;

import org.vichtisproductions.focusting.bus.events.ActionEvent;
import org.vichtisproductions.focusting.model.Averages;
import org.vichtisproductions.focusting.model.ScreenAction;
import org.vichtisproductions.focusting.model.Stage;
import org.joda.time.DateTime;
import org.vichtisproductions.focusting.utils.CalendarUtils;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import timber.log.Timber;

/**
 * Created by Renier on 2016/04/04.
 */
public class ScreenActionInteractor implements IScreenActionInteractor {

    private static final String TAG = "ScreenActionInteractor";

    private Context mContext;
    private DatabaseReference mFirebaseClient;
    private IStageInteractor mStageInteractor;
    private Realm mRealm;
    private IUserInteractor mUserInteractor;
    private IDatabaseInteractor mDatabaseInteractor;

    private FirebaseAnalytics mFirebaseAnalytics;

    public ScreenActionInteractor(Context context, DatabaseReference firebaseClient, IStageInteractor stageInteractor, Realm realm, IUserInteractor userInteractor, IDatabaseInteractor databaseInteractor) {
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
                            duration,
                            CalendarUtils.getAttendeeCount(mContext)
                            );

                    mDatabaseInteractor.commitScreenAction(screenAction);

                    DatabaseReference ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("ScreenEvents");
                    ref.push().setValue(screenAction);

                    if (mStageInteractor.getCurrentStageHandler() != null) {
                        mStageInteractor.getCurrentStageHandler().handleScreenAction(screenAction);
                    }

                    mUserInteractor.logLastUserInteraction();
                }, error -> {
                    //Handle error
                    String here = "";
                });


        if (action.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Timber.i( "Screen Event - " + "LOCKED");
        } else if (action.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Timber.i( "Screen Event - " + "SCREEN_ON");
        } else if (action.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Timber.i( "Screen Event - " + "USER_PRESENT");
        }  else if (action.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Timber.i( "Screen Event - " + "POWER_CONNECTED");
        } else if (action.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Timber.i( "Screen Event - " + "POWER_DISCONNECTED");
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
                long totalSFT = mDatabaseInteractor.getTotalSFTForStage(action.getStage(), action.getDay(), action.getHour());
                long totalSOT = mDatabaseInteractor.getTotalSOTForStage(action.getStage(), action.getDay(), action.getHour());

                Averages avg = new Averages(
                        UUID.randomUUID().toString(),
                        action.getStage(),
                        action.getDay(),
                        action.getHour(),
                        unlockCount,
                        sft,
                        sot,
                        totalSFT,
                        totalSOT
                );
                mDatabaseInteractor.commitAverages(avg);

                int currStage = stage.getStage();
                int currDay = action.getDay();

                mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
                Bundle fbAnalyticsBundle = new Bundle();

                if (action.getStage() != stage.getStage()) {
                    switch (action.getStage()) {
                        case 1:
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.GENERATE_LEAD, null);
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.LEVEL, Integer.toString(action.getStage()));
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, fbAnalyticsBundle);
                        case 2:
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "stage_" + Integer.toString(action.getStage()));
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Stage 2");
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Stage 2");
                            fbAnalyticsBundle.putLong(FirebaseAnalytics.Param.QUANTITY, 1L);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, fbAnalyticsBundle);
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.LEVEL, Integer.toString(action.getStage()));
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, fbAnalyticsBundle);
                        case 3:
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "stage_" + Integer.toString(action.getStage()));
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Stage 3");
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Stage 3");
                            fbAnalyticsBundle.putLong(FirebaseAnalytics.Param.QUANTITY, 1L);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.PRESENT_OFFER, fbAnalyticsBundle);
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.LEVEL, Integer.toString(action.getStage()));
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, fbAnalyticsBundle);
                       case 4:
                           mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, null);
                           fbAnalyticsBundle.clear();
                           fbAnalyticsBundle.putString(FirebaseAnalytics.Param.LEVEL, Integer.toString(action.getStage()));
                           mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, fbAnalyticsBundle);
                        case 5:
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putLong(FirebaseAnalytics.Param.SCORE, 5L);
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE, fbAnalyticsBundle);
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.LEVEL, Integer.toString(action.getStage()));
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, fbAnalyticsBundle);
                        case 6:
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "stage_" + Integer.toString(currStage));
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, fbAnalyticsBundle);
                            fbAnalyticsBundle.clear();
                            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.LEVEL, Integer.toString(action.getStage()));
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, fbAnalyticsBundle);
                    }
                }
                if (action.getDay() != stage.getDay()) {
                    fbAnalyticsBundle.clear();
                    fbAnalyticsBundle.putString(FirebaseAnalytics.Param.GROUP_ID, "Day " + Integer.toString(currDay));
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.JOIN_GROUP, fbAnalyticsBundle);
                }

                DatabaseReference ref = mFirebaseClient.child(mUserInteractor.getInstanceIdSynchronous()).child("Averages");
                ref.push().setValue(avg);
            }
        }
    }
}
