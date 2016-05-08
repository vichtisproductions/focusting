package org.coderswithoutborders.deglancer.func_debug.view;

/**
 * Created by Renier on 2016/05/08.
 */
public interface IStageSelectView {
    void setStageVal(int stage);
    void setStageDay(int day);
    void setStageHour(int hour);
    void enableStagePicker();
    void disableStagePicker();
    void enableDayPicker();
    void disableDayPicker();
    void enableHourPicker();
    void disableHourPicker();
}
