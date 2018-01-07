package org.vichtisproductions.focusting.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Renier on 2016/05/14.
 */
public class Stage6Toast extends RealmObject {

    @PrimaryKey
    private String mId;
    private int mStage;
    private int mToast;

    public Stage6Toast() {

    }

    public Stage6Toast(String mId, int mStage, int mToast) {
        this.mId = mId;
        this.mStage = mStage;
        this.mToast = mToast;
    }

    public String getId() {
        return mId;
    }

    public int getStage() {
        return mStage;
    }

    public int getTarget() {
        return mToast;
    }
}
