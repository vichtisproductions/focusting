package org.vichtisproductions.focusting.func_debug.stage6;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage6Presenter {
    void setView(IDebugStage6View view);
    void clearView();
    void onAttached();
    void onDetached();
    void previousStageClicked();
}
