package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.interactor.IInitialStartupInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.view.IMainActivityView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chris.teli on 3/21/2016.
 */
public class MainActivityPresenter implements IMainActivityPresenter {
    private IInitialStartupInteractor mInitialStartupInteractor;
    private IStageInteractor mStageInteractor;

    private IMainActivityView mView;

    public MainActivityPresenter(IInitialStartupInteractor initialStartupInteractor, IStageInteractor stageInteractor) {
        mInitialStartupInteractor = initialStartupInteractor;
        mStageInteractor = stageInteractor;
    }

    @Override
    public void setView(IMainActivityView view) {
        mView = view;
    }

    @Override
    public void clearView() {
        mView = null;
    }

    @Override
    public void init() {
        mInitialStartupInteractor.captureInitialDataIfNotCaptured();

        mStageInteractor.getCurrentStage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stage -> {
                    if (mView != null) {
                        mView.removeAllViewsFromMain();

                        switch (stage.getStage()) {
                            case 1:
                                mView.showStage1View(stage);
                                break;

                            case 2:
                                mView.showStage2View(stage);
                                break;

                            case 3:
                                mView.showStage3View(stage);
                                break;

                            case 4:
                                mView.showStage4View(stage);
                                break;

                            case 5:
                                mView.showStage5View(stage);
                                break;
                        }
                    }
                }, error -> {
                    //TODO - Handle error
                    String here = "";
                });
    }
}

