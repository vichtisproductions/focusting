package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.utils.TimeUtils;
import org.coderswithoutborders.deglancer.view.IStatsView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Renier on 2016/05/06.
 */
public class StatsViewPresenter implements IStatsViewPresenter {
    private IStatsView mView;
    private Stage mCurrentStage;

    private IDatabaseInteractor mDatabaseInteractor;
    private IStageInteractor mStageInteractor;

    private boolean mEnableStageChange = true;
    private boolean mEnableDayChange = true;
    private boolean mEnableHourChange = true;

    public StatsViewPresenter(IDatabaseInteractor databaseInteractor, IStageInteractor stageInteractor) {
        this.mDatabaseInteractor = databaseInteractor;
        this.mStageInteractor = stageInteractor;
    }

    @Override
    public void setStage(Stage stage) {
        mCurrentStage = stage;
    }

    @Override
    public void onStageChange(int newVal) {
        if (mCurrentStage != null)
            mCurrentStage.setStage(newVal);

        if (mView != null) {
            loadValues(false);
        }
    }

    @Override
    public void onDayChange(int day) {
        if (mCurrentStage != null)
            mCurrentStage.setDay(day);

        if (mView != null) {
            loadValues(false);
        }
    }

    @Override
    public void onHourChange(int hour) {
        if (mCurrentStage != null)
            mCurrentStage.setHour(hour);

        if (mView != null) {
            loadValues(false);
        }
    }


    @Override
    public void setView(IStatsView view) {
        mView = view;

        enableDisableStagePickerControls();
    }

    @Override
    public void clearView() {
        mView = null;
    }

    @Override
    public void onAttached() {
        loadValues(true);
    }

    @Override
    public void onDetached() {

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

                        toReturn[0] = String.valueOf(mDatabaseInteractor.getUnlockCountForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay()));
                        toReturn[1] = String.valueOf(mDatabaseInteractor.getUnlockCountForStage(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()));

                        toReturn[2] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSOTForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay()), true, false, true);
                        toReturn[3] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSOTForStage(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()), true, false, true);
                        toReturn[4] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSFTForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay()), true, false, true);
                        toReturn[5] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSFTForStage(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()), true, false, true);

                        toReturn[6] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSOTForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay()), true, false, true);
                        toReturn[7] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSOTForStage(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()), true, false, true);
                        toReturn[8] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSFTForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay()), true, false, true);
                        toReturn[9] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSFTForStage(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour()), true, false, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return Observable.just(toReturn);
                })
                .subscribe(values -> {
                    if (mView != null) {
                        mView.setStageVal(mCurrentStage.getStage());
                        mView.setStageDay(mCurrentStage.getDay());
                        mView.setStageHour(mCurrentStage.getHour());

                        mView.setValues(
                                values[0], values[1],
                                values[2], values[3],
                                values[4], values[5],
                                values[6], values[7],
                                values[8], values[9]
                        );
                    }
                }, error -> {
                    //TODO - handle error
                });
    }

    @Override
    public void setStagePickerState(boolean enableStageChange, boolean enableDayChange, boolean enableHourChange) {
        mEnableStageChange = enableStageChange;
        mEnableDayChange = enableDayChange;
        mEnableHourChange = enableHourChange;

        enableDisableStagePickerControls();
    }

    private void enableDisableStagePickerControls() {
        if (mView != null) {
            if (mEnableStageChange) {
                mView.enableStagePicker();
            } else {
                mView.disableStagePicker();
            }

            if (mEnableDayChange) {
                mView.enableDayPicker();
            } else {
                mView.disableDayPicker();
            }

            if (mEnableHourChange) {
                mView.enableHourPicker();
            } else {
                mView.disableHourPicker();
            }
        }
    }

    @Override
    public void clearDayClicked() {
        mDatabaseInteractor.clearEntriesForStageDay(mCurrentStage.getStage(), mCurrentStage.getDay());
        loadValues(false);
    }

    @Override
    public void clearHourClicked() {
        mDatabaseInteractor.clearEntriesForStageHour(mCurrentStage.getStage(), mCurrentStage.getDay(), mCurrentStage.getHour());
        loadValues(false);
    }
}
