package org.coderswithoutborders.deglancer.pretest;

import org.coderswithoutborders.deglancer.func_debug.stage1.IDebugStage1View;
import org.coderswithoutborders.deglancer.interactor.IPreTestInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.model.Stage;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lauripal on 29.6.2016.
 */
public class PreTestPresenter implements IPreTestPresenter {

    private IPreTestView mView;
    private IPreTestInteractor mPreTestInteractor;

    public PreTestPresenter(IPreTestInteractor preTestInteractor) {
        this.mPreTestInteractor = preTestInteractor;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }

    // @Override
    public void submitPreTestResults(String answer1, String answer2, String answer3, String answer4, String answer5, String answer6, String answer7, String answer8, String answer9, String answer10) {
        mPreTestInteractor.uploadPreTestResults(answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9, answer10);

        if (mView != null) {
            mView.moveToMainView();
            mView.finishActivity();
        }
    }
}
