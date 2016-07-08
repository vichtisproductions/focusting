package org.coderswithoutborders.deglancer.func_debug.stage5;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage5Presenter {
    void setView(IDebugStage5View view);
    void clearView();
    void onAttached();
    void onDetached();
    void advanceStageClicked();
    void previousStageClicked();
}
