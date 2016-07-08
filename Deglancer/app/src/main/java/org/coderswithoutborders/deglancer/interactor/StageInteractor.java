package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.bus.events.DebugStageEvent;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.stagehandlers.IStageHandler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage1Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage2Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage3Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage4Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage5Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage6Handler;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Renier on 2016/04/12.
 */
public class StageInteractor implements IStageInteractor {
    private Context mContext;
    private RxBus mBus;
    private IInitialStartupInteractor mInitialStartupInteractor;
    private Stage1Handler mStage1Handler;
    private Stage2Handler mStage2Handler;
    private Stage3Handler mStage3Handler;
    private Stage4Handler mStage4Handler;
    private Stage5Handler mStage5Handler;
    private Stage6Handler mStage6Handler;

    public StageInteractor(
            Context context,
            RxBus bus,
            IInitialStartupInteractor initialStartupInteractor,
            Stage1Handler stage1Handler,
            Stage2Handler stage2Handler,
            Stage3Handler stage3Handler,
            Stage4Handler stage4Handler,
            Stage5Handler stage5Handler,
            Stage6Handler stage6Handler) {
        mContext = context;
        mBus = bus;
        mInitialStartupInteractor = initialStartupInteractor;
        mStage1Handler = stage1Handler;
        mStage2Handler = stage2Handler;
        mStage3Handler = stage3Handler;
        mStage4Handler = stage4Handler;
        mStage5Handler = stage5Handler;
        mStage6Handler = stage6Handler;
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
        DateTime initialStartTime = new DateTime(mInitialStartupInteractor.getInitialStartTime());
        int days = Days.daysBetween(initialStartTime, new DateTime()).getDays();

        int stageNr;
        int day;
        int hour = Hours.hoursBetween(initialStartTime, new DateTime()).getHours() - (days * 24) + 1;

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
        } else if (days < 56) {
            //Stage5
            stageNr = 5;
            day = days - 28 + 1;
        } else {
            //Stage6
            stageNr = 6;
            day = days - 56 + 1;
        }

        return new Stage(
                stageNr,
                day,
                hour
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
            case 6:
                return mStage6Handler;

                // We have come to the end of the research
                // Go and find out what type of toast user has asked from here on
                // Assume 1 = Just the information.

                // BEGIN trying to figure out the right handler
                /*
                int handler=1;
                mStage6ToastInteractor.getStage6Toast(stage.getStage())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(target -> {
                                switch (target) {
                                    case 0:
                                        // User has asked No toast
                                        handler=1;
                                        break;
                                    case 1:
                                        // User has asked Information only
                                        handler=2;
                                        break;
                                    case 2:
                                        // User has asked Information and Thumbs up
                                        handler=3;
                                }
                        }, error -> {
                            //TODO - handle error
                        });
                // END trying to figure out the right handler

                int handler = mStage6ToastInteractor.getStage6ToastSynchronous(6);
                if (handler ==1) {
                    return mStage1Handler;
                } else if (handler==2) {
                    return mStage2Handler;
                } else if (handler==3) {
                    return mStage3Handler;
                }
                */
        }

        return null;
    }

    @Override
    public void goToNextStage() {
        Observable.create(subscriber -> {
            Stage currentStage = getCurrentStageSynchronous();
            Timber.d("Stage now set at " + Integer.toString(currentStage.getStage()));
            if (currentStage.getStage() < 5) {
                int days = currentStage.getStage() * 7;
                DateTime newStartTime = new DateTime().minusDays(days);

                mInitialStartupInteractor.overrideInitialStartTime(newStartTime.getMillis());
            } else if (currentStage.getStage() == 5) {
                    // TODO - Verify that this logic works
                    int days = 28 + (currentStage.getStage() * 7);
                    DateTime newStartTime = new DateTime().minusDays(days);

                    mInitialStartupInteractor.overrideInitialStartTime(newStartTime.getMillis());
                } else {
                //We can't go any further
            }

            subscriber.onNext(null);
            subscriber.onCompleted();
        })
                .subscribe(result -> {
                    mBus.post(new DebugStageEvent());
                }, error -> {
                    mBus.post(new DebugStageEvent());
                });
    }

    @Override
    public void goToPreviousStage() {
        Observable.create(subscriber -> {
            Stage currentStage = getCurrentStageSynchronous();
            // TODO - Check this too
            if (currentStage.getStage() == 6) {
                int days = ((currentStage.getStage() - 2) * 7) + 28;
                DateTime newStartTime = new DateTime().minusDays(days);

                mInitialStartupInteractor.overrideInitialStartTime(newStartTime.getMillis());
            } else if (currentStage.getStage() > 1) {
                int days = (currentStage.getStage() - 2) * 7;
                DateTime newStartTime = new DateTime().minusDays(days);

                mInitialStartupInteractor.overrideInitialStartTime(newStartTime.getMillis());
            } else {
                //We can't go back any further
            }

            subscriber.onNext(null);
            subscriber.onCompleted();
        })
                .subscribe(result -> {
                    mBus.post(new DebugStageEvent());
                }, error -> {
                    mBus.post(new DebugStageEvent());
                });
    }

    @Override
    public void setStageAsCurrentStage(int stage, int day, int hour) {

    }
}
