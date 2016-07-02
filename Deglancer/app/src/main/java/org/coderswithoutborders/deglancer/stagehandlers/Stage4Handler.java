package org.coderswithoutborders.deglancer.stagehandlers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.interactor.ITargetInteractor;
import org.coderswithoutborders.deglancer.model.ScreenAction;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.model.TriState;
import org.coderswithoutborders.deglancer.utils.TimeUtils;
import org.coderswithoutborders.deglancer.utils.ToastUtils;
import org.coderswithoutborders.deglancer.stagehandlers.Keystore;
import org.coderswithoutborders.deglancer.view.MainActivity;

/**
 * Created by Renier on 2016/04/27.
 */
public class Stage4Handler implements IStageHandler {
    private Context mContext;
    private IDatabaseInteractor mDatabaseInteractor;
    private ITargetInteractor mTargetInteractor;

    private Keystore store;//Holds our key pairs

    // Notification
    private NotificationManager notifyMgr = null;
    private static final int NOTIFY_ME_ID = 31337;
    private static final String TAG = "Deglancer.Stage4Handler";


    public Stage4Handler(Context context, IDatabaseInteractor databaseInteractor, ITargetInteractor targetInteractor) {
        mContext = context;
        mDatabaseInteractor = databaseInteractor;
        mTargetInteractor = targetInteractor;
    }

    @Override
    public void handleScreenAction(ScreenAction action) {
        if (action.getEventType().equals(Intent.ACTION_SCREEN_ON)) {

            //get unlock count up to current hour for this stage day
            long unlockCount = mDatabaseInteractor.getUnlockCountForStage(action.getStage(), action.getDay(), action.getHour());

            //get totalSOTTime up to current hour for this stage day
            long totalSOTTime = mDatabaseInteractor.getTotalSOTForStage(action.getStage(), action.getDay(), action.getHour());

            //get average sft time up to current hour for this stage day
            double avgSFTTime = mDatabaseInteractor.getAverageSFTForStage(action.getStage(), action.getDay(), action.getHour());
            // override avgSFTTimePreviousStage with last SFT
            long sinceLastLock = action.getDuration();
            avgSFTTime = (double) sinceLastLock;


            Stage stageToCompareTo = new Stage(action.getStage() - 1, action.getDay(), action.getHour());
            //get unlock count up to current hour in previous stage
            long unlockCountPreviousStage = mDatabaseInteractor.getUnlockCountForStageFromAverages(stageToCompareTo.getStage(), stageToCompareTo.getDay(), stageToCompareTo.getHour());

            //get totalSOTTime up to current hour in previous stage
            long totalSOTTimePreviousStage = mDatabaseInteractor.getTotalSOTForStageFromAverages(stageToCompareTo.getStage(), stageToCompareTo.getDay(), stageToCompareTo.getHour());

            //get average sft time up to current hour in previous stage
            double avgSFTTimePreviousStage = mDatabaseInteractor.getAverageSFTForStageFromAverages(stageToCompareTo.getStage(), stageToCompareTo.getDay(), stageToCompareTo.getHour());


            double unlockPercentage = unlockCount / (unlockCountPreviousStage > 0 ? unlockCountPreviousStage : unlockCount);
            double sotPercentage = totalSOTTime / (totalSOTTimePreviousStage > 0 ? totalSOTTimePreviousStage : totalSOTTime);
            double sftPercentage = avgSFTTime / (avgSFTTimePreviousStage > 0 ? avgSFTTimePreviousStage : avgSFTTime);


            double unlockDiffPercentage = unlockCount >= unlockCountPreviousStage ? (unlockPercentage - 1) * 100 : (1 - unlockPercentage) * 100;
            double sotDiffPercentage = totalSOTTime >= totalSOTTimePreviousStage ? (sotPercentage - 1) * 100 : (1 - sotPercentage) * 100;
            double sftDiffPercentage = avgSFTTime >= avgSFTTimePreviousStage ? (sftPercentage - 1) * 100 : (1 - sftPercentage) * 100;

            int targetPercentage = mTargetInteractor.getTargetForStageSynchronous(action.getStage());

            // calculate targets for this stage.
            // Lapa 2016/6/216
            long targetUnlockCount = (long) (unlockCountPreviousStage * (100 - targetPercentage) / 100);
            long targetSOTTime = (long) (totalSOTTimePreviousStage * (100 - targetPercentage) / 100);
            long targetSFTTime = (long) (avgSFTTimePreviousStage * (100 + targetPercentage) / 100);

            TriState unlockState;
            TriState sotState;
            TriState sftState;


/**
 if (unlockCount == unlockCountPreviousStage) {
 unlockState = new TriState(TriState.State.Same);
 } else if (unlockCount > unlockCountPreviousStage) {
 unlockState = new TriState(TriState.State.Worse);
 } else{
 if (unlockDiffPercentage >= targetPercentage) {
 unlockState = new TriState(TriState.State.Better);
 } else {
 unlockState = new TriState(TriState.State.Same);
 }
 }
 */

            // Recalculate TriState for unlocks
            // Lapa 2016/6/21
            if (unlockCount == targetUnlockCount) {
                unlockState = new TriState(TriState.State.Same);
            } else if (unlockCount > targetUnlockCount) {
                unlockState = new TriState(TriState.State.Worse);
            } else {
                unlockState = new TriState(TriState.State.Better);
            }

/**
 if (totalSOTTime == totalSOTTimePreviousStage) {
 sotState = new TriState(TriState.State.Same);
 } else if (totalSOTTime > totalSOTTimePreviousStage) {
 sotState = new TriState(TriState.State.Worse);
 } else {
 if (sotDiffPercentage >= targetPercentage) {
 sotState = new TriState(TriState.State.Better);
 } else {
 sotState = new TriState(TriState.State.Same);
 }
 }
 */

            // Recalculate TriState for SOT
            // Lapa 2016/6/21
            if (totalSOTTime == targetSOTTime) {
                sotState = new TriState(TriState.State.Same);
            } else if (totalSOTTime > targetSOTTime) {
                sotState = new TriState(TriState.State.Worse);
            } else {
                sotState = new TriState(TriState.State.Better);
            }

/**
 if (avgSFTTime == avgSFTTimePreviousStage) {
 sftState = new TriState(TriState.State.Same);
 } else if (avgSFTTime > avgSFTTimePreviousStage) {
 if (sftDiffPercentage >= targetPercentage) {
 sftState = new TriState(TriState.State.Better);
 } else {
 sftState = new TriState(TriState.State.Same);
 }
 } else {
 sftState = new TriState(TriState.State.Worse);
 }
 */

            // Recalculate TriState for SFT
            // Lapa 2016/6/21
            if (sinceLastLock == targetSFTTime) {
                sftState = new TriState(TriState.State.Same);
            } else if (sinceLastLock > targetSFTTime) {
                sftState = new TriState(TriState.State.Better);
            } else {
                sftState = new TriState(TriState.State.Worse);
            }


            ToastUtils.showToast(mContext, action.getDuration(), unlockCount, totalSOTTime, unlockState, sotState, sftState, unlockDiffPercentage, sotDiffPercentage, sftDiffPercentage, targetPercentage);

            // Here  be notification if needed
            goalSelectNotifier();

        }
    }

