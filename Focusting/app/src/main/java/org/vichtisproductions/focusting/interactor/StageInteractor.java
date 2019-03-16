package org.vichtisproductions.focusting.interactor;

import android.content.Context;
import android.content.res.Resources;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.bus.events.DebugStageEvent;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.stagehandlers.IStageHandler;
import org.vichtisproductions.focusting.stagehandlers.Stage1Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage2Group2Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage2Group3Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage2Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage3Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage4Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage5Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage6Handler;
import org.vichtisproductions.focusting.utils.CalendarUtils;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

/**
 * Created by Renier on 2016/04/12.
 */
public class StageInteractor implements IStageInteractor {
    private Context mContext;
    private RxBus mBus;
    private IInitialStartupInteractor mInitialStartupInteractor;
    private Stage1Handler mStage1Handler;
    private final Stage2Group2Handler stage2Group2Handler;
    private final Stage2Group3Handler stage2Group3Handler;
    private Stage2Handler mStage2Handler;
    private Stage3Handler mStage3Handler;
    private Stage4Handler mStage4Handler;
    private Stage5Handler mStage5Handler;
    private Stage6Handler mStage6Handler;
    private IStage6ToastInteractor mStage6ToastInteractor;

    public StageInteractor(
            Context context,
            RxBus bus,
            IInitialStartupInteractor initialStartupInteractor,
            Stage1Handler stage1Handler,
            Stage2Group2Handler stage2Group2Handler,
            Stage2Group3Handler stage2Group3Handler,
            Stage2Handler stage2Handler,
            Stage3Handler stage3Handler,
            Stage4Handler stage4Handler,
            Stage5Handler stage5Handler,
            Stage6Handler stage6Handler,
            IStage6ToastInteractor stage6ToastInteractor) {
        mContext = context;
        mBus = bus;
        mInitialStartupInteractor = initialStartupInteractor;
        mStage1Handler = stage1Handler;
        this.stage2Group2Handler = stage2Group2Handler;
        this.stage2Group3Handler = stage2Group3Handler;
        mStage2Handler = stage2Handler;
        mStage3Handler = stage3Handler;
        mStage4Handler = stage4Handler;
        mStage5Handler = stage5Handler;
        mStage6Handler = stage6Handler;
        mStage6ToastInteractor = stage6ToastInteractor;
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

        // TODO - REVIEW IF THIS IS JUST FINE
        // First, find out what group the user belongs
        int FocustingGroupNumber = new Integer(mInitialStartupInteractor.getGroupNumber());

        // Then, how long are the stages for that group
        // Figure out the length of the stage depending on the FocustingGroupNumber
        // TODO - SIMPLIFY THIS BECAUSE STAGES ARE OF EQUAL LENGTH
        int[] lengthOfStage = new int[3];
        Resources resources = mContext.getResources();
        if (FocustingGroupNumber == 1) {
            lengthOfStage = resources.getIntArray(R.array.FocustingStagesGroup1);
        } else if (FocustingGroupNumber == 2) {
            lengthOfStage = resources.getIntArray(R.array.FocustingStagesGroup2);
        } else if (FocustingGroupNumber == 3) {
            lengthOfStage = resources.getIntArray(R.array.FocustingStagesGroup3);
        }
        int Stage1len = lengthOfStage[0]; // TODO - 8
        int Stage2len = lengthOfStage[1]; // TODO - 14
        int Stage3len = lengthOfStage[2]; // TODO - 7


        // Figure out what stage the person should be on that day
        // TODO - SIMPLIFY & REVIEW
        if (days < Stage1len) {
            //Stage1
            stageNr = 1;
            day = days + 1;
        } else if (days < (Stage1len + Stage2len)) {
            //Stage2
            stageNr = 2;
            day = days - Stage1len + 1;
        } else if (days < (Stage1len + Stage2len + Stage3len)) {
            //Stage3
            stageNr = 3;
            day = days - Stage1len - Stage2len + 1;
        } else {
            //Stage4
            stageNr = 4;
            day = days - Stage1len - Stage2len - Stage3len + 1;
        }

        // Now return stage number etc.
        return new Stage(
                stageNr,
                day,
                hour
        );
    }

    @Override
    public IStageHandler getCurrentStageHandler() {

        Stage stage = getCurrentStageSynchronous();
        Timber.d("Stage handler " + stage.getStage());

        // First, find out what group the user belongs
        int FocustingGroupNumber = new Integer(mInitialStartupInteractor.getGroupNumber());
        Timber.d("Focusting group: " + String.valueOf(FocustingGroupNumber));
        // TODO - Figure out number of attendees - here calling CalendarUtils
        Timber.d("Now checking number of attendees");
        int attendeeCount = CalendarUtils.getAttendeeCount(mContext);
        Timber.d("It was: %d", attendeeCount);

        // Finally, figure out what stage interactor should be used
        // Array name: FocustingStagesGroup1, 2 and 3

        // TODO - Review and confirm that handler picker works fine
        switch (stage.getStage()) {
            case 1:
                return mStage1Handler;
            case 2:
                Timber.d("Stage 2: Figuring out what stimulus to use.");
                switch (FocustingGroupNumber) {
                    case 1:
                        // No messages for group 1
                        return mStage1Handler;
                    case 2:
                        return stage2Group2Handler;
                    case 3:
                        return stage2Group3Handler;
                }
            case 3:
                return mStage1Handler;
            case 4:
                // TODO: Here be the post-research handler logic
                return mStage1Handler;
        }

        return null;
    }

    @Override
    public void goToNextStage() {
        Observable.create(subscriber -> {
            Stage currentStage = getCurrentStageSynchronous();
            // Timber.d("Stage now set at " + Integer.toString(currentStage.getStage()));
            if (currentStage.getStage() < 6) {
                int days = currentStage.getStage() * 7;
                // Timber.d("Going forward " + Integer.toString(days) + " days");
                DateTime newStartTime = new DateTime().minusDays(days);

                mInitialStartupInteractor.overrideInitialStartTime(newStartTime.getMillis());
                /*
            } else if (currentStage.getStage() == 5) {
                    int days = 56;
                    Timber.d("Going forward " + Integer.toString(days) + " days");
                    DateTime newStartTime = new DateTime().minusDays(days);

                    mInitialStartupInteractor.overrideInitialStartTime(newStartTime.getMillis());
                    */
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
            // Timber.d("Current stage at " + Integer.toString(currentStage.getStage()));
            int days = 0;
            /*
            if (currentStage.getStage() == 6) {
                days = 28;
                Timber.d("Going back " + Integer.toString(days) + " days");
                DateTime newStartTime = new DateTime().minusDays(days);

                mInitialStartupInteractor.overrideInitialStartTime(newStartTime.getMillis());

            } else if (currentStage.getStage() == 5) {
                days = 21;
                Timber.d("Going back " + Integer.toString(days) + " days");
                DateTime newStartTime = new DateTime().minusDays(days);

                mInitialStartupInteractor.overrideInitialStartTime(newStartTime.getMillis());
            } else
            */
            if (currentStage.getStage() > 1) {
                days = (currentStage.getStage() - 2) * 7;
                // Timber.d("Going back " + Integer.toString(days) + " days");
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
