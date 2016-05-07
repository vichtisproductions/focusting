package org.coderswithoutborders.deglancer.func_debug.stage2;

import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.model.Stage;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage2Presenter implements IDebugStage1Presenter {
    private IDebugStage1View mView;
    private IStageInteractor mStageInteractor;

    private Stage mCurrentStage;


    public DebugStage2Presenter(IStageInteractor stageInteractor) {
        this.mStageInteractor = stageInteractor;
    }

    @Override
    public void setView(IDebugStage1View view) {
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
    }
}
