package org.coderswithoutborders.deglancer.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
// For RIS button
import android.content.Context;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.func_debug.stage1.DebugStage1Activity;
import org.coderswithoutborders.deglancer.func_debug.stage2.DebugStage2Activity;
import org.coderswithoutborders.deglancer.func_debug.stage3.DebugStage3Activity;
import org.coderswithoutborders.deglancer.func_debug.stage4.DebugStage4Activity;
import org.coderswithoutborders.deglancer.func_debug.stage5.DebugStage5Activity;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.presenter.IMainActivityPresenter;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.pretest.PreTestActivity;
import org.coderswithoutborders.deglancer.services.TrackerService;

import javax.inject.Inject;

/**
 * Created by chris.teli on 3/20/2016.
 */
public class MainActivity extends AppCompatActivity implements IMainActivityView {

    @Inject
    IMainActivityPresenter mPresenter;

    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainApplication.from(this).getGraph().inject(this);

        Intent i = new Intent(getApplicationContext(), TrackerService.class);
        getApplicationContext().startService(i);

        findViewById(R.id.btnDebug).setOnClickListener(v -> mPresenter.debugClicked());
        findViewById(R.id.TextResInfoSheet).setOnClickListener(v -> showRIS());

        findViewById(R.id.btnPreTest).setOnClickListener(v -> showPreTest());
        if (mPresenter.isPreTestRun()) {
            findViewById(R.id.btnPreTest).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btnPreTest).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.setView(this);
        mPresenter.init();
        if (mPresenter.isPreTestRun()) {
            findViewById(R.id.btnPreTest).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btnPreTest).setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
        if (mPresenter.isPreTestRun()) {
            findViewById(R.id.btnPreTest).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btnPreTest).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        mPresenter.clearView();

        super.onPause();
    }


    @Override
    public void showStage1View(Stage stage) {
        try {
            Intent i = new Intent(this, DebugStage1Activity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showStage2View(Stage stage) {
        try {
            Intent i = new Intent(this, DebugStage2Activity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showStage3View(Stage stage) {
        try {
            Intent i = new Intent(this, DebugStage3Activity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showStage4View(Stage stage) {
        try {
            Intent i = new Intent(this, DebugStage4Activity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showStage5View(Stage stage) {
        try {
            Intent i = new Intent(this, DebugStage5Activity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setStageText(String stage) {
        ((TextView)findViewById(R.id.tvStage)).setText(stage);
    }

    public void setIntroText(int stage, int day) {
        String str;

        String dayString;
        String remainingTimeText;
        int stageLength=0;

        int[] lengthOfStage = getResources().getIntArray(R.array.eachStageLength);
        stageLength = lengthOfStage[stage - 1];
        String[] introTexts = getResources().getStringArray((R.array.IntroTexts));
        str = introTexts[stage - 1];

        ((TextView)findViewById(R.id.tvIntro)).setText(str);
        remainingTimeText = getResources().getString(R.string.RemainingTimeText);
        int remainingTime;
        remainingTime = stageLength - day + 1;
        dayString = Integer.toString(remainingTime);
        ((TextView)findViewById(R.id.tvStageRemainingTime)).setText(dayString + " " + remainingTimeText);

    }

    public void showPreTest() {
        try {
            Intent i = new Intent(this, PreTestActivity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRIS() {
        try {
            Intent i = new Intent(this, ResearchInformationSheet.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
