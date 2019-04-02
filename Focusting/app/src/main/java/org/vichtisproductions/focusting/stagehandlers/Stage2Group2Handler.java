package org.vichtisproductions.focusting.stagehandlers;

import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.model.ScreenAction;
import org.vichtisproductions.focusting.utils.CalendarUtils;
import org.vichtisproductions.focusting.utils.DataUtils;
import org.vichtisproductions.focusting.utils.ToastUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class Stage2Group2Handler implements IStageHandler {
    private final Context mContext;
    private final DataUtils dataUtils;

    @Inject
    public Stage2Group2Handler(Context context, DataUtils dataUtils) {
        mContext = context;
        this.dataUtils = dataUtils;
    }

    @Override
    public void handleScreenAction(ScreenAction action) {
        if (action.getEventType().equals(Intent.ACTION_USER_PRESENT)) {
            final int hourOfDay = DateTime.now().hourOfDay().get();
            final int attendees = CalendarUtils.getAttendeeCount(mContext);
            if (hourOfDay >= 7 && hourOfDay <= 17 && attendees >= 3) {
                Timber.d("Showing toast for group 2");
                ToastUtils.showToast(mContext, dataUtils.getUsername(), R.string.toast1_content);
            } else {
                Timber.d("Cannot show toast for group 2, hour: %d, attendees: %d", hourOfDay, attendees);
            }
        }
    }
}
