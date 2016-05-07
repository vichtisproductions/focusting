package org.coderswithoutborders.deglancer.func_debug.stage1;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.view.StatsView;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage1Activity extends AppCompatActivity implements IDebugStage1View {

    @Inject
    IDebugStage1Presenter mPresenter;

    private Button btnAdvance;
    private StatsView mStatsView;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.debug_stage1);

        mStatsView = (StatsView) findViewById(R.id.statsView);
        mStatsView.setStagePickerState(false, true, true);

        btnAdvance = (Button) findViewById(R.id.btnAdvance);
        btnAdvance.setOnClickListener(buttonClickListener);

        MainApplication.from(getApplicationContext()).getGraph().inject(this);
    }

    View.OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnAdvance) {
            mPresenter.advanceStageClicked();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (mPresenter != null) {
            mPresenter.setView(this);
            mPresenter.onAttached();
        }
    }

    @Override
    protected void onPause() {
        if (mPresenter != null) {
            mPresenter.onDetached();
            mPresenter.clearView();
        }

        super.onPause();
    }
}
