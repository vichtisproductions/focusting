package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.view.IAveragesSetView;

/**
 * Created by Renier on 2016/05/07.
 */
public interface IAveragesSetViewPresenter {
    void setView(IAveragesSetView view);
    void clearView();
    void onAttached();
    void onDetached();
    void setStage(int stage);
    void saveClicked();
    void avgUnlocksPicked(int unlocks);
    void avgSOTPicked(long millis);
    void avgSFTPicked(long millis);
}
