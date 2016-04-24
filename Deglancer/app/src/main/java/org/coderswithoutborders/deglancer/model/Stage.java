package org.coderswithoutborders.deglancer.model;

/**
 * Created by Renier on 2016/04/24.
 */
public class Stage {
    private int mStage;
    private int mDay;
    private int mHour;

    public Stage(int mStage, int mDay, int mHour) {
        this.mStage = mStage;
        this.mDay = mDay;
        this.mHour = mHour;
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
}
