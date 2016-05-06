package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
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
    private Stage mStage;

    private IDatabaseInteractor mDatabaseInteractor;

    public StatsViewPresenter(IDatabaseInteractor databaseInteractor) {
        this.mDatabaseInteractor = databaseInteractor;
    }

    @Override
    public void setStage(Stage stage) {
        mStage = stage;
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
        loadValues();
    }

    @Override
    public void onDetached() {

    }

    @Override
    public void refresh() {
        loadValues();
    }

    private void loadValues() {
        if (mStage != null) {
            Observable.create(subscriber -> {
                String[] toReturn = new String[0];
                try {
                    toReturn = new String[10];

                    toReturn[0] = String.valueOf(mDatabaseInteractor.getUnlockCountForStageDay(mStage.getStage(), mStage.getDay()));
                    toReturn[1] = String.valueOf(mDatabaseInteractor.getUnlockCountForStage(mStage.getStage(), mStage.getDay(), mStage.getHour()));

                    toReturn[2] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSOTForStageDay(mStage.getStage(), mStage.getDay()), true, false, true);
                    toReturn[3] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSOTForStage(mStage.getStage(), mStage.getDay(), mStage.getHour()), true, false, true);
                    toReturn[4] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSFTForStageDay(mStage.getStage(), mStage.getDay()), true, false, true);
                    toReturn[5] = TimeUtils.getTimeStringFromMillis(mDatabaseInteractor.getTotalSFTForStage(mStage.getStage(), mStage.getDay(), mStage.getHour()), true, false, true);

                    toReturn[6] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSOTForStageDay(mStage.getStage(), mStage.getDay()), true, false, true);
                    toReturn[7] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSOTForStage(mStage.getStage(), mStage.getDay(), mStage.getHour()), true, false, true);
                    toReturn[8] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSFTForStageDay(mStage.getStage(), mStage.getDay()), true, false, true);
                    toReturn[9] = TimeUtils.getTimeStringFromMillis((long) mDatabaseInteractor.getAverageSFTForStage(mStage.getStage(), mStage.getDay(), mStage.getHour()), true, false, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                subscriber.onNext(toReturn);
                subscriber.onCompleted();
            })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (mView != null) {
                            String[] values = (String[]) result;

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
    }
}
