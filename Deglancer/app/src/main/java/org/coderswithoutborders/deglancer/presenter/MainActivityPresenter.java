package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.bus.events.DebugStageEvent;
import org.coderswithoutborders.deglancer.interactor.IInitialStartupInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
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
    private RxBus mBus;

    private IMainActivityView mView;

    public MainActivityPresenter(IInitialStartupInteractor initialStartupInteractor, IStageInteractor stageInteractor, RxBus bus) {
        mInitialStartupInteractor = initialStartupInteractor;
        mStageInteractor = stageInteractor;
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
            if (event instanceof DebugStageEvent) {
                init();
            }
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

