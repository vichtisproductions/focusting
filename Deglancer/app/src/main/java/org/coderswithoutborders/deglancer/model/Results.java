package org.coderswithoutborders.deglancer.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Lapa on 2016/06/29.
 */
public class Results extends RealmObject {

    @PrimaryKey
    private String mId;
    private String preTestQ1;
    private String preTestQ2;
    private String preTestQ3;
    private String preTestQ4;
    private String preTestQ5;
    private String preTestQ6;
    private String preTestQ7;
    private String preTestQ8;
    private String preTestQ9;
    private String preTestQ10;

    public Results() {

    }

    public Results(String mId, String preTestQ1, String preTestQ2, String preTestQ3, String preTestQ4, String preTestQ5, String preTestQ6, String preTestQ7, String preTestQ8, String preTestQ9, String preTestQ10) {
        this.mId = mId;
        this.preTestQ1 = preTestQ1;
        this.preTestQ2 = preTestQ2;
        this.preTestQ3 = preTestQ3;
        this.preTestQ4 = preTestQ4;
        this.preTestQ5 = preTestQ5;
        this.preTestQ6 = preTestQ6;
        this.preTestQ7 = preTestQ7;
        this.preTestQ8 = preTestQ8;
        this.preTestQ9 = preTestQ9;
        this.preTestQ10 = preTestQ10;
    }

    public String getId() {
        return mId;
    }

}
