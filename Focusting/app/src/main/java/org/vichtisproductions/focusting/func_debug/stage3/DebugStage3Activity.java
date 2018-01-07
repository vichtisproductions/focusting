package org.vichtisproductions.focusting.func_debug.stage3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.func_debug.stage2.DebugStage2Activity;
import org.vichtisproductions.focusting.func_debug.stage4.DebugStage4Activity;
import org.vichtisproductions.focusting.func_debug.view.AveragesSetView;
import org.vichtisproductions.focusting.func_debug.view.StageSelectView;
import org.vichtisproductions.focusting.func_debug.view.StatsView;
import org.vichtisproductions.focusting.model.Stage;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage3Activity extends AppCompatActivity implements IDebugStage3View {

    @Inject
    IDebugStage3Presenter mPresenter;

    private Button btnAdvance;
    private Button btnBack;
    private StatsView mStatsView;
    private StageSelectView mStageSelectView;
    private AveragesSetView mAvgSetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.debug_stage3);

        MainApplication.from(getApplicationContext()).getGraph().inject(this);

        mStatsView = (StatsView)findViewById(R.id.statsView);

        mStageSelectView = (StageSelectView) findViewById(R.id.stageSelectView);
        mStageSelectView.setStagePickerState(false, true, true);

        btnAdvance = (Button) findViewById(R.id.btnAdvance);
        btnAdvance.setOnClickListener(buttonClickListener);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(buttonClickListener);

        mAvgSetView = (AveragesSetView) findViewById(R.id.averagesSetView);
        mAvgSetView.setStage(new Stage(2, 1, 1));

    }

    View.OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnAdvance) {
            mPresenter.advanceStageClicked();
        } else if (v.getId() == R.id.btnBack) {
            mPresenter.previousStageClicked();
        }
    };

    @Override
    public void setStage(Stage stage) {
        mStageSelectView.setStage(stage);
    }

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
        Intent i = new Intent(this, DebugStage4Activity.class);
        startActivity(i);
    }

    @Override
    public void setTitleStage(String stage) {
        setTitle(String.format(getApplicationContext().getString(R.string.activity_debug_stage_activity_title_from_code), stage));
    }
}
