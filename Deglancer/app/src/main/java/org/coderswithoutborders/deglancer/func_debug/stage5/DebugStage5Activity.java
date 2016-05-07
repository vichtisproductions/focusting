package org.coderswithoutborders.deglancer.func_debug.stage5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.func_debug.stage2.DebugStage2Activity;
import org.coderswithoutborders.deglancer.func_debug.stage3.IDebugStage3Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage3.IDebugStage3View;
import org.coderswithoutborders.deglancer.view.AveragesSetView;
import org.coderswithoutborders.deglancer.view.StatsView;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage5Activity extends AppCompatActivity implements IDebugStage3View {

    @Inject
    IDebugStage3Presenter mPresenter;

    private Button btnAdvance;
    private Button btnBack;
    private StatsView mStatsView;
    private AveragesSetView mAvgSetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.debug_stage3);

        MainApplication.from(getApplicationContext()).getGraph().inject(this);

        mStatsView = (StatsView)findViewById(R.id.statsView);
        mStatsView.setStagePickerState(false, true, true);

        btnAdvance = (Button) findViewById(R.id.btnAdvance);
        btnAdvance.setOnClickListener(buttonClickListener);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(buttonClickListener);

        mAvgSetView = (AveragesSetView) findViewById(R.id.averagesSetView);
        mAvgSetView.setStage(3);
    }

    View.OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnAdvance) {
            mPresenter.advanceStageClicked();
        } else if (v.getId() == R.id.btnBack) {
            mPresenter.previousStageClicked();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (mPresenter != null) {
            mPresenter.setView(this);
            mPresenter.onAttached();
        }

        if (mAvgSetView != null)
            mAvgSetView.setActivity(this);
    }

    @Override
    protected void onPause() {
        if (mPresenter != null) {
            mPresenter.onDetached();
            mPresenter.clearView();
        }

        if (mAvgSetView != null)
            mAvgSetView.clearActivity();

        super.onPause();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void moveToStage2View() {
        Intent i = new Intent(this, DebugStage2Activity.class);
        startActivity(i);
    }

    @Override
    public void moveToStage4View() {
        /*Intent i = new Intent(this, DebugStage4Activity.class);
        startActivity(i);*/
    }
}
