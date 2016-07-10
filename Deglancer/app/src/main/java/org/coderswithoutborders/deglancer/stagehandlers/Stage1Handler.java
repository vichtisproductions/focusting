package org.coderswithoutborders.deglancer.stagehandlers;

import android.content.Context;
import android.content.Intent;

import org.coderswithoutborders.deglancer.BuildConfig;
import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.model.ScreenAction;
import org.coderswithoutborders.deglancer.utils.ToastUtils;

/**
 * Created by Renier on 2016/04/27.
 */
public class Stage1Handler implements IStageHandler {
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
