package org.coderswithoutborders.deglancer.stagehandlers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.model.ScreenAction;
import org.coderswithoutborders.deglancer.utils.TimeUtils;
import org.coderswithoutborders.deglancer.utils.ToastUtils;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

/**
 * Created by Renier on 2016/04/27.
 */
public class Stage2Handler implements IStageHandler {
    private Context mContext;
    private IDatabaseInteractor mDatabaseInteractor;

    public Stage2Handler(Context context, IDatabaseInteractor databaseInteractor) {
        mContext = context;
        mDatabaseInteractor = databaseInteractor;
    }

    @Override
    public void handleScreenAction(ScreenAction action) {
        if (action.getEventType().equals(Intent.ACTION_SCREEN_ON)) {
            long unlockCount = mDatabaseInteractor.getUnlockCountForStageDay(action.getStage(), action.getDay());
            long totalSOTTime = mDatabaseInteractor.getTotalSOTForStageDay(action.getStage(), action.getDay());

            String lastSleepString = mContext.getString(R.string.toast_last_sleep_label) + " " + TimeUtils.getTimeStringFromMillis(action.getDuration(), true, false, true, false);
            String unlockString = mContext.getString(R.string.toast_unlocks_today_label) + " " + unlockCount;
            String totalSOTTimeString = mContext.getString(R.string.toast_screen_on_today_label) + " " + TimeUtils.getTimeStringFromMillis(totalSOTTime, true, true, true, true);


            StringBuilder sb = new StringBuilder();
            sb.append(lastSleepString);
            sb.append(System.getProperty("line.separator"));
            sb.append(unlockString);
            sb.append(System.getProperty("line.separator"));
            sb.append(totalSOTTimeString);

            // Toast.makeText(mContext, sb.toString(), Toast.LENGTH_LONG).show();
            ToastUtils.showToastStage2(mContext,action.getDuration(), unlockCount, totalSOTTime);


        }
    }
}