    public void goalSelectNotifier() {

        store = Keystore.getInstance(mContext);//Creates or Gets our key pairs.  You MUST have access to current context!

        // Get target for stage, NULL means no target. That means let's notify
        // 1. get int (number of notifications done) - if more than 5, do nothing
        if (mDatabaseInteractor.getTargetForStage(4) == null) {
            if ((store.getInt("numOfNotifications") < 5) && (TimeUtils.getHowManyHoursAgo(store.getLong("timeLastNotification")) > 4)) {
                // Log.d(TAG, "Let's notify: " + Integer.toString(store.getInt("numOfNotifications")) + " - " + TimeUtils.getHowManyHoursAgo(store.getLong("timeLastNotification")));
                // 2. if less than 5 times and less than 4 hours ago, invoke new one
                notifyMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent resultIntent = new Intent(mContext, MainActivity.class);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                mContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Builder notifyObj =
                        new NotificationCompat.Builder(mContext)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Deglancer")
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setContentText("Select your goal for this week");

                notifyMgr.notify(NOTIFY_ME_ID, notifyObj.build());

                // Increase number of notifications by one + update last notification time
                // Log.d(TAG, "Updating time: " + Integer.toString(store.getInt("numOfNotifications")) + " - " + Long.toString(System.currentTimeMillis()));
                store.putInt("numOfNotifications", store.getInt("numOfNotifications") + 1);
                store.putLong("timeLastNotification", System.currentTimeMillis());
            }
        }
        /*
        else {
            Log.d(TAG, "No need to notify right now: " + Integer.toString(store.getInt("numOfNotifications")) + " - " + TimeUtils.getHowManyHoursAgo(store.getLong("timeLastNotification")));
            if (store.getInt("numOfNotifications") == 5) {
                Log.d(TAG, "Reset for fun.");
                store.putInt("numOfNotifications", 1);
            }
        }
        */
    }

}
