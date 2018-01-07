package org.vichtisproductions.focusting.func_debug.presenter;

import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.bus.events.StageSelectEvent;
import org.vichtisproductions.focusting.func_debug.view.IStageSelectView;
import org.vichtisproductions.focusting.model.Stage;

/**
 * Created by Renier on 2016/05/08.
 */
public class StageSelectViewPresenter implements IStageSelectViewPresenter {
    private IStageSelectView mView;
    private RxBus mBus;
    private Stage mCurrentStage;

    private boolean mEnableStageChange = true;
    private boolean mEnableDayChange = true;
    private boolean mEnableHourChange = true;

    public StageSelectViewPresenter(RxBus bus) {
        this.mBus = bus;
    }

    @Override
    public void setView(IStageSelectView view) {
        mView = view;

        enableDisableStagePickerControls();
    }

    @Override
    public void clearView() {
        mView = null;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }

    @Override
    public void onStageChange(int newVal) {
        if (mCurrentStage != null)
            mCurrentStage.setStage(newVal);

        if (mView != null) {
            refreshValues();
        }
        mBus.post(new StageSelectEvent(mCurrentStage));
    }

    @Override
    public void onDayChange(int day) {
        if (mCurrentStage != null)
            mCurrentStage.setDay(day);

        if (mView != null) {
            refreshValues();
        }
        mBus.post(new StageSelectEvent(mCurrentStage));
    }

    @Override
    public void onHourChange(int hour) {
        if (mCurrentStage != null)
            mCurrentStage.setHour(hour);

        if (mView != null) {
            refreshValues();
        }
        mBus.post(new StageSelectEvent(mCurrentStage));
    }


    @Override
    public void setStage(Stage stage) {
        mCurrentStage = stage;

        refreshValues();
    }

    private void refreshValues() {

        if (mView != null) {
            mView.setStageVal(mCurrentStage.getStage());
            mView.setStageDay(mCurrentStage.getDay());
            mView.setStageHour(mCurrentStage.getHour());
        }
    }


    @Override
    public void setStagePickerState(boolean enableStageChange, boolean enableDayChange, boolean enableHourChange) {
        mEnableStageChange = enableStageChange;
        mEnableDayChange = enableDayChange;
        mEnableHourChange = enableHourChange;

        enableDisableStagePickerControls();
    }

    private void enableDisableStagePickerControls() {
        if (mView != null) {
            if (mEnableStageChange) {
                mView.enableStagePicker();
            } else {
                mView.disableStagePicker();
            }

            if (mEnableDayChange) {
                mView.enableDayPicker();
            } else {
                mView.disableDayPicker();
            }

            if (mEnableHourChange) {
                mView.enableHourPicker();
            } else {
                mView.disableHourPicker();
            }
        }
    }

    @Override
    public void setStageAsCurrentStage() {
        if (mCurrentStage != null) {

        }
    }
}
