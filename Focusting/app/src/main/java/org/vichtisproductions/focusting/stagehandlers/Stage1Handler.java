package org.vichtisproductions.focusting.stagehandlers;

import android.content.Context;
import android.content.Intent;

import org.vichtisproductions.focusting.BuildConfig;
import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.model.ScreenAction;
import org.vichtisproductions.focusting.utils.ToastUtils;

/**
 * Created by Renier on 2016/04/27.
 */
public class Stage1Handler implements IStageHandler {
    // TODO - Make this a handler for stage 1
    private Context mContext;
    private IDatabaseInteractor mDatabaseInteractor;

    public Stage1Handler(Context context, IDatabaseInteractor databaseInteractor) {
        mContext = context;
        mDatabaseInteractor = databaseInteractor;
    }

    @Override
    public void handleScreenAction(ScreenAction action) {
        //Only display for dev purposes
        if (BuildConfig.DEBUG) {
            if (action.getEventType().equals(Intent.ACTION_SCREEN_ON)
                    || action.getEventType().equals(Intent.ACTION_USER_PRESENT)) {
                long unlockCount = mDatabaseInteractor.getUnlockCountForStageDay(action.getStage(), action.getDay());
                long totalSOTTime = mDatabaseInteractor.getTotalSOTForStageDay(action.getStage(), action.getDay());

                // ToastUtils.showToast(mContext, action.getDuration(), unlockCount, totalSOTTime);

//                StringBuilder sb = new StringBuilder();
//                sb.append(lastSleepString);
//                sb.append(System.getProperty("line.separator"));
//                sb.append(unlockString);
//                sb.append(System.getProperty("line.separator"));
//                sb.append(totalSOTTimeString);
//
//                Toast.makeText(mContext, sb.toString(), Toast.LENGTH_LONG).show();

            }
        }
    }
}
