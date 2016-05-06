package org.coderswithoutborders.deglancer.func_debug.stage2;

import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.model.Stage;

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
        mStageInteractor
                .getCurrentStage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stage -> {
                   if (mView != null) {
                       mView.setStage(stage);
                   }
                }, error -> {
                    //TODO - handle error
                });
    }

    @Override
    public void onDetached() {

    }

    @Override
    public void onDayChange(int day) {
        if (mCurrentStage != null)
            mCurrentStage.setDay(day);

        if (mView != null) {
            mView.setStage(mCurrentStage);
            mView.refreshStats();
        }
    }

    @Override
    public void onHourChange(int hour) {
        if (mCurrentStage != null)
            mCurrentStage.setHour(hour);

        if (mView != null) {
            mView.setStage(mCurrentStage);
            mView.refreshStats();
        }
    }

    @Override
    public void advanceStageClicked() {
        mStageInteractor.goToNextStage();
    }

    @Override
    public void moveStageBackClicked() {
        mStageInteractor.goToPreviousStage();
    }
}
