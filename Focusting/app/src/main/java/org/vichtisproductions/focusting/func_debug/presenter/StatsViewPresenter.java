package org.vichtisproductions.focusting.func_debug.presenter;

import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.bus.events.StageSelectEvent;
import org.vichtisproductions.focusting.func_debug.view.StageSelectView;
import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.utils.TimeUtils;
import org.vichtisproductions.focusting.func_debug.view.IStatsView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Renier on 2016/05/06.
 */
public class StatsViewPresenter implements IStatsViewPresenter {

    private CompositeSubscription mSubscriptions;

    private IStatsView mView;
    private Stage mCurrentStage;

    private IDatabaseInteractor mDatabaseInteractor;
    private IStageInteractor mStageInteractor;
    private RxBus mBus;

    public StatsViewPresenter(IDatabaseInteractor databaseInteractor, IStageInteractor stageInteractor, RxBus bus) {
        this.mDatabaseInteractor = databaseInteractor;
        this.mStageInteractor = stageInteractor;
        this.mBus = bus;

        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void setStage(Stage stage) {
        mCurrentStage = stage;
    }


    @Override
    public void setView(IStatsView view) {
        mView = view;
    }

    @Override
    public void clearView() {
        mView = null;
    }

    @Override
    public void onAttached() {
        loadValues(true);

        if (mSubscriptions == null || mSubscriptions.isUnsubscribed()) {
            mSubscriptions = new CompositeSubscription();
        } else if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
            mSubscriptions = new CompositeSubscription();
        }

        mSubscriptions.add(mBus.toObserverable().subscribe((event) -> {
            if (event instanceof StageSelectEvent) {
                mCurrentStage = ((StageSelectEvent) event).getStage();
            }

            loadValues(false);
        }));
    }

    @Override
    public void onDetached() {
        if (!mSubscriptions.isUnsubscribed())
            mSubscriptions.unsubscribe();
    }

    @Override
    public void refresh() {
        loadValues(true);
    }

    private void loadValues(boolean refresh) {
        Observable<Stage> theObservable;

        if (mCurrentStage == null || refresh) {
            theObservable = mStageInteractor.getCurrentStage();
        } else {
            theObservable = Observable.just(mCurrentStage);
        }

        theObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(stage -> {
                    mCurrentStage = stage;

                    String[] toReturn = new String[0];
                    try {
                        toReturn = new String[10];

                        toReturn[0] = String.valueOf(mDatabaseInteractor.getUnlockCountForStageDayFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay()));
                        toReturn[1] = String.valueOf(mDatabaseInteractor.getUnlockCountForStageFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()));

                        toReturn[2] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSOTForStageDayFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay()), true, true, true, true);
                        toReturn[3] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSOTForStageFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()), true, true, true, true);
                        toReturn[4] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSFTForStageDayFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay()), true, false, true, false);
                        toReturn[5] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSFTForStageFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()), true, false, true, false);

                        toReturn[6] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSOTForStageDayFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay()), true, false, true, false);
                        toReturn[7] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSOTForStageFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()), true, false, true, false);
                        toReturn[8] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSFTForStageDayFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay()), true, false, true, false);
                        toReturn[9] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSFTForStageFromAverages(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()), true, false, true, false);                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return Observable.just(toReturn);
                })
                .subscribe(values -> {
                    if (mView != null) {

                        mView.setValues(
                                values[0], values[1],
                                values[2], values[3],
                                values[4], values[5],
                                values[6], values[7],
                                values[8], values[9]
                        );
                    }
                }, error -> {
                    // handle error
                });
    }


    @Override
    public void clearDayClicked() {
        mDatabaseInteractor.clearEntriesForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay());
        mDatabaseInteractor.clearAveragesForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay());
        loadValues(false);
    }

    @Override
    public void clearHourClicked() {
        mDatabaseInteractor.clearEntriesForStageHour(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour());
        mDatabaseInteractor.clearAveragesForStageHour(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour());
        loadValues(false);
    }

    @Override
    public void clearStageClicked() {
        mDatabaseInteractor.clearEntriesForStage(mCurrentStage.getStage());
        mDatabaseInteractor.clearAveragesForStage(mCurrentStage.getStage());
        loadValues(false);

    }
}
