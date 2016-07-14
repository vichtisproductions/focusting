package org.coderswithoutborders.deglancer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
// For RIS button
import android.widget.Button;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.coderswithoutborders.deglancer.BuildConfig;
import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.func_debug.stage1.DebugStage1Activity;
import org.coderswithoutborders.deglancer.func_debug.stage2.DebugStage2Activity;
import org.coderswithoutborders.deglancer.func_debug.stage3.DebugStage3Activity;
import org.coderswithoutborders.deglancer.func_debug.stage4.DebugStage4Activity;
import org.coderswithoutborders.deglancer.func_debug.stage5.DebugStage5Activity;
import org.coderswithoutborders.deglancer.func_debug.stage6.DebugStage6Activity;
import org.coderswithoutborders.deglancer.func_debug.view.TargetSetView;
import org.coderswithoutborders.deglancer.view.Stage6ToastSetView;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.presenter.IMainActivityPresenter;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.pretest.PreTestActivity;
import org.coderswithoutborders.deglancer.services.TrackerService;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by chris.teli on 3/20/2016.
 */
public class MainActivity extends AppCompatActivity implements IMainActivityView {

    @Inject
    IMainActivityPresenter mPresenter;

    private static final String TAG = "Deglancer.Main";

    private TargetSetView mTargetSetView;
    private Stage6ToastSetView mStage6ToastSetView;

    private FirebaseAnalytics mFirebaseAnalytics;

    private Button button;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MainApplication.from(this).getGraph().inject(this);

        Intent i = new Intent(getApplicationContext(), TrackerService.class);
        getApplicationContext().startService(i);

        mTargetSetView = (TargetSetView) findViewById(R.id.targetSetView);
        mStage6ToastSetView = (Stage6ToastSetView) findViewById(R.id.stage6ToastSetView);

        findViewById(R.id.btnPreTest).setOnClickListener(v -> showPreTest());


        FloatingActionButton fabShare = (FloatingActionButton) findViewById(R.id.fabShare);
        if (fabShare != null) {
            fabShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareIt();
                }
            });
        }

        setStageDependentViewsVisibility();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);

    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.textShareBody);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.textShareSubject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.textShareIntent)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // Don't show Debug in release
        if (BuildConfig.DEBUG) {
            getMenuInflater().inflate(R.menu.menu_main_debug, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        // Timber.d("Clicked on " + Integer.toString(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.action_debug:
                mPresenter.debugClicked();
                return true;
            case R.id.action_RIS:
                showRIS();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setStageDependentViewsVisibility() {

        int stage = mPresenter.whatStage();
        int target = mPresenter.whatTarget();
        String targetText = "";
        String targetSelected = "";

        // Set Pre-research questionnaire
        if ((stage == 4) || (mPresenter.isPreTestRun())) {
            findViewById(R.id.btnPreTest).setVisibility(View.GONE);
            findViewById(R.id.tvPreTestNotice).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btnPreTest).setVisibility(View.VISIBLE);
            findViewById(R.id.tvPreTestNotice).setVisibility(View.VISIBLE);
        }

        // Setting target set view for stage 4
        // Timber.d("Setting preset visibility for stage " + Integer.toString(stage));
        if (stage == 4) {
            findViewById(R.id.targetSetView).setVisibility(View.VISIBLE);
            if (target != -1) {
                // findViewById(R.id.tvTargetSelected).setVisibility(View.GONE);
            // } else {
                // findViewById(R.id.targetSetView).setVisibility(View.GONE);
                // findViewById(R.id.tvTargetSelected).setVisibility(View.VISIBLE);
                targetText = getResources().getString(R.string.tvTargetSelectedText);
                targetSelected = targetText; // + " " + Integer.toString(target) + " %";
                ((TextView) findViewById(R.id.textView10)).setText(targetSelected);
            }
        } else {
            // Timber.d("Hiding target setting");
            findViewById(R.id.targetSetView).setVisibility(View.GONE);
            findViewById(R.id.tvTargetSelected).setVisibility(View.GONE);
        }

        // Setting snooze button
        if (stage == 1 || stage == 5) {
            findViewById(R.id.btnSnooze).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btnSnooze).setVisibility(View.VISIBLE);
            findViewById(R.id.btnSnooze).setOnClickListener(v -> mPresenter.snoozeClicked(this));
        }

        if (stage == 6) {
            // Timber.d("Setting stage6 Toast setting visible");
            findViewById(R.id.stage6ToastSetView).setVisibility(View.VISIBLE);
            mStage6ToastSetView.setToastRight();
        } else {
            findViewById(R.id.stage6ToastSetView).setVisibility(View.GONE);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.setView(this);
        mPresenter.init();
        setStageDependentViewsVisibility();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
    public void showStage6View(Stage stage) {
        try {
            Intent i = new Intent(this, DebugStage6Activity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    @Override
    public void setStageText(String stage) {
        Button debugButton = (Button) findViewById(R.id.btnDebug);
        debugButton.setText("DEBUG - " + stage);
    }
     */

    public void setIntroText(int stage, int day) {
        String str;

        String dayString;
        String remainingTimeText;
        int stageLength = 0;

        // Set Intro text for the stage
        String[] introTexts = getResources().getStringArray((R.array.IntroTexts));
        str = introTexts[stage - 1];
        ((TextView) findViewById(R.id.tvIntro)).setText(str);

        // Then, if the stage is part of the research
        if (stage < 6) {
            // Figure out how long is each stage
            int[] lengthOfStage = getResources().getIntArray(R.array.eachStageLength);
            stageLength = lengthOfStage[stage - 1];

            remainingTimeText = getResources().getString(R.string.RemainingTimeText);
            int remainingTime;
            remainingTime = stageLength - day + 1;
            dayString = Integer.toString(remainingTime);
            ((TextView) findViewById(R.id.tvStageRemainingTime)).setText(dayString + " " + remainingTimeText);
            findViewById(R.id.tvStageRemainingTime).setVisibility(View.VISIBLE);
        } else {
            // Otherwise we are done, make it blank.
            findViewById(R.id.tvStageRemainingTime).setVisibility(View.GONE);
        }

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
