package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.Intent;

import org.coderswithoutborders.deglancer.model.Averages;
import org.coderswithoutborders.deglancer.model.ScreenAction;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Renier on 2016/04/27.
 */
public class DatabaseInteractor implements IDatabaseInteractor {

    private Context mContext;
    private Realm mRealm;

    public DatabaseInteractor(Context context, Realm realm) {
        this.mContext = context;
        this.mRealm = realm;
    }

    @Override
    public long getUnlockCountForStage(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                .count();
    }

    @Override
    public double getAverageSFTForStage(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                .average("mDuration");
    }

    @Override
    public double getAverageSOTForStage(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_OFF)
                .average("mDuration");
    }

    @Override
    public void commitAverages(Averages averages) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(averages);
        mRealm.commitTransaction();
    }

    @Override
    public ScreenAction getLastScreenAction() {
        RealmResults<ScreenAction> results = mRealm
                .where(ScreenAction.class)
                .findAllSorted("mEventDateTime", Sort.DESCENDING);

        if (results.isValid() && results.size() > 0 && results.first() != null) {
            return results.first();
        } else {
            return null;
        }
    }

    @Override
    public ScreenAction getLastScreenActionOfType(String action) {
        RealmResults<ScreenAction> results = mRealm
                .where(ScreenAction.class)
                .equalTo("mEventType", action)
                .findAllSorted("mEventDateTime", Sort.DESCENDING);

        if (results.isValid() && results.size() > 0 && results.first() != null) {
            return results.first();
        } else {
            return null;
        }
    }

    @Override
    public void commitScreenAction(ScreenAction action) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(action);
        mRealm.commitTransaction();
    }

    @Override
    public long getUnlockCountForStageDay(int stage, int day) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                .count();
    }

    @Override
    public long getTotalSOTForStageDay(int stage, int day) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mEventType", Intent.ACTION_SCREEN_OFF)
                .sum("mDuration")
                .longValue();
    }
}
