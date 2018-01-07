package org.vichtisproductions.focusting.func_debug.stage2;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage2Presenter {
    void setView(IDebugStage2View view);
    void clearView();
    void onAttached();
    void onDetached();
    void advanceStageClicked();
    void previousStageClicked();
}
