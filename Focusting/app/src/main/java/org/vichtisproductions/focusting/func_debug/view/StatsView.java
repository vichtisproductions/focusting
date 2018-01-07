package org.vichtisproductions.focusting.func_debug.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.func_debug.presenter.IStatsViewPresenter;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class StatsView extends FrameLayout implements IStatsView {

    @Inject
    IStatsViewPresenter mPresenter;

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

            findViewById(R.id.btnClearStage).setOnClickListener(buttonClickListener);
            findViewById(R.id.btnClearDay).setOnClickListener(buttonClickListener);
            findViewById(R.id.btnClearHour).setOnClickListener(buttonClickListener);
        }
    }

    public void refresh() {
        mPresenter.refresh();
    }

    public void setStage(Stage stage) {
        mPresenter.setStage(stage);
    }


    OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnClearStage) {
            mPresenter.clearStageClicked();
        } else if (v.getId() == R.id.btnClearDay) {
            mPresenter.clearDayClicked();
        } else if (v.getId() == R.id.btnClearHour) {
            mPresenter.clearHourClicked();
        }
    };

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
