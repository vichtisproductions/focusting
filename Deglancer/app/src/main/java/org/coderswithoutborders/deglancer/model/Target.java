package org.coderswithoutborders.deglancer.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Renier on 2016/05/14.
 */
public class Target extends RealmObject {

    @PrimaryKey
    private String mId;
    private int mStage;
    private int mTarget;

    public Target() {

    }

    public Target(String mId, int mStage, int mTarget) {
        this.mId = mId;
        this.mStage = mStage;
        this.mTarget = mTarget;
    }

    public String getId() {
        return mId;
    }

    public int getStage() {
        return mStage;
    }

    public int getTarget() {
        return mTarget;
    }
}
