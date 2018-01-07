package org.vichtisproductions.focusting.presenter;

import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.bus.events.StageSelectEvent;
import org.vichtisproductions.focusting.interactor.IStage6ToastInteractor;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.interactor.StageInteractor;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.model.Stage6Toast;
import org.vichtisproductions.focusting.view.IStage6ToastSetView;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Renier on 2016/05/14.
 */
public class Stage6ToastSetViewPresenter implements IStage6ToastSetViewPresenter {

    private CompositeSubscription mSubscriptions;

    private IStage6ToastSetView mView;
    private IStage6ToastInteractor mStage6ToastInteractor;
    private IStageInteractor mStageInteractor;
    private RxBus mBus;

    private Stage mCurrentStage;

    public Stage6ToastSetViewPresenter(IStage6ToastInteractor toastInteractor, RxBus bus, IStageInteractor stageInteractor) {
        this.mStage6ToastInteractor = toastInteractor;
        this.mStageInteractor = stageInteractor;
        this.mBus = bus;
    }

    @Override
    public void setView(IStage6ToastSetView view) {
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

        mStage6ToastInteractor.getStage6Toast(stage.getStage())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(target -> {
                    if (mView != null) {
                        switch (target) {
                            case 0:
                                mView.setNoToastSelected();
                                break;

                            case 1:
                                mView.setInformationSelected();
                                break;

                            case 2:
                                mView.setThumbsUpSelected();
                                break;
                        }
                    }

                }, error -> {
                    //TODO - handle error
                });
    }

    @Override
    public void setStage6ToastTapped(int toast) {
        // mTargetInteractor.setTargetForStage(mCurrentStage.getStage(), target);
        mStage6ToastInteractor.setStage6Toast(6, toast);
    }

    public void setRadioButtonRight(int stage) {
        // First go get the current Stage from StageInteractor
        Stage currentStage = mStageInteractor.getCurrentStageSynchronous();
        // Then set the right toast for this stage
        if (currentStage.getStage() == 6) {
            // Timber.d("It's stage 6, let's define the toast type now.");
            setStage(currentStage);
        }
        // That should be it.
    }

    /*
    public Stage6Toast getTargetForStage6() {
        // Stage6Toast toast = mStage6ToastInteractor.getStage6ToastSynchronous(6);
        return;
    }
    */

}
