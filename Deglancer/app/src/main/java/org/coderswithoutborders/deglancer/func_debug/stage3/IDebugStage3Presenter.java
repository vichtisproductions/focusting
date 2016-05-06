package org.coderswithoutborders.deglancer.func_debug.stage3;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage3Presenter {
    void setView(IDebugStage3View view);
    void clearView();
    void onAttached();
    void onDetached();
}
