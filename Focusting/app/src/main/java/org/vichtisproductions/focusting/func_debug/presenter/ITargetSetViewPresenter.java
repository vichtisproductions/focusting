package org.vichtisproductions.focusting.func_debug.presenter;

import org.vichtisproductions.focusting.func_debug.view.ITargetSetView;
import org.vichtisproductions.focusting.model.Stage;

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
