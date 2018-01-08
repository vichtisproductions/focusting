package org.vichtisproductions.focusting.func_debug.stage6;

import org.vichtisproductions.focusting.func_debug.stage3.IDebugStage3Presenter;
import org.vichtisproductions.focusting.func_debug.stage3.IDebugStage3View;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.model.Stage;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage6Presenter implements IDebugStage6Presenter {
    private IDebugStage6View mView;
    private IStageInteractor mStageInteractor;

    private Stage mCurrentStage;


    public DebugStage6Presenter(IStageInteractor stageInteractor) {
        this.mStageInteractor = stageInteractor;
    }

    @Override
    public void setView(IDebugStage6View view) {
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
                    //Handle error
                });
    }

    @Override
    public void onDetached() {

    }

    @Override
    public void previousStageClicked() {
        mStageInteractor.goToPreviousStage();

        if (mView != null) {
            mView.moveToStage5View();
            mView.finishActivity();
        }
    }
}
