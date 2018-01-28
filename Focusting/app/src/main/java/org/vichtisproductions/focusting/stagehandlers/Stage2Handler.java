package org.vichtisproductions.focusting.stagehandlers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.model.ScreenAction;
import org.vichtisproductions.focusting.utils.CalendarUtils;
import org.vichtisproductions.focusting.utils.TimeUtils;
import org.vichtisproductions.focusting.utils.ToastUtils;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

/**
 * Created by Renier on 2016/04/27.
 */

//TODO - Make this handler a Group 1 handler for stage 2

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

            // TODO - Show stimulus type 1
            if (CalendarUtils.getAttendeeCount() > 1) {

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

                ToastUtils.showToast(mContext, action.getDuration(), unlockCount, totalSOTTime);
            }
        }
    }
}
