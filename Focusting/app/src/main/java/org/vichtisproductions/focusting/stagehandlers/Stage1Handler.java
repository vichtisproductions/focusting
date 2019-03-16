package org.vichtisproductions.focusting.stagehandlers;

import android.content.Context;
import android.content.Intent;

import org.vichtisproductions.focusting.BuildConfig;
import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.model.ScreenAction;

import timber.log.Timber;

/**
 * Created by Renier on 2016/04/27.
 */
public class Stage1Handler implements IStageHandler {
    // Just record the data, don't show a stimulus
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
            if (action.getEventType().equals(Intent.ACTION_SCREEN_ON)) {
                Timber.d("Stage 1 - Screen on");
            } else if (action.getEventType().equals(Intent.ACTION_USER_PRESENT)) {
                Timber.d("Stage 1 - User is present");
            }
        }
    }
}
