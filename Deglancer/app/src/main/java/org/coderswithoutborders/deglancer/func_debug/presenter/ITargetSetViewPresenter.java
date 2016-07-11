package org.coderswithoutborders.deglancer.func_debug.presenter;

import org.coderswithoutborders.deglancer.func_debug.view.ITargetSetView;
import org.coderswithoutborders.deglancer.model.Stage;

/**
 * Created by Renier on 2016/05/14.
 */
public interface ITargetSetViewPresenter {
    void setView(ITargetSetView view);
    void clearView();
    void onAttached();
    void onDetached();
    void setStage(Stage stage);
    void setTargetTapped(int target);
    void setRadioButtonRight(int i);
}
