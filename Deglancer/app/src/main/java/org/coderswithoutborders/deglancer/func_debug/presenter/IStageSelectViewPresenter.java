package org.coderswithoutborders.deglancer.func_debug.presenter;

import org.coderswithoutborders.deglancer.func_debug.view.IStageSelectView;
import org.coderswithoutborders.deglancer.model.Stage;

/**
 * Created by Renier on 2016/05/08.
 */
public interface IStageSelectViewPresenter {
    void setView(IStageSelectView view);
    void clearView();
    void onAttached();
    void onDetached();
    void onStageChange(int newVal);
    void onDayChange(int day);
    void onHourChange(int hour);
    void setStagePickerState(boolean enableStageChange, boolean enableDayChange, boolean enableHourChange);
    void setStage(Stage stage);
    void setStageAsCurrentStage();
}
