package org.vichtisproductions.focusting.func_debug.presenter;

import org.vichtisproductions.focusting.func_debug.view.IAveragesSetView;
import org.vichtisproductions.focusting.model.Stage;

/**
 * Created by Renier on 2016/05/07.
 */
public interface IAveragesSetViewPresenter {
    void setView(IAveragesSetView view);
    void clearView();
    void onAttached();
    void onDetached();
    void setStage(Stage stage);
    void setForStageClicked();
    void setForStageDayClicked();
    void setForStageDayHourClicked();
    void avgUnlocksPicked(int unlocks);
    void avgSOTPicked(long millis);
    void avgSFTPicked(long millis);
    void totalSOTPicked(long millis);
    void totalSFTPicked(long millis);
}
