package org.vichtisproductions.focusting.stagehandlers;

import android.content.Context;

import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.model.ScreenAction;

/**
 * Created by Renier on 2016/04/27.
 */
public class Stage5Handler implements IStageHandler {
    private Context mContext;
    private IDatabaseInteractor mDatabaseInteractor;

    public Stage5Handler(Context context, IDatabaseInteractor databaseInteractor) {
        mContext = context;
        mDatabaseInteractor = databaseInteractor;
    }

    @Override
    public void handleScreenAction(ScreenAction action) {

    }
}
