package org.coderswithoutborders.deglancer.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Renier on 2016/04/12.
 */
public class ScreenAction extends RealmObject{

    @PrimaryKey
    private String mId;
    private String mEventType;
    private long mEventDateTime;
    private int mStage;
    private int mDay;
    private int mHour;
    private long mDuration;

    public ScreenAction() {

    }

    public ScreenAction(String mId, String mEventType, long mEventDateTime, int mStage, int mDay, int mHour, long mDuration) {
        this.mId = mId;
        this.mEventType = mEventType;
        this.mEventDateTime = mEventDateTime;
        this.mStage = mStage;
        this.mDay = mDay;
        this.mHour = mHour;
        this.mDuration = mDuration;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getEventType() {
        return mEventType;
    }

    public void setEventType(String mEventType) {
        this.mEventType = mEventType;
    }

    public long getEventDateTime() {
        return mEventDateTime;
    }

    public void setEventDateTime(long mEventDateTime) {
        this.mEventDateTime = mEventDateTime;
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

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }
}

