package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.func_debug.view.ITargetSetView;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.model.Stage6Toast;
import org.coderswithoutborders.deglancer.view.IStage6ToastSetView;

/**
 * Created by Renier on 2016/05/14.
 */
public interface IStage6ToastSetViewPresenter {
    void setView(IStage6ToastSetView view);
    void clearView();
    void onAttached();
    void onDetached();
    void setStage(Stage stage);
    void setStage6ToastTapped(int target);
    // Stage6Toast getTargetForStage6();
    void setRadioButtonRight(int stage);
}
