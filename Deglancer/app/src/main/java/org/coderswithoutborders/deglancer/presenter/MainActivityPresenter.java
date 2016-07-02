package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.bus.events.DebugStageEvent;
import org.coderswithoutborders.deglancer.func_debug.view.ITargetSetView;
import org.coderswithoutborders.deglancer.func_debug.view.TargetSetView;
import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.interactor.IInitialStartupInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.model.Target;
import org.coderswithoutborders.deglancer.view.IMainActivityView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by chris.teli on 3/21/2016.
 */
public class MainActivityPresenter implements IMainActivityPresenter {

    private CompositeSubscription mSubscriptions;

    private IInitialStartupInteractor mInitialStartupInteractor;
    private IStageInteractor mStageInteractor;
    private IDatabaseInteractor mDatabaseInteractor;
    private RxBus mBus;

    private IMainActivityView mView;

    public MainActivityPresenter(IInitialStartupInteractor initialStartupInteractor, IStageInteractor stageInteractor, IDatabaseInteractor databaseInteractor, RxBus bus) {
        mInitialStartupInteractor = initialStartupInteractor;
        mStageInteractor = stageInteractor;
        mDatabaseInteractor = databaseInteractor;
        mBus = bus;

        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void setView(IMainActivityView view) {
        mView = view;

        if (mSubscriptions == null || mSubscriptions.isUnsubscribed()) {
            mSubscriptions = new CompositeSubscription();
        } else if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
            mSubscriptions = new CompositeSubscription();
        }

        mSubscriptions.add(mBus.toObserverable().subscribe((event) -> {

        }));
    }

    @Override
    public void clearView() {
        mView = null;

        if (!mSubscriptions.isUnsubscribed())
            mSubscriptions.unsubscribe();
    }

    @Override
    public void init() {
        mInitialStartupInteractor.captureInitialDataIfNotCaptured();

        mStageInteractor.getCurrentStage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stage -> {
                    if (mView != null) {
                        mView.setStageText(stage.getStage() + "-" + stage.getDay() + "-" + stage.getHour());
                        mView.setIntroText(stage.getStage(), stage.getDay());
                    }
                }, error -> {
                    //TODO - Handle error
                    String here = "";
                });
    }

    @Override
    public boolean isPreTestRun() {
        if (mDatabaseInteractor.isPreTestRun()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int whatStage() {

        Stage stage = mStageInteractor.getCurrentStageSynchronous();

        switch (stage.getStage()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
        }

        return 0;

    }

    @Override
    public int whatTarget() {
        Target myTarget = mDatabaseInteractor.getTargetForStage(4);
        if (myTarget == null) {
            return -1;
        } else {
            return myTarget.getTarget();
        }
    }

    @Override
    public void debugClicked() {
        mStageInteractor.getCurrentStage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stage -> {
                    if (mView != null) {
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

