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

    public ScreenAction() {

    }

    public ScreenAction(String mId, String mEventType, long mEventDateTime, int mStage) {
        this.mId = mId;
        this.mEventType = mEventType;
        this.mEventDateTime = mEventDateTime;
        this.mStage = mStage;
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
}
