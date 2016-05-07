package org.coderswithoutborders.deglancer.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.presenter.IAveragesSetViewPresenter;
import org.coderswithoutborders.deglancer.utils.TimePickerDialog;

import javax.inject.Inject;

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
    private Button btnSave;

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
            btnSave = (Button) findViewById(R.id.btnSave);

            btnSOTPick.setOnClickListener(buttonClickListener);
            btnSFTPick.setOnClickListener(buttonClickListener);
            btnSave.setOnClickListener(buttonClickListener);
        }
    }

    private OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnSOTPick) {
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
        } else if (v.getId() == R.id.btnSave) {
            try {
                mPresenter.avgUnlocksPicked(Integer.parseInt(String.valueOf(etUnlocks.getText())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                mPresenter.avgUnlocksPicked(0);
            }

            mPresenter.saveClicked();
        }
    };


    public void setStage(int stage) {
        mPresenter.setStage(stage);
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

    @Override
    public void setAvgSOTText(String text) {
        btnSOTPick.setText(text);
    }

    @Override
    public void setAvgSFTText(String text) {
        btnSFTPick.setText(text);
    }
}
