package org.coderswithoutborders.deglancer.func_debug.stage2;

import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.model.Stage;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage2Presenter implements IDebugStage2Presenter {
    private IDebugStage2View mView;
    private IStageInteractor mStageInteractor;

    private Stage mCurrentStage;


    public DebugStage2Presenter(IStageInteractor stageInteractor) {
        this.mStageInteractor = stageInteractor;
    }

    @Override
    public void setView(IDebugStage2View view) {
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
    public void advanceStageClicked() {
        mStageInteractor.goToNextStage();

        if (mView != null) {
            mView.moveToStage3View();
            mView.finishActivity();
        }
    }

    @Override
    public void previousStageClicked() {
        mStageInteractor.goToPreviousStage();

        if (mView != null) {
            mView.moveToStage1View();
            mView.finishActivity();
        }
    }
}
