package org.vichtisproductions.focusting.func_debug.presenter;

import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.bus.events.StageSelectEvent;
import org.vichtisproductions.focusting.func_debug.view.ITargetSetView;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.interactor.ITargetInteractor;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.model.Target;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Renier on 2016/05/14.
 */
public class TargetSetViewPresenter implements ITargetSetViewPresenter {

    private CompositeSubscription mSubscriptions;

    private ITargetSetView mView;
    private ITargetInteractor mTargetInteractor;
    private IStageInteractor mStageInteractor;
    private RxBus mBus;

    private Stage mCurrentStage;
    private Target mCurrentTarget;

    public TargetSetViewPresenter(ITargetInteractor targetInteractor, RxBus bus, IStageInteractor stageInteractor) {
        this.mTargetInteractor = targetInteractor;
        this.mStageInteractor = stageInteractor;
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
                            default:
                                break;
                        }
                    }

                }, error -> {
                    //TODO - handle error
                });
    }

    @Override
    public void setTargetTapped(int target) {
        // mTargetInteractor.setTargetForStage(mCurrentStage.getStage(), target);
        mTargetInteractor.setTargetForStage(4, target);
    }

    @Override
    public void setRadioButtonRight(int stage) {
            int currentTarget = mTargetInteractor.getTargetForStageSynchronous(4);
            Stage currentStage = mStageInteractor.getCurrentStageSynchronous();
            // Then set the right toast for this stage
            if (currentStage.getStage() == 4) {
                // Timber.d("It's stage 4, let's define the target now.");
                setStage(currentStage);
            }
            // That should be it.
    }

}
