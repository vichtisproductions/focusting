package org.vichtisproductions.focusting.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.vichtisproductions.focusting.BuildConfig;
import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.func_debug.stage1.DebugStage1Activity;
import org.vichtisproductions.focusting.func_debug.stage2.DebugStage2Activity;
import org.vichtisproductions.focusting.func_debug.stage3.DebugStage3Activity;
import org.vichtisproductions.focusting.func_debug.stage4.DebugStage4Activity;
import org.vichtisproductions.focusting.func_debug.stage5.DebugStage5Activity;
import org.vichtisproductions.focusting.func_debug.stage6.DebugStage6Activity;
import org.vichtisproductions.focusting.func_debug.view.TargetSetView;
import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.presenter.IMainActivityPresenter;
import org.vichtisproductions.focusting.pretest.PreTestActivity;
import org.vichtisproductions.focusting.services.TrackerService;
import org.vichtisproductions.focusting.utils.CalendarUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by chris.teli on 3/20/2016.
 */
public class MainActivity extends AppCompatActivity implements IMainActivityView,
        PermissionRationaleDialog.Callback {

    @Inject
    IMainActivityPresenter mPresenter;

    private static final String TAG = "Focusting.Main";
    private static final int PERMISSION_REQUEST_READ_CALENDAR = 100;
    private static final String PERMISSION_RATIONALE_DIALOG_TAG = "permission_rationale";

    private TargetSetView mTargetSetView;
    private Stage6ToastSetView mStage6ToastSetView;

    private FirebaseAnalytics mFirebaseAnalytics;

    private Button button;

    private Toolbar mToolbar;

    // TODO - FIX Prompting permissions
    // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALENDAR},1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO - Review what the application view should be and rewrite it
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

        // Fix start time if not fixed
        mPresenter.adjustStartTime(getApplicationContext());

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
        ifHuaweiAlert();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }

    private void shareIt() {
        // TODO - Is there a need for app sharing functionality?
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
        // TODO - THIS NEEDS THOROUGH TESTING
        int stage = mPresenter.whatStage();
        int target = mPresenter.whatTarget();
        String targetText;
        String targetSelected;

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
        if (checkForCalendarPermission()) {
            mPresenter.init();
        }
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Init presenter after permission is given
                    mPresenter.init();
                    Timber.d("Permissions given");
                } else {
                    Timber.d("No permissions given");
                }
                return;
            }
        }
    }

    private boolean checkForCalendarPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALENDAR)) {
                // Show rationale dialog
                new PermissionRationaleDialog().show(getSupportFragmentManager(), PERMISSION_RATIONALE_DIALOG_TAG);
            } else {
                requestPermissions();
            }
            return false;
        }

        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CALENDAR},
                PERMISSION_REQUEST_READ_CALENDAR);
    }

    @Override
    public void onDialogButtonClick(int which) {
        // Rationale dialog closed, ask for permission again
        requestPermissions();
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
        int stageLength;

        // Set Intro text for the stage
        String[] introTexts = getResources().getStringArray((R.array.IntroTexts));
        str = introTexts[stage - 1];
        ((TextView) findViewById(R.id.tvIntro)).setText(str);

        // Then, if the stage is part of the research
        if (stage < 3) {
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


    // HUAWEI STUFF BELOW THIS

    private void ifHuaweiAlert() {
        final SharedPreferences settings = getSharedPreferences("ProtectedApps", MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(this);
                dontShowAgain.setText(R.string.text_huawei_dont_show_again);
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.title_huawei_message))
                        .setMessage(getString(R.string.text_huawei_message))
                        .setView(dontShowAgain)
                        .setPositiveButton(getString(R.string.button_huawei_message), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                huaweiProtectedApps();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void huaweiProtectedApps() {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial();
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private String getUserSerial() {
        //noinspection ResourceType
        Object userManager = getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }

}
