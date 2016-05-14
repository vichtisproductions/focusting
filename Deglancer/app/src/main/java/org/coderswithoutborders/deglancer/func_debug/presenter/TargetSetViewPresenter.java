package org.coderswithoutborders.deglancer.func_debug.presenter;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.bus.events.StageSelectEvent;
import org.coderswithoutborders.deglancer.func_debug.view.ITargetSetView;
import org.coderswithoutborders.deglancer.interactor.ITargetInteractor;
import org.coderswithoutborders.deglancer.model.Stage;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Renier on 2016/05/14.
 */
public class TargetSetViewPresenter implements ITargetSetViewPresenter {

    private CompositeSubscription mSubscriptions;

    private ITargetSetView mView;
    private ITargetInteractor mTargetInteractor;
    private RxBus mBus;

    private Stage mCurrentStage;

    public TargetSetViewPresenter(ITargetInteractor targetInteractor, RxBus bus) {
        this.mTargetInteractor = targetInteractor;
        this.mBus = bus;
    }

    @Override
    public void setView(ITargetSetView view) {
        mView = view;
    }

    @Override
    public void clearView() {
        mView = null;
    }

    @Override
    public void onAttached() {
        if (mSubscriptions == null || mSubscriptions.isUnsubscribed()) {
            mSubscriptions = new CompositeSubscription();
        } else if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
            mSubscriptions = new CompositeSubscription();
        }

        mSubscriptions.add(mBus.toObserverable().subscribe((event) -> {
            if (event instanceof StageSelectEvent) {
                setStage(((StageSelectEvent) event).getStage());
            }
        }));
    }

    @Override
    public void onDetached() {
        if (!mSubscriptions.isUnsubscribed())
            mSubscriptions.unsubscribe();
    }

    @Override
    public void setStage(Stage stage) {
        mCurrentStage = stage;

        mTargetInteractor.getTargetForStage(stage.getStage())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(target -> {
                    if (mView != null) {
                        switch (target) {
                            case 5:
                                mView.set5Selected();
                                break;

                            case 10:
                                mView.set10Selected();
                                break;

                            case 15:
                                mView.set15Selected();
                                break;
                        }
                    }

                }, error -> {
                    //TODO - handle error
                });
    }

    @Override
    public void setTargetTapped(int target) {
        mTargetInteractor.setTargetForStage(mCurrentStage.getStage(), target);
    }
}
