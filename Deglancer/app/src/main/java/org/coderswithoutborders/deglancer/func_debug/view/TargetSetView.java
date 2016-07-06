package org.coderswithoutborders.deglancer.func_debug.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.func_debug.presenter.ITargetSetViewPresenter;
import org.coderswithoutborders.deglancer.model.Stage;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Renier on 2016/05/14.
 */
public class TargetSetView extends FrameLayout implements ITargetSetView {

    @Inject
    ITargetSetViewPresenter mPresenter;

    private RadioButton rdb5;
    private RadioButton rdb10;
    private RadioButton rdb15;

    private static final String TAG = "TargetSetView";

    public TargetSetView(Context context) {
        super(context);

        init();
    }

    public TargetSetView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public TargetSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TargetSetView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.target_set_view, this);

        if (!isInEditMode()) {
            MainApplication.from(getContext()).getGraph().inject(this);

            findViewById(R.id.btnApply).setOnClickListener(buttonClickListener);

            rdb5 = (RadioButton) findViewById(R.id.rdb5);
            rdb10 = (RadioButton) findViewById(R.id.rdb10);
            rdb15 = (RadioButton) findViewById(R.id.rdb15);
        }
    }

    public void setStage(Stage stage) {
        mPresenter.setStage(stage);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int target = 0;
            if (rdb5.isChecked()) {
                target = 5;
            } else if (rdb10.isChecked()) {
                target = 10;
            } else if (rdb15.isChecked()) {
                target = 15;
            }

            Timber.d( "Setting target for " + Integer.toString(target));
            mPresenter.setTargetTapped(target);
        }
    };

    @Override
    public void set5Selected() { rdb5.setChecked(true);  }

    @Override
    public void set10Selected() {
        rdb10.setChecked(true);
    }

    @Override
    public void set15Selected() {
        rdb15.setChecked(true);
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
