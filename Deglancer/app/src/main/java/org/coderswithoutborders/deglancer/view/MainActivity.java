package org.coderswithoutborders.deglancer.view;

import android.content.Intent;
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

        addRISButton();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.setView(this);
        mPresenter.init();
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
        // String stage;
        // stage = Integer.toString(stage);
        String dayString;
        String remainingTimeText;
        int stageLength=0;

        switch (stage) {
            /*
            case "":
                str = getResources().getString(R.string.IntroTextPreTest);
                ((TextView)findViewById(R.id.tvIntro)).setText(str);
                break;
             */
            case 1:
                str = getResources().getString(R.string.IntroTextStage1);
                stageLength = getResources().getInteger(R.integer.lengthStage1);
                ((TextView)findViewById(R.id.tvIntro)).setText(str);
                break;
            case 2:
                str = getResources().getString(R.string.IntroTextStage2);
                stageLength = getResources().getInteger(R.integer.lengthStage2);
                ((TextView)findViewById(R.id.tvIntro)).setText(str);
                break;
            case 3:
                str = getResources().getString(R.string.IntroTextStage3);
                stageLength = getResources().getInteger(R.integer.lengthStage3);
                ((TextView)findViewById(R.id.tvIntro)).setText(str);
                break;
            case 4:
                str = getResources().getString(R.string.IntroTextStage4);
                stageLength = getResources().getInteger(R.integer.lengthStage4);
                ((TextView)findViewById(R.id.tvIntro)).setText(str);
                break;
            case 5:
                str = getResources().getString(R.string.IntroTextStage5);
                stageLength = getResources().getInteger(R.integer.lengthStage5);
                ((TextView)findViewById(R.id.tvIntro)).setText(str);
                break;
        }
        remainingTimeText = getResources().getString(R.string.RemainingTimeText);
        int remainingTime;
        remainingTime = stageLength - day;
        dayString = Integer.toString(remainingTime);
        ((TextView)findViewById(R.id.tvStageRemainingTime)).setText(dayString + " " + remainingTimeText);

    }

    public void addRISButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.TextResInfoSheet);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, ResearchInformationSheet.class);
                startActivity(intent);

            }

        });

    }
}
