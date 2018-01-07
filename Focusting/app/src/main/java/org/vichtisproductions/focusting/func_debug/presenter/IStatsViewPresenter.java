package org.vichtisproductions.focusting.func_debug.presenter;

import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.func_debug.view.IStatsView;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IStatsViewPresenter {
    void setView(IStatsView view);
    void clearView();
    void onAttached();
    void onDetached();
    void refresh();
    void setStage(Stage stage);
    void clearDayClicked();
    void clearHourClicked();
    void clearStageClicked();
}
