package org.coderswithoutborders.deglancer.func_debug.stage1;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage1Presenter {
    void setView(IDebugStage1View view);
    void clearView();
    void onAttached();
    void onDetached();
}
