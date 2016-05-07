package org.coderswithoutborders.deglancer.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.presenter.IStatsViewPresenter;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class StatsView extends FrameLayout implements IStatsView {

    @Inject
    IStatsViewPresenter mPresenter;


    private NumberPicker npStage;
    private NumberPicker npDay;
    private NumberPicker npHour;

    private TextView tvTotalUnlocksDay;
    private TextView tvTotalUnlocksHour;
    private TextView tvTotalSOTDay;
    private TextView tvTotalSOTHour;
    private TextView tvTotalSFTDay;
    private TextView tvTotalSFTHour;
    private TextView tvAvgSOTDay;
    private TextView tvAvgSOTHour;
    private TextView tvAvgSFTDay;
    private TextView tvAvgSFTHour;

    public StatsView(Context context) {
        super(context);
        init();
    }

    public StatsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StatsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        inflate(getContext(), R.layout.stats_view, this);

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

            tvTotalUnlocksDay = (TextView) findViewById(R.id.tvTotalUnlocksDay);
            tvTotalUnlocksHour = (TextView) findViewById(R.id.tvTotalUnlocksHour);
            tvTotalSOTDay = (TextView) findViewById(R.id.tvTotalSOTDay);
            tvTotalSOTHour = (TextView) findViewById(R.id.tvTotalSOTHour);
            tvTotalSFTDay = (TextView) findViewById(R.id.tvTotalSFTDay);
            tvTotalSFTHour = (TextView) findViewById(R.id.tvTotalSFTHour);
            tvAvgSOTDay = (TextView) findViewById(R.id.tvAvgSOTDay);
            tvAvgSOTHour = (TextView) findViewById(R.id.tvAvgSOTHour);
            tvAvgSFTDay = (TextView) findViewById(R.id.tvAvgSFTDay);
            tvAvgSFTHour = (TextView) findViewById(R.id.tvAvgSFTHour);

            findViewById(R.id.btnClearDay).setOnClickListener(buttonClickListener);
            findViewById(R.id.btnClearHour).setOnClickListener(buttonClickListener);
        }
    }

    public void setStagePickerState(boolean enableStageChange, boolean enableDayChange, boolean enableHourChange) {
        mPresenter.setStagePickerState(enableStageChange, enableDayChange, enableHourChange);
    }

    public void refresh() {
        mPresenter.refresh();
    }

    public void setStage(Stage stage) {
        mPresenter.setStage(stage);
    }


    OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnClearDay) {
            mPresenter.clearDayClicked();
        } else if (v.getId() == R.id.btnClearHour) {
            mPresenter.clearHourClicked();
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
    public void setValues(String totalUnlocksDay, String totalUnlocksHour,
                          String totalSOTDay, String totalSOTHour,
                          String totalSFTDay, String totalSFTHour,
                          String avgSOTDay, String avgSOTHour,
                          String avgSFTDay, String avgSFTHour) {

        tvTotalUnlocksDay.setText(totalUnlocksDay);
        tvTotalUnlocksHour.setText(totalUnlocksHour);
        tvTotalSOTDay.setText(totalSOTDay);
        tvTotalSOTHour.setText(totalSOTHour);
        tvTotalSFTDay.setText(totalSFTDay);
        tvTotalSFTHour.setText(totalSFTHour);
        tvAvgSOTDay.setText(avgSOTDay);
        tvAvgSOTHour.setText(avgSOTHour);
        tvAvgSFTDay.setText(avgSFTDay);
        tvAvgSFTHour.setText(avgSFTHour);
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
