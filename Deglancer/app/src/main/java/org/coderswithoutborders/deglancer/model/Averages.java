package org.coderswithoutborders.deglancer.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Renier on 2016/04/24.
 */
public class Averages extends RealmObject{
    @PrimaryKey
    private String mId;
    private int mStage;
    private int mDay;
    private int mHour;
    private long mUnlockCount;
    private double mSFT;
    private double mSOT;

    public Averages() {

    }

    public Averages(String mId, int mStage, int mDay, int mHour, long mUnlockCount, double mSFT, double mSOT) {
        this.mId = mId;
        this.mStage = mStage;
        this.mDay = mDay;
        this.mHour = mHour;
        this.mUnlockCount = mUnlockCount;
        this.mSFT = mSFT;
        this.mSOT = mSOT;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public int getStage() {
        return mStage;
    }

    public void setStage(int mStage) {
        this.mStage = mStage;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int mDay) {
        this.mDay = mDay;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int mHour) {
        this.mHour = mHour;
    }

    public long getUnlockCount() {
        return mUnlockCount;
    }

    public void setUnlockCount(long mUnlockCount) {
        this.mUnlockCount = mUnlockCount;
    }

    public double getSFT() {
        return mSFT;
    }

    public void setSFT(double mSFT) {
        this.mSFT = mSFT;
    }

    public double getSOT() {
        return mSOT;
    }

    public void setSOT(double mSOT) {
        this.mSOT = mSOT;
    }
}
