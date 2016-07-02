package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.view.IMainActivityView;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IMainActivityPresenter {

    void setView(IMainActivityView view);
    void clearView();
    void init();
    void debugClicked();
    boolean isPreTestRun();
    int whatStage();
    int whatTarget();
}
