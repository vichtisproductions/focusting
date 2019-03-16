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

import timber.log.Timber;

public class Stage2Group3Handler implements IStageHandler {
    private final Context mContext;
    private final DataUtils dataUtils;

    @Inject
    public Stage2Group3Handler(Context context, DataUtils dataUtils) {
        mContext = context;
        this.dataUtils = dataUtils;
    }

    @Override
    public void handleScreenAction(ScreenAction action) {
        if (action.getEventType().equals(Intent.ACTION_USER_PRESENT)) {
            final int hourOfDay = DateTime.now().hourOfDay().get();
            final int attendees = CalendarUtils.getAttendeeCount(mContext);
            if (hourOfDay >= 7 && hourOfDay <= 17 && attendees >= 3) {
                Timber.d("Showing toast for group 3");
                ToastUtils.showToast(mContext, dataUtils.getUsername(), R.string.toast2_content);
            } else {
                Timber.d("Cannot show toast for group 3, hour: %d, attendees: %d", hourOfDay, attendees);
            }
        }
    }
}