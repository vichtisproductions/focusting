package org.vichtisproductions.focusting.func_debug.stage4;

import org.vichtisproductions.focusting.func_debug.stage3.IDebugStage3Presenter;
import org.vichtisproductions.focusting.func_debug.stage3.IDebugStage3View;
import org.vichtisproductions.focusting.func_debug.stage5.IDebugStage5Presenter;
import org.vichtisproductions.focusting.func_debug.stage5.IDebugStage5View;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.model.Stage;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage4Presenter implements IDebugStage4Presenter {
    private IDebugStage4View mView;
    private IStageInteractor mStageInteractor;

    private Stage mCurrentStage;


    public DebugStage4Presenter(IStageInteractor stageInteractor) {
        this.mStageInteractor = stageInteractor;
    }

    @Override
    public void setView(IDebugStage4View view) {
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
            mView.moveToStage5View();
            mView.finishActivity();
        }
    }

    @Override
    public void previousStageClicked() {
        mStageInteractor.goToPreviousStage();

        if (mView != null) {
            mView.moveToStage3View();
            mView.finishActivity();
        }
    }
}
