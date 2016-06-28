package org.coderswithoutborders.deglancer.func_debug.stage1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.func_debug.stage2.DebugStage2Activity;
import org.coderswithoutborders.deglancer.func_debug.view.StageSelectView;
import org.coderswithoutborders.deglancer.func_debug.view.StatsView;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.view.TakeTheTest;

import android.view.View.OnClickListener;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage1Activity extends AppCompatActivity implements IDebugStage1View {

    @Inject
    IDebugStage1Presenter mPresenter;

    private Button btnAdvance;
    private Button button;
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

        addTestButton();

        MainApplication.from(getApplicationContext()).getGraph().inject(this);


    }

    View.OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnAdvance) {
            mPresenter.advanceStageClicked();
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

    public void addTestButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.btnTakePreTest);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, TakeTheTest.class);
                startActivity(intent);

            }

        });

    }
}

