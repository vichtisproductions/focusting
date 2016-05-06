package org.coderswithoutborders.deglancer.func_debug.stage5;

import org.coderswithoutborders.deglancer.interactor.IStageInteractor;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage5Presenter implements IDebugStage5Presenter {
    private IDebugStage5View mView;
    private IStageInteractor mStageInteractor;


    public DebugStage5Presenter(IStageInteractor stageInteractor) {
        this.mStageInteractor = stageInteractor;
    }

    @Override
    public void setView(IDebugStage5View view) {
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
}
