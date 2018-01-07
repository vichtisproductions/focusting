package org.vichtisproductions.focusting.func_debug.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.func_debug.presenter.ITargetSetViewPresenter;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.utils.AwesomeRadioButton.SegmentedGroup;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Renier on 2016/05/14.
 */
public class TargetSetView extends FrameLayout implements ITargetSetView, RadioGroup.OnCheckedChangeListener {

    @Inject
    ITargetSetViewPresenter mPresenter;

    private RadioButton rdb5;
    private RadioButton rdb10;
    private RadioButton rdb15;

    SegmentedGroup segmentedTargetGroup;

    private FirebaseAnalytics mFirebaseAnalytics;

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

            segmentedTargetGroup = (SegmentedGroup) findViewById(R.id.group);
            rdb5 = (RadioButton) findViewById(R.id.rdb5);
            rdb10 = (RadioButton) findViewById(R.id.rdb10);
            rdb15 = (RadioButton) findViewById(R.id.rdb15);

            segmentedTargetGroup.setOnCheckedChangeListener(this);

            setTargetRight();

        }
    }

    private void setTargetRight() {
        mPresenter.setRadioButtonRight(4);
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

            // Timber.d( "Setting target for " + Integer.toString(target));
            mPresenter.setTargetTapped(target);
            Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.tvGoalSetToastText) + target + " %", 3);
            toast.setGravity(Gravity.BOTTOM, 0, 30);
            toast.show();

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
            Bundle fbAnalyticsBundle = new Bundle();
            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "stage4");
            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Stage 4 target");
            fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "target");
            fbAnalyticsBundle.putLong(FirebaseAnalytics.Param.QUANTITY, (long) target);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, fbAnalyticsBundle);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        int target = 0;
        String ToastText = "";

        switch (checkedId) {
            case R.id.rdb5:
                target = 5;
                ToastText = getContext().getString(R.string.rdbStage6NoInformationText);
                break;
            case R.id.rdb10:
                target = 10;
                ToastText = getContext().getString(R.string.rdbStage6InformationText);
                break;
            case R.id.rdb15:
                target = 15;
                ToastText = getContext().getString(R.string.rdbStage6InformationandThumbsUpText);
                break;
            default:
                // Leave us empty-handed.
        }


        // Timber.d( "Setting target for " + Integer.toString(target));
        mPresenter.setTargetTapped(target);
        // Toast toast = Toast.makeText(getContext(), getContext().getString(R.string.tvGoalSetToastText) + target + " %", 3);
        // toast.setGravity(Gravity.BOTTOM, 0, 30);
        // toast.show();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        Bundle fbAnalyticsBundle = new Bundle();
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "stage4");
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Stage 4 target");
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "target");
        fbAnalyticsBundle.putLong(FirebaseAnalytics.Param.QUANTITY, (long) target);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, fbAnalyticsBundle);

    }
}
