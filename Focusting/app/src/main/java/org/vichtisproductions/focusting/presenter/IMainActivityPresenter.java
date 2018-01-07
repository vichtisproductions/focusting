package org.vichtisproductions.focusting.presenter;

import android.content.Context;

import org.vichtisproductions.focusting.view.IMainActivityView;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IMainActivityPresenter {

    void setView(IMainActivityView view);
    void clearView();
    void init();
    void debugClicked();
    void snoozeClicked(Context context);
    boolean isPreTestRun();
    int whatStage();
    int whatTarget();
    void adjustStartTime(Context context);
}
