package org.coderswithoutborders.deglancer.stagehandlers;

import android.content.Context;

import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.model.ScreenAction;

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

    }
}
