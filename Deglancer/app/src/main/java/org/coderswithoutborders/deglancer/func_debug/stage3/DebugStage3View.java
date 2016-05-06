package org.coderswithoutborders.deglancer.func_debug.stage3;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.view.StatsView;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage3View extends FrameLayout implements IDebugStage3View {

    @Inject
    IDebugStage3Presenter mPresenter;

    private StatsView mStatsView;

    public DebugStage3View(Context context) {
        super(context);
        init();
    }

    public DebugStage3View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DebugStage3View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DebugStage3View(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        inflate(getContext(), R.layout.debug_stage1, this);

        if (!isInEditMode()) {
            MainApplication.from(getContext()).getGraph().inject(this);

            mStatsView = (StatsView)findViewById(R.id.statsView);
        }
    }

    @Override
    public void setStage(Stage stage) {
        mStatsView.setStage(stage);
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
