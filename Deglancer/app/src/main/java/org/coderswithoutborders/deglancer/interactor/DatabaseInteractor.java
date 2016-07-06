package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;
import android.content.Intent;

import org.coderswithoutborders.deglancer.model.Averages;
import org.coderswithoutborders.deglancer.model.PreTestResults;
import org.coderswithoutborders.deglancer.model.ScreenAction;
import org.coderswithoutborders.deglancer.model.Target;
import org.coderswithoutborders.deglancer.model.UserInfo;

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
    public long getUnlockCountForStageDay(int stage, int day) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                .count();
    }

    @Override
    public long getTotalSFTForStage(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                .sum("mDuration")
                .longValue();
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
    public double getAverageSFTForStage(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                .average("mDuration");
    }

    @Override
    public long getTotalSOTForStage(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_OFF)
                .sum("mDuration")
                .longValue();
    }

    @Override
    public long getUnlockCountForStageFromAverages(int stage, int day, int hour) {
        Averages avg = mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .findFirst();

        if (avg != null) {
            return avg.getUnlockCount();
        } else {
            return 0l;
        }
    }

    @Override
    public long getUnlockCountForStageDayFromAverages(int stage, int day) {
        return mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .sum("mUnlockCount")
                .longValue();
    }

    @Override
    public long getTotalSFTForStageFromAverages(int stage, int day, int hour) {
        Averages avg = mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .findFirst();

        if (avg != null) {
            return avg.getTotalSFT();
        } else {
            return 0l;
        }
    }

    @Override
    public long getTotalSFTForStageDayFromAverages(int stage, int day) {
        return mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .sum("mTotalSFT")
                .longValue();
    }

    @Override
    public double getAverageSFTForStageFromAverages(int stage, int day, int hour) {
        Averages avg = mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .findFirst();

        if (avg != null) {
            return avg.getSFT();
        } else {
            return 0l;
        }
    }

    @Override
    public double getAverageSFTForStageDayFromAverages(int stage, int day) {
        return mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .average("mSFT");
    }

    @Override
    public long getTotalSOTForStageFromAverages(int stage, int day, int hour) {
        Averages avg = mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .findFirst();

        if (avg != null) {
            return avg.getTotalSOT();
        } else {
            return 0l;
        }
    }

    @Override
    public long getTotalSOTForStageDayFromAverages(int stage, int day) {
        return mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .sum("mTotalSOT")
                .longValue();
    }

    @Override
    public double getAverageSOTForStageFromAverages(int stage, int day, int hour) {
        Averages avg = mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .findFirst();

        if (avg != null) {
            return avg.getSOT();
        } else {
            return 0l;
        }
    }

    @Override
    public double getAverageSOTForStageDayFromAverages(int stage, int day) {
        return mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .average("mSOT");
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
    public void clearEntriesForStage(int stage) {
        mRealm.beginTransaction();

        mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .findAll().clear();

        mRealm.commitTransaction();
    }

    @Override
    public void clearEntriesForStageDay(int stage, int day) {
        mRealm.beginTransaction();

        mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .findAll().clear();

        mRealm.commitTransaction();
    }

    @Override
    public void clearEntriesForStageHour(int stage, int day, int hour) {
        mRealm.beginTransaction();

        mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .findAll().clear();

        mRealm.commitTransaction();
    }

    @Override
    public void clearAveragesForStage(int stage) {
        mRealm.beginTransaction();

        mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .findAll().clear();

        mRealm.commitTransaction();
    }

    @Override
    public void clearAveragesForStageDay(int stage, int day) {
        mRealm.beginTransaction();

        mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .findAll().clear();

        mRealm.commitTransaction();
    }

    @Override
    public void clearAveragesForStageHour(int stage, int day, int hour) {
        mRealm.beginTransaction();

        mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .equalTo("mHour", hour)
                .findAll().clear();

        mRealm.commitTransaction();
    }

    @Override
    public void commitTarget(Target target) {
        mRealm.beginTransaction();

        mRealm.where(Target.class)
                .equalTo("mStage", target.getStage())
                .findAll().clear();

        mRealm.copyToRealm(target);

        mRealm.commitTransaction();
    }

    @Override
    public void clearTarget() {
        mRealm.beginTransaction();

        mRealm.where(Target.class)
                .findAll().clear();

        mRealm.commitTransaction();
    }

    @Override
    public void commitPreTestResults(PreTestResults preTestResults) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(preTestResults);
        mRealm.commitTransaction();
    }

    @Override
    public void clearPreTestResults() {
        mRealm.beginTransaction();
        mRealm.where(PreTestResults.class)
                .findAll().clear();
        mRealm.commitTransaction();
    }

    @Override
    public boolean isPreTestRun() {
        RealmResults<PreTestResults> results = mRealm
                .where(PreTestResults.class)
                .findAll();

        if (results.isValid() && results.size() > 0 && results.first() != null) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Target getTargetForStage(int stage) {
        try {
            RealmResults<Target> results = mRealm
                    .where(Target.class)
                    .equalTo("mStage", stage)
                    .findAll();

            if (results.isValid() && results.size() > 0 && results.first() != null) {
                return results.first();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getSumOfUnlockCountForStageDayUpToHourFromAverages(int stage, int day, int hour) {
        return mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .lessThanOrEqualTo("mHour", hour)
                .sum("mUnlockCount")
                .longValue();
    }

    @Override
    public long getSumOfSOTForStageDayUpToHourFromAverages(int stage, int day, int hour) {
        return mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .lessThanOrEqualTo("mHour", hour)
                .sum("mTotalSOT")
                .longValue();
    }

    @Override
    public double getAverageSFTForStageDayUpToHourFromAverages(int stage, int day, int hour) {
        return mRealm.where(Averages.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .lessThanOrEqualTo("mHour", hour)
                .average("mSFT");
    }

    @Override
    public long getSumOfUnlockCountForStageDayUpToHourFromActions(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .lessThanOrEqualTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                .count();
    }

    @Override
    public long getSumOfSOTForStageDayUpToHourFromActions(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .lessThanOrEqualTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_OFF)
                .sum("mDuration")
                .longValue();
    }

    @Override
    public double getAverageSFTForStageDayUpToHourFromActions(int stage, int day, int hour) {
        return mRealm.where(ScreenAction.class)
                .equalTo("mStage", stage)
                .equalTo("mDay", day)
                .lessThanOrEqualTo("mHour", hour)
                .equalTo("mEventType", Intent.ACTION_SCREEN_ON)
                .average("mDuration");
    }
}
