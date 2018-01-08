package org.vichtisproductions.focusting.func_debug.stage1;



import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.model.Stage;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage1Presenter implements IDebugStage1Presenter {

    private IDebugStage1View mView;
    private IStageInteractor mStageInteractor;
    private IDatabaseInteractor mDatabaseInteractor;

    private Stage mCurrentStage;

    private static final String TAG = "Focusting.Debug1Presenter";

    public DebugStage1Presenter(IStageInteractor stageInteractor, IDatabaseInteractor databaseInteractor) {
        this.mStageInteractor = stageInteractor;
        this.mDatabaseInteractor = databaseInteractor;
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
    public void advanceStageClicked() {
        mStageInteractor.goToNextStage();

        if (mView != null) {
            mView.moveToStage2View();
            mView.finishActivity();
        }
    }

    public void clearTestResults() {
        // Timber.d( "Clear Pre-tests and targets");
        mDatabaseInteractor.clearPreTestResults();
        mDatabaseInteractor.clearTarget();
    }
}
