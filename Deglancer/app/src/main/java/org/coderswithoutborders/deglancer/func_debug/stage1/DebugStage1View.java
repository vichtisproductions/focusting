package org.coderswithoutborders.deglancer.func_debug.stage1;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.view.StatsView;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage1View extends FrameLayout implements IDebugStage1View {

    @Inject
    IDebugStage1Presenter mPresenter;

    private Button advanceStageButton;
    private StatsView mStatsView;
    private TextView tvStage;
    private NumberPicker npDay;
    private NumberPicker npHour;

    public DebugStage1View(Context context) {
        super(context);
        init();
    }

    public DebugStage1View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DebugStage1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DebugStage1View(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        inflate(getContext(), R.layout.debug_stage1, this);

        if (!isInEditMode()) {
            MainApplication.from(getContext()).getGraph().inject(this);

            mStatsView = (StatsView)findViewById(R.id.statsView);

            tvStage = (TextView) findViewById(R.id.tvStage);

            advanceStageButton = (Button) findViewById(R.id.btnAdvance);
            advanceStageButton.setOnClickListener(buttonClickListener);

            npDay = (NumberPicker) findViewById(R.id.npDay);
            npHour = (NumberPicker) findViewById(R.id.npHour);

            npDay.setMinValue(1);
            npDay.setMaxValue(7);

            npHour.setMinValue(1);
            npHour.setMaxValue(23);

            npDay.setOnValueChangedListener(valueChangeListener);
            npHour.setOnValueChangedListener(valueChangeListener);
        }
    }

    OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnAdvance) {
            mPresenter.advanceStageClicked();
        }
    };

    NumberPicker.OnValueChangeListener valueChangeListener = (picker, oldVal, newVal) -> {
        if (picker.getId() == R.id.npDay) {
            mPresenter.onDayChange(newVal);
        } else if (picker.getId() == R.id.npHour) {
            mPresenter.onHourChange(newVal);
        }
    };

    @Override
    public void setStage(Stage stage) {
        mStatsView.setStage(stage);
        tvStage.setText(String.valueOf(stage.getStage()));
    }

    @Override
    public void refreshStats() {
        mStatsView.refresh();
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
