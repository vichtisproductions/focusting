package org.vichtisproductions.focusting.func_debug.stage4;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage4Presenter {
    void setView(IDebugStage4View view);
    void clearView();
    void onAttached();
    void onDetached();
    void advanceStageClicked();
    void previousStageClicked();
}
