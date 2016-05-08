package org.coderswithoutborders.deglancer.func_debug.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.func_debug.presenter.IStageSelectViewPresenter;
import org.coderswithoutborders.deglancer.model.Stage;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/08.
 */
public class StageSelectView extends FrameLayout implements IStageSelectView {
    @Inject
    IStageSelectViewPresenter mPresenter;


    private NumberPicker npStage;
    private NumberPicker npDay;
    private NumberPicker npHour;

    public StageSelectView(Context context) {
        super(context);
        init();
    }

    public StageSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StageSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StageSelectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.stage_select_view, this);

        if (!isInEditMode()) {
            MainApplication.from(getContext()).getGraph().inject(this);

            npStage = (NumberPicker) findViewById(R.id.npStage);
            npDay = (NumberPicker) findViewById(R.id.npDay);
            npHour = (NumberPicker) findViewById(R.id.npHour);

            npStage.setMinValue(1);
            npStage.setMaxValue(5);

            npDay.setMinValue(1);
            npDay.setMaxValue(7);

            npHour.setMinValue(1);
            npHour.setMaxValue(23);

            npStage.setOnValueChangedListener(valueChangeListener);
            npDay.setOnValueChangedListener(valueChangeListener);
            npHour.setOnValueChangedListener(valueChangeListener);

            findViewById(R.id.btnSetStageAsCurrentStage).setOnClickListener(buttonClickListener);
        }
    }

    OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnSetStageAsCurrentStage) {
            mPresenter.setStageAsCurrentStage();

            Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_LONG).show();
        }
    };

    NumberPicker.OnValueChangeListener valueChangeListener = (picker, oldVal, newVal) -> {
        if (picker.getId() == R.id.npStage) {
            mPresenter.onStageChange(newVal);
        } else if (picker.getId() == R.id.npDay) {
            mPresenter.onDayChange(newVal);
        } else if (picker.getId() == R.id.npHour) {
            mPresenter.onHourChange(newVal);
        }

    };

    public void setStage(Stage stage) {
        mPresenter.setStage(stage);
    }

    public void setStagePickerState(boolean enableStageChange, boolean enableDayChange, boolean enableHourChange) {
        mPresenter.setStagePickerState(enableStageChange, enableDayChange, enableHourChange);
    }

    @Override
    public void setStageVal(int stage) {
        npStage.setValue(stage);
    }

    @Override
    public void setStageDay(int day) {
        npDay.setValue(day);
    }

    @Override
    public void setStageHour(int hour) {
        npHour.setValue(hour);
    }

    @Override
    public void enableStagePicker() {
        npStage.setEnabled(true);
    }

    @Override
    public void disableStagePicker() {
        npStage.setEnabled(false);
    }

    @Override
    public void enableDayPicker() {
        npDay.setEnabled(true);
    }

    @Override
    public void disableDayPicker() {
        npDay.setEnabled(false);
    }

    @Override
    public void enableHourPicker() {
        npHour.setEnabled(true);
    }

    @Override
    public void disableHourPicker() {
        npHour.setEnabled(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mPresenter != null) {
            mPresenter.setView(this);
            mPresenter.onAttached();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mPresenter != null) {
            mPresenter.onDetached();
            mPresenter.clearView();
        }
    }
}
