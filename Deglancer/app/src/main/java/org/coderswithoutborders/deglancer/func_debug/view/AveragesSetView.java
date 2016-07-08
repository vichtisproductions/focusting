package org.coderswithoutborders.deglancer.func_debug.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.func_debug.presenter.IAveragesSetViewPresenter;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.utils.TimePickerDialog;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Renier on 2016/05/07.
 */
public class AveragesSetView extends FrameLayout implements IAveragesSetView {

    @Inject
    IAveragesSetViewPresenter mPresenter;

    private TextView tvStageLabel;
    private EditText etUnlocks;
    private Button btnSOTPick;
    private Button btnSFTPick;
    private Button btnTotalSOTPick;
    private Button btnTotalSFTPick;
    private Button btnSetForCurrentStageDayHour;
    private Button btnSetForCurrentStageDay;
    private Button btnSetForCurrentStage;

    private Activity mActivity;

    public AveragesSetView(Context context) {
        super(context);
        init();
    }

    public AveragesSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AveragesSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AveragesSetView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void updateTitleTextWith(String text) {
        tvStageLabel.setText(String.format(getContext().getString(R.string.averages_set_view_stage_label), text));
    }
    public void setStage(Stage stage) {
        mPresenter.setStage(stage);
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void clearActivity() {
        mActivity = null;
    }

    private void init() {
        inflate(getContext(), R.layout.averages_set_view, this);

        if (!isInEditMode()) {
            MainApplication.from(getContext()).getGraph().inject(this);

            tvStageLabel = (TextView) findViewById(R.id.tvStageLabel);
            etUnlocks = (EditText) findViewById(R.id.etUnlocks);
            btnSOTPick = (Button) findViewById(R.id.btnSOTPick);
            btnSFTPick = (Button) findViewById(R.id.btnSFTPick);
            btnTotalSOTPick = (Button) findViewById(R.id.btnTotalSOTPick);
            btnTotalSFTPick = (Button) findViewById(R.id.btnTotalSFTPick);
            btnSetForCurrentStageDayHour = (Button) findViewById(R.id.btnSetForCurrentStageDayHour);
            btnSetForCurrentStageDay = (Button) findViewById(R.id.btnSetForCurrentStageDay);
            btnSetForCurrentStage = (Button) findViewById(R.id.btnSetForCurrentStage);

            btnSOTPick.setOnClickListener(buttonClickListener);
            btnSFTPick.setOnClickListener(buttonClickListener);
            btnTotalSOTPick.setOnClickListener(buttonClickListener);
            btnTotalSFTPick.setOnClickListener(buttonClickListener);
            btnSetForCurrentStageDayHour.setOnClickListener(buttonClickListener);
            btnSetForCurrentStageDay.setOnClickListener(buttonClickListener);
            btnSetForCurrentStage.setOnClickListener(buttonClickListener);
        }
    }

    private OnClickListener buttonClickListener = v -> {
        Timber.d("Button clicked to " + String.valueOf(v.getId()));
        if (v.getId() == R.id.btnSOTPick) {
            Timber.d("It was a btnSOTPick");
            TimePickerDialog dialog = new TimePickerDialog(mActivity);
            dialog.setTimePickerEventListener(new TimePickerDialog.TimePickerEventListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onTimeSelected(long millis) {
                    mPresenter.avgSOTPicked(millis);
                }
            });
            Timber.d("All set, let's show.");
            dialog.show();
        } else if (v.getId() == R.id.btnSFTPick) {
            TimePickerDialog dialog = new TimePickerDialog(mActivity);
            dialog.setTimePickerEventListener(new TimePickerDialog.TimePickerEventListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onTimeSelected(long millis) {
                    mPresenter.avgSFTPicked(millis);
                }
            });
            dialog.show();
        }  else if (v.getId() == R.id.btnTotalSOTPick) {
            TimePickerDialog dialog = new TimePickerDialog(mActivity);
            dialog.setTimePickerEventListener(new TimePickerDialog.TimePickerEventListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onTimeSelected(long millis) {
                    mPresenter.totalSOTPicked(millis);
                }
            });
            dialog.show();
        } else if (v.getId() == R.id.btnTotalSFTPick) {
            TimePickerDialog dialog = new TimePickerDialog(mActivity);
            dialog.setTimePickerEventListener(new TimePickerDialog.TimePickerEventListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onTimeSelected(long millis) {
                    mPresenter.totalSFTPicked(millis);
                }
            });
            dialog.show();
        }
        else if (v.getId() == R.id.btnSetForCurrentStageDay) {
            try {
                mPresenter.avgUnlocksPicked(Integer.parseInt(String.valueOf(etUnlocks.getText())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                mPresenter.avgUnlocksPicked(0);
            }

            mPresenter.setForStageDayClicked();
        } else if (v.getId() == R.id.btnSetForCurrentStageDayHour) {
            try {
                mPresenter.avgUnlocksPicked(Integer.parseInt(String.valueOf(etUnlocks.getText())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                mPresenter.avgUnlocksPicked(0);
            }

            mPresenter.setForStageDayHourClicked();
        } else if (v.getId() == R.id.btnSetForCurrentStage) {
            try {
                mPresenter.avgUnlocksPicked(Integer.parseInt(String.valueOf(etUnlocks.getText())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                mPresenter.avgUnlocksPicked(0);
            }

            mPresenter.setForStageClicked();
        }
    };

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

    @Override
    public void setAvgSOTText(String text) {
        btnSOTPick.setText(text);
    }

    @Override
    public void setAvgSFTText(String text) {
        btnSFTPick.setText(text);
    }

    @Override
    public void setTotalSOTText(String text) {
        btnTotalSOTPick.setText(text);
    }

    @Override
    public void setTotalSFTText(String text) {
        btnTotalSFTPick.setText(text);
    }
}
