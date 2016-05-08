package org.coderswithoutborders.deglancer.func_debug.presenter;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.bus.events.AveragesOverrideEvent;
import org.coderswithoutborders.deglancer.bus.events.StageSelectEvent;
import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.model.Averages;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.utils.TimeUtils;
import org.coderswithoutborders.deglancer.func_debug.view.IAveragesSetView;

import java.util.UUID;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Renier on 2016/05/07.
 */
public class AveragesSetViewPresenter implements IAveragesSetViewPresenter {

    private CompositeSubscription mSubscriptions;

    private IAveragesSetView mView;
    private IDatabaseInteractor mDatabaseInteractor;
    private RxBus mBus;

    private int mStageVal = 0;
    private Stage mCurrentStage;

    private int mAvgUnlocks = 0;
    private long mAvgSOT = 0;
    private long mAvgSFT = 0;
    private long mTotalSOT = 0;
    private long mTotalSFT = 0;

    public AveragesSetViewPresenter(IDatabaseInteractor databaseInteractor, RxBus bus) {
        this.mDatabaseInteractor = databaseInteractor;
        this.mBus = bus;
    }

    @Override
    public void setView(IAveragesSetView view) {
        mView = view;
    }

    @Override
    public void setStage(Stage stage) {
        mCurrentStage = stage;
        mStageVal = stage.getStage();

        if (mView != null) {
            mView.updateTitleTextWith(mStageVal + "-" + mCurrentStage.getDay() + "-" + mCurrentStage.getHour());
        }
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
                mCurrentStage = ((StageSelectEvent) event).getStage();
               // mCurrentStage.setStage(mStageVal);

                if (mView != null) {
                    mView.updateTitleTextWith(mStageVal + "-" + mCurrentStage.getDay() + "-" + mCurrentStage.getHour());
                }
            }
        }));

        if (mView != null && mCurrentStage != null) {
            mView.updateTitleTextWith(mStageVal + "-" + mCurrentStage.getDay() + "-" + mCurrentStage.getHour());
        }
    }

    @Override
    public void onDetached() {
        if (!mSubscriptions.isUnsubscribed())
            mSubscriptions.unsubscribe();
    }

    @Override
    public void setForStageDayClicked() {
        if (mCurrentStage != null) {
            mDatabaseInteractor.clearAveragesForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay());

            for (int j = 0; j < 24; j++) {
                Averages avg = new Averages(
                        UUID.randomUUID().toString(),
                        mStageVal,
                        mCurrentStage.getDay(),
                        j,
                        mAvgUnlocks,
                        mAvgSFT,
                        mAvgSOT,
                        mTotalSFT,
                        mTotalSOT
                );
                mDatabaseInteractor.commitAverages(avg);
            }


            mBus.post(new AveragesOverrideEvent());
        }
    }

    @Override
    public void setForStageDayHourClicked() {
        mDatabaseInteractor.clearAveragesForStageHour(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour());
        Averages avg = new Averages(
                UUID.randomUUID().toString(),
                mStageVal,
                mCurrentStage.getDay(),
                mCurrentStage.getHour(),
                mAvgUnlocks,
                mAvgSFT,
                mAvgSOT,
                mTotalSFT,
                mTotalSOT
        );
        mDatabaseInteractor.commitAverages(avg);

        mBus.post(new AveragesOverrideEvent());
    }

    @Override
    public void setForStageClicked() {
        if (mCurrentStage != null) {
            mDatabaseInteractor.clearAveragesForStage(mCurrentStage.getStage());

            for (int i = 1; i < 8; i++) {
                for (int j = 0; j < 24; j++) {
                    Averages avg = new Averages(
                            UUID.randomUUID().toString(),
                            mStageVal,
                            i,
                            j,
                            mAvgUnlocks,
                            mAvgSFT,
                            mAvgSOT,
                            mTotalSFT,
                            mTotalSOT
                    );
                    mDatabaseInteractor.commitAverages(avg);
                }
            }

            mBus.post(new AveragesOverrideEvent());
        }
    }

    @Override
    public void avgSFTPicked(long millis) {
        mAvgSFT = millis;

        if (mView != null) {
            mView.setAvgSFTText(TimeUtils.getTimeStringFromMillis(millis, true, false, true));
        }
    }

    @Override
    public void avgSOTPicked(long millis) {
        mAvgSOT = millis;

        if (mView != null) {
            mView.setAvgSOTText(TimeUtils.getTimeStringFromMillis(millis, true, false, true));
        }
    }

    @Override
    public void totalSFTPicked(long millis) {
        mTotalSFT = millis;

        if (mView != null) {
            mView.setTotalSFTText(TimeUtils.getTimeStringFromMillis(millis, true, false, true));
        }
    }

    @Override
    public void totalSOTPicked(long millis) {
        mTotalSOT = millis;

        if (mView != null) {
            mView.setTotalSOTText(TimeUtils.getTimeStringFromMillis(millis, true, false, true));
        }
    }

    @Override
    public void avgUnlocksPicked(int unlocks) {
        mAvgUnlocks = unlocks;
    }
}
