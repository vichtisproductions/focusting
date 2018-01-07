package org.vichtisproductions.focusting.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Gravity;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.bus.events.DebugStageEvent;
import org.vichtisproductions.focusting.func_debug.view.ITargetSetView;
import org.vichtisproductions.focusting.func_debug.view.TargetSetView;
import org.vichtisproductions.focusting.interactor.DatabaseInteractor;
import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.interactor.IInitialStartupInteractor;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.model.Target;
import org.vichtisproductions.focusting.utils.ToastUtils;
import org.vichtisproductions.focusting.view.IMainActivityView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;


/**
 * Created by chris.teli on 3/21/2016.
 */
public class MainActivityPresenter implements IMainActivityPresenter {

    private CompositeSubscription mSubscriptions;

    private IInitialStartupInteractor mInitialStartupInteractor;
    private IStageInteractor mStageInteractor;
    private IDatabaseInteractor mDatabaseInteractor;
    private RxBus mBus;

    private IMainActivityView mView;

    private FirebaseAnalytics mFirebaseAnalytics;


    public MainActivityPresenter(IInitialStartupInteractor initialStartupInteractor, IStageInteractor stageInteractor, IDatabaseInteractor databaseInteractor, RxBus bus) {
        mInitialStartupInteractor = initialStartupInteractor;
        mStageInteractor = stageInteractor;
        mDatabaseInteractor = databaseInteractor;
        mBus = bus;

        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void setView(IMainActivityView view) {
        mView = view;

        if (mSubscriptions == null || mSubscriptions.isUnsubscribed()) {
            mSubscriptions = new CompositeSubscription();
        } else if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
            mSubscriptions = new CompositeSubscription();
        }

        mSubscriptions.add(mBus.toObserverable().subscribe((event) -> {

        }));
    }

    @Override
    public void clearView() {
        mView = null;

        if (!mSubscriptions.isUnsubscribed())
            mSubscriptions.unsubscribe();
    }

    @Override
    public void init() {
        mInitialStartupInteractor.captureInitialDataIfNotCaptured();

        mStageInteractor.getCurrentStage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stage -> {
                    if (mView != null) {
                        // mView.setStageText(stage.getStage() + "-" + stage.getDay() + "-" + stage.getHour());
                        mView.setIntroText(stage.getStage(), stage.getDay());
                    }
                }, error -> {
                    //TODO - Handle error
                    String here = "";
                });
    }

    @Override
    public boolean isPreTestRun() {
        if (mDatabaseInteractor.isPreTestRun()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int whatStage() {

        Stage stage = mStageInteractor.getCurrentStageSynchronous();

        switch (stage.getStage()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
        }

        return 0;

    }

    @Override
    public int whatTarget() {
        Target myTarget = mDatabaseInteractor.getTargetForStage(4);
        if (myTarget == null) {
            return -1;
        } else {
            return myTarget.getTarget();
        }
    }

    @Override
    public void debugClicked() {
        mStageInteractor.getCurrentStage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stage -> {
                    if (mView != null) {
                        switch (stage.getStage()) {
                            case 1:
                                mView.showStage1View(stage);
                                break;

                            case 2:
                                mView.showStage2View(stage);
                                break;

                            case 3:
                                mView.showStage3View(stage);
                                break;

                            case 4:
                                mView.showStage4View(stage);
                                break;

                            case 5:
                                mView.showStage5View(stage);
                                break;

                            case 6:
                                mView.showStage6View(stage);
                                break;
                        }
                    }
                }, error -> {
                    //TODO - Handle error
                    String here = "";
                });
    }

    @Override
    public void snoozeClicked(Context context) {
        ToastUtils.setSnooze(context);
        Toast toast = Toast.makeText(context, context.getString(R.string.tvSnoozedText), 3);
        toast.setGravity(Gravity.BOTTOM, 0, 30);
        toast.show();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle fbAnalyticsBundle = new Bundle();
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "snooze_notification");
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME, "Snooze notification");
        fbAnalyticsBundle.putLong(FirebaseAnalytics.Param.VALUE, 2L);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY, fbAnalyticsBundle);

    }


    // Adjust start time if app has been updated from pre-V16
    public void adjustStartTime(Context context) {


        final String SP_NAME = "InitialStartupSP";
        final String SP_KEY_INITIAL_SETUP_DONE = "InitialSetupDone";
        final String SP_KEY_INITIAL_START_TIME = "InitialStartTime";
        final String SP_KEY_INITIAL_TIMEZONE = "InitialTimezone";
        final String SP_KEY_INITIAL_START_TIME_ZERO_HOUR = "InitialStartTimeZeroHour";

        SharedPreferences mPrefs;
        mPrefs = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        Boolean initialSetupDone = mPrefs.getBoolean(SP_KEY_INITIAL_SETUP_DONE, false);
        if (!initialSetupDone) {
            Timber.d("Initial startup. Skipping.");
            return;
        } else {
            Timber.d("Adjusting...");
            Long initialZeroHour = mPrefs.getLong(SP_KEY_INITIAL_START_TIME_ZERO_HOUR, -1);
            if (Long.valueOf(initialZeroHour) == Long.valueOf(-1)) {
                Long initialStartTime = mPrefs.getLong(SP_KEY_INITIAL_START_TIME, 0);
                String userTimezone = mPrefs.getString(SP_KEY_INITIAL_TIMEZONE, "-1");
                if (userTimezone == "-1") {
                    Timber.d("Gotta specify timezone first");
                    userTimezone = Time.getCurrentTimezone();
                }
                Timber.d("Timezone: " + userTimezone);
                Timber.d("Initial start time: " + Long.toString(initialStartTime));
                DateTime startTimeBeginning = new DateTime(Long.valueOf(initialStartTime), DateTimeZone.UTC);
                DateTime todayStart = startTimeBeginning.withTimeAtStartOfDay();
                Timber.d("Beginning of the day: " + Long.toString(todayStart.getMillis()));
                Timber.d("Now overriding");
                initialStartTime = todayStart.getMillis();
                SharedPreferences.Editor editor;
                editor = mPrefs.edit();
                editor.putLong(SP_KEY_INITIAL_START_TIME, initialStartTime);
                editor.putLong(SP_KEY_INITIAL_START_TIME_ZERO_HOUR, initialStartTime);
                editor.putString(SP_KEY_INITIAL_TIMEZONE, userTimezone);
                editor.apply();
                Timber.d("Initial start time now: " + Long.toString(initialStartTime));
            } else {
                Timber.d("Start time adjusted, no need.");
            }

        }
    }


}

