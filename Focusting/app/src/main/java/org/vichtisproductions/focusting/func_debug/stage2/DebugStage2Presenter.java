package org.vichtisproductions.focusting.func_debug.stage2;

import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.model.Stage;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        mStageInteractor.getCurrentStage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (mView != null) {
                        mView.setStage(result);
                        mView.setTitleStage(result.getStage() + "-" + result.getDay() + "-" + result.getHour());
                    }
                }, error -> {
                    //TODO - Handle error
                });
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
