package org.vichtisproductions.focusting.func_debug.stage1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.func_debug.stage2.DebugStage2Activity;
import org.vichtisproductions.focusting.func_debug.view.StageSelectView;
import org.vichtisproductions.focusting.func_debug.view.StatsView;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.pretest.PreTestActivity;

import android.view.View.OnClickListener;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage1Activity extends AppCompatActivity implements IDebugStage1View {

    @Inject
    IDebugStage1Presenter mPresenter;

    private Button btnAdvance;
    private Button button, button2;
    private StageSelectView mStageSelectView;
    private StatsView mStatsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.debug_stage1);

        mStatsView = (StatsView) findViewById(R.id.statsView);

        mStageSelectView = (StageSelectView) findViewById(R.id.stageSelectView);
        mStageSelectView.setStagePickerState(false, true, true);

        btnAdvance = (Button) findViewById(R.id.btnAdvance);
        btnAdvance.setOnClickListener(buttonClickListener);
        button = (Button) findViewById(R.id.btnTakePreTest);
        button.setOnClickListener(buttonClickListener);

        MainApplication.from(getApplicationContext()).getGraph().inject(this);

    }

    View.OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnAdvance) {
            mPresenter.advanceStageClicked();
        } else if (v.getId() == R.id.btnTakePreTest) {
            mPresenter.clearTestResults();
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
    }

    @Override
    protected void onPause() {
        if (mPresenter != null) {
            mPresenter.onDetached();
            mPresenter.clearView();
        }

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
    public void setTitleStage(String stage) {
        setTitle(String.format(getApplicationContext().getString(R.string.activity_debug_stage_activity_title_from_code), stage));
    }

}

