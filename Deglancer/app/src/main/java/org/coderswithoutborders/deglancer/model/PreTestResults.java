package org.coderswithoutborders.deglancer.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Lapa on 2016/06/29.
 */
public class PreTestResults extends RealmObject {

    @PrimaryKey
    private String mId;
    private int answerone;
    private int answertwo;
    private int answerthree;
    private int answerfour;
    private int answerfive;
    private int answersix;
    private int answerseven;
    private int answereight;
    private int answernine;
    private int answerten;

    public PreTestResults() {

    }

    public PreTestResults(String mId, int answerone, int answertwo, int answerthree, int answerfour, int answerfive, int answersix, int answerseven, int answereight, int answernine, int answerten) {
        this.mId = mId;
        this.answerone = answerone;
        this.answertwo = answertwo;
        this.answerthree = answerthree;
        this.answerfour = answerfour;
        this.answerfive = answerfive;
        this.answersix = answersix;
        this.answerseven = answerseven;
        this.answereight = answereight;
        this.answernine = answernine;
        this.answerten = answerten;
    }

    public String getId() {
        return mId;
    }

}
