package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;

import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.stagehandlers.IStageHandler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage1Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage2Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage3Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage4Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage5Handler;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public class StageInteractor implements IStageInteractor {
    private Context mContext;
    private IInitialStartupInteractor mInitialStartupInteractor;
    private Stage1Handler mStage1Handler;
    private Stage2Handler mStage2Handler;
    private Stage3Handler mStage3Handler;
    private Stage4Handler mStage4Handler;
    private Stage5Handler mStage5Handler;

    public StageInteractor(
            Context context,
            IInitialStartupInteractor initialStartupInteractor,
            Stage1Handler stage1Handler,
            Stage2Handler stage2Handler,
            Stage3Handler stage3Handler,
            Stage4Handler stage4Handler,
            Stage5Handler stage5Handler) {
        mContext = context;
        mInitialStartupInteractor = initialStartupInteractor;
        mStage1Handler = stage1Handler;
        mStage2Handler = stage2Handler;
        mStage3Handler = stage3Handler;
        mStage4Handler = stage4Handler;
        mStage5Handler = stage5Handler;
    }

    @Override
    public Observable<Stage> getCurrentStage() {
        return Observable.defer(() -> Observable.create(subscriber -> {

            subscriber.onNext(getCurrentStageSynchronous());
            subscriber.onCompleted();
        }));
    }

    @Override
    public Stage getCurrentStageSynchronous() {
        Calendar c = Calendar.getInstance(Locale.getDefault());

        DateTime initialStartTime = new DateTime(mInitialStartupInteractor.getInitialStartTime());
        int days = Days.daysBetween(initialStartTime, new DateTime()).getDays();

        int stageNr;
        int day;

        if (days < 7) {
            //Stage1
            stageNr = 1;
            day = days + 1;
        } else if (days < 14) {
            //Stage2
            stageNr = 2;
            day = days - 7 + 1;
        } else if (days < 21) {
            //Stage3
            stageNr = 3;
            day = days - 14 + 1;
        } else if (days < 28) {
            //Stage4
            stageNr = 4;
            day = days - 21 + 1;
        } else {
            //Stage5
            stageNr = 5;
            day = days - 28 + 1;
        }

        return new Stage(
                stageNr,
                day,
                c.get(Calendar.HOUR_OF_DAY)
        );
    }

    @Override
    public IStageHandler getCurrentStageHandler() {
        Stage stage = getCurrentStageSynchronous();

        switch (stage.getStage()) {
            case 1:
                return mStage1Handler;
            case 2:
                return mStage2Handler;
            case 3:
                return mStage3Handler;
            case 4:
                return mStage4Handler;
            case 5:
                return mStage5Handler;
        }

        return null;
    }

    @Override
    public void advanceCurrentStage() {
        Observable.create(subscriber -> {




            subscriber.onNext(null);
            subscriber.onCompleted();
        })
                .subscribe(result -> {

                }, error -> {

                });
    }
}
