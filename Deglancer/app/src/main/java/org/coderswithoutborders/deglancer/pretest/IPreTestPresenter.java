package org.coderswithoutborders.deglancer.pretest;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IPreTestPresenter {
    void onAttached();
    void onDetached();
    void submitPreTestResults(int answerone, int answertwo, int answerthree, int answerfour, int answerfive, int answersix, int answerseven, int answereight, int answernine, int answerten);
}
