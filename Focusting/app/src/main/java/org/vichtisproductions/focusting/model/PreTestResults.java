package org.vichtisproductions.focusting.model;

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

    public int getAns1() {
        return answerone;
    }
    public int getAns2() {
        return answertwo;
    }
    public int getAns3() {
        return answerthree;
    }
    public int getAns4() {
        return answerfour;
    }
    public int getAns5() {
        return answerfive;
    }
    public int getAns6() {
        return answersix;
    }
    public int getAns7() {
        return answerseven;
    }
    public int getAns8() {
        return answereight;
    }
    public int getAns9() {
        return answernine;
    }
    public int getAns10() {
        return answerten;
    }

    public void setAns1(int ans) { this.answerone = ans; }
    public void setAns2(int ans) {
        this.answertwo = ans;
    }
    public void setAns3(int ans) {
        this.answerthree = ans;
    }
    public void setAns4(int ans) {
        this.answerfour = ans;
    }
    public void setAns5(int ans) { this.answerfive = ans; }
    public void setAns6(int ans) {
        this.answersix = ans;
    }
    public void setAns7(int ans) { this.answerseven = ans; }
    public void setAns8(int ans) { this.answereight = ans; }
    public void setAns9(int ans) { this.answernine = ans; }
    public void setAns10(int ans) { this.answerten = ans; }

}
