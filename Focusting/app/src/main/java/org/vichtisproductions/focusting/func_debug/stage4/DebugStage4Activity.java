package org.vichtisproductions.focusting.func_debug.stage4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.func_debug.stage3.DebugStage3Activity;
import org.vichtisproductions.focusting.func_debug.stage5.DebugStage5Activity;
import org.vichtisproductions.focusting.func_debug.view.AveragesSetView;
import org.vichtisproductions.focusting.func_debug.view.StageSelectView;
import org.vichtisproductions.focusting.func_debug.view.StatsView;
import org.vichtisproductions.focusting.func_debug.view.TargetSetView;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.view.MainActivity;

// Notification
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage4Activity extends AppCompatActivity implements IDebugStage4View {

    @Inject
    IDebugStage4Presenter mPresenter;

    private Button btnAdvance;
    private Button btnBack;
    private StatsView mStatsView;
    private StageSelectView mStageSelectView;
    private AveragesSetView mAvgSetView;
    private TargetSetView mTargetSetView;

    // Notification
    private NotificationManager notifyMgr=null;
    private static final int NOTIFY_ME_ID=31337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.debug_stage4);

        MainApplication.from(getApplicationContext()).getGraph().inject(this);

        mStatsView = (StatsView)findViewById(R.id.statsView);

        mStageSelectView = (StageSelectView) findViewById(R.id.stageSelectView);
        mStageSelectView.setStagePickerState(false, true, true);

        btnAdvance = (Button) findViewById(R.id.btnAdvance);
        btnAdvance.setOnClickListener(buttonClickListener);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(buttonClickListener);

        mAvgSetView = (AveragesSetView) findViewById(R.id.averagesSetView);
        mAvgSetView.setStage(new Stage(3, 1, 1));

        mTargetSetView = (TargetSetView) findViewById(R.id.targetSetView);
        mTargetSetView.setStage(new Stage(4, 1, 1));

        // See if you can invoke a notification here -- Lapa 23.6.2016
        /*

        notifyMgr=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyObj =
                new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Focusting")
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setContentText("Select your goal for this week");

        notifyMgr.notify(NOTIFY_ME_ID, notifyObj.build());
        */

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
    public void moveToStage3View() {
        Intent i = new Intent(this, DebugStage3Activity.class);
        startActivity(i);
    }

    @Override
    public void moveToStage5View() {
        Intent i = new Intent(this, DebugStage5Activity.class);
        startActivity(i);
    }

    @Override
    public void setTitleStage(String stage) {
        setTitle(String.format(getApplicationContext().getString(R.string.activity_debug_stage_activity_title_from_code), stage));
    }
}
