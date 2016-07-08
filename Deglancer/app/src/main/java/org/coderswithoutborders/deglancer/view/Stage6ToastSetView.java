package org.coderswithoutborders.deglancer.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.model.Stage6Toast;
import org.coderswithoutborders.deglancer.presenter.IStage6ToastSetViewPresenter;
import org.coderswithoutborders.deglancer.view.IStage6ToastSetView;
import org.coderswithoutborders.deglancer.model.Stage;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Renier on 2016/05/14.
 */
public class Stage6ToastSetView extends FrameLayout implements IStage6ToastSetView {

    @Inject
    IStage6ToastSetViewPresenter mPresenter;

    private RadioButton rdbNoToast;
    private RadioButton rdbInformation;
    private RadioButton rdbThumbsUp;

    private FirebaseAnalytics mFirebaseAnalytics;

    public Stage6ToastSetView(Context context) {
        super(context);

        init();
    }

    public Stage6ToastSetView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public Stage6ToastSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Stage6ToastSetView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.stage6_toast_set_view, this);

        if (!isInEditMode()) {
            MainApplication.from(getContext()).getGraph().inject(this);

            findViewById(R.id.btnApplyStage6Toast).setOnClickListener(buttonClickListener);

            rdbNoToast = (RadioButton) findViewById(R.id.rdbNoToast);
            rdbInformation = (RadioButton) findViewById(R.id.rdbInformation);
            rdbThumbsUp = (RadioButton) findViewById(R.id.rdbThumbsUp);

            // We know we are in stage 6, so let's put the radio button to the right place
            // Let mPresenter figure out the rest
            setToastRight();
        }
    }

    public void setToastRight() {
        mPresenter.setRadioButtonRight(6);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int target = 0;
            String ToastText ="";
            if (rdbNoToast.isChecked()) {
                target = 0;
                ToastText = getContext().getString(R.string.rdbStage6NoInformationText);
            } else if (rdbInformation.isChecked()) {
                target = 1;
                ToastText = getContext().getString(R.string.rdbStage6InformationText);
            } else if (rdbThumbsUp.isChecked()) {
                target = 2;
                ToastText = getContext().getString(R.string.rdbStage6InformationandThumbsUpText);
            }

            Timber.d( "Setting target for " + Integer.toString(target));
            mPresenter.setStage6ToastTapped(target);
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.toastStage6SetToastNotificationText) + ToastText, 3);
            toast.setGravity(Gravity.BOTTOM, 0, 30);
            toast.show();

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
            Bundle fbAnalyticsBundle = new Bundle();
            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "stage6_toast_" + Integer.toString(target));
            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Stage 6 Toast: " + ToastText);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, fbAnalyticsBundle);

        }
    };

    @Override
    public void setNoToastSelected() { rdbNoToast.setChecked(true);  }

    @Override
    public void setInformationSelected() {
        rdbInformation.setChecked(true);
    }

    @Override
    public void setThumbsUpSelected() {
        rdbThumbsUp.setChecked(true);
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
