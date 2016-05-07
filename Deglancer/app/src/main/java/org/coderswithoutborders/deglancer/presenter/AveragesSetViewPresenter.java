package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.utils.TimeUtils;
import org.coderswithoutborders.deglancer.view.IAveragesSetView;

/**
 * Created by Renier on 2016/05/07.
 */
public class AveragesSetViewPresenter implements IAveragesSetViewPresenter {

    private IAveragesSetView mView;
    private IDatabaseInteractor mDatabaseInteractor;

    private int mStage;

    private int mAvgUnlocks = 0;
    private long mAvgSOT = 0;
    private long mAvgSFT = 0;

    public AveragesSetViewPresenter(IDatabaseInteractor databaseInteractor) {
        this.mDatabaseInteractor = databaseInteractor;
    }

    @Override
    public void setView(IAveragesSetView view) {
        mView = view;
    }

    @Override
    public void clearView() {
        mView = null;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }

    @Override
    public void setStage(int stage) {
        mStage = stage;
    }

    @Override
    public void saveClicked() {




    }

    @Override
    public void avgSFTPicked(long millis) {
        mAvgSFT = millis;

        if (mView != null) {
            mView.setAvgSFTText(TimeUtils.getTimeStringFromMillis(millis, false, true, true));
        }
    }

    @Override
    public void avgSOTPicked(long millis) {
        mAvgSOT = millis;

        if (mView != null) {
            mView.setAvgSOTText(TimeUtils.getTimeStringFromMillis(millis, false, true, true));
        }
    }

    @Override
    public void avgUnlocksPicked(int unlocks) {
        mAvgUnlocks = unlocks;
    }
}
