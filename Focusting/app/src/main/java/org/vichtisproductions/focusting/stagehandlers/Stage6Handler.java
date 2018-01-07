package org.vichtisproductions.focusting.stagehandlers;

import android.content.Context;
import android.content.Intent;

import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.model.ScreenAction;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.model.TriState;
import org.vichtisproductions.focusting.utils.ToastUtils;

import timber.log.Timber;

/**
 * Created by Renier on 2016/04/27.
 */
public class Stage6Handler implements IStageHandler {
    private Context mContext;
    private IDatabaseInteractor mDatabaseInteractor;

    public Stage6Handler(Context context, IDatabaseInteractor databaseInteractor) {
        mContext = context;
        mDatabaseInteractor = databaseInteractor;
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

            // Timber.d(String.format("We're in stage %d", action.getStage()));
            // TODO - Verify. Always compare to yesterday.
            Stage stageToCompareTo;
            Timber.d("It's " + Integer.toString(action.getDay()) + "th day");
            // Always compare to yesterday
            if (action.getDay() == 1 ) {
                stageToCompareTo = new Stage(action.getStage() - 1, 7, action.getHour());
            } else {
                stageToCompareTo = new Stage(action.getStage(), action.getDay() - 1, action.getHour());
            }

            // TODO - This should ideally calculate an average from the 7 day rolling average, not just from one day.
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

            TriState unlockState;
            TriState sotState;
            TriState sftState;

            if (unlockCount == unlockCountPreviousStage) {
                unlockState = new TriState(TriState.State.Same);
            } else if (unlockCount > unlockCountPreviousStage) {
                unlockState = new TriState(TriState.State.Worse);
            } else{
                unlockState = new TriState(TriState.State.Better);
            }

            if (totalSOTTime == totalSOTTimePreviousStage) {
                sotState = new TriState(TriState.State.Same);
            } else if (totalSOTTime > totalSOTTimePreviousStage) {
                sotState = new TriState(TriState.State.Worse);
            } else {
                sotState = new TriState(TriState.State.Better);
            }

            if (avgSFTTime == avgSFTTimePreviousStage) {
                sftState = new TriState(TriState.State.Same);
            } else if (avgSFTTime > avgSFTTimePreviousStage) {
                sftState = new TriState(TriState.State.Better);
            } else {
                sftState = new TriState(TriState.State.Worse);
            }

            ToastUtils.showToast(mContext, action.getDuration(), unlockCount, totalSOTTime, unlockState, sotState, sftState, unlockDiffPercentage, sotDiffPercentage, sftDiffPercentage);
        }
    }
}
