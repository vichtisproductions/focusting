package org.vichtisproductions.focusting.presenter;

import org.vichtisproductions.focusting.func_debug.view.ITargetSetView;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.model.Stage6Toast;
import org.vichtisproductions.focusting.view.IStage6ToastSetView;

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
