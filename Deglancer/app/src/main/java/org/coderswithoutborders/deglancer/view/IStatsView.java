package org.coderswithoutborders.deglancer.view;

import org.coderswithoutborders.deglancer.model.Stage;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IStatsView {
    void setValues(
            String totalUnlocksDay, String totalUnlocksHour,
            String totalSOTDay, String totalSOTHour,
            String totalSFTDay, String totalSFTHour,
            String avgSOTDay, String avgSOTHour,
            String avgSFTDay, String avgSFTHour);
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
