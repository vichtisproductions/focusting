package org.vichtisproductions.focusting.func_debug;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.interactor.IInitialStartupInteractor;

import java.util.Calendar;

import javax.inject.Inject;

import timber.log.Timber;

public class EditSetupActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, DatePickerFragment.Callback {

    private static final String STARTUP_TIME_TAG = "startup_time";

    @Inject
    IInitialStartupInteractor startupInteractor;

    private Spinner groupSpinner;

    private int currentGroup = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_setup_activity);
        MainApplication.from(this).getGraph().inject(this);

        // Init group spinner
        groupSpinner = findViewById(R.id.edit_setup_group);
        groupSpinner.setOnItemSelectedListener(this);
        currentGroup = startupInteractor.getGroupNumber();
        groupSpinner.setSelection(currentGroup - 1);
    }

    public void onStartDateClick(View view) {
        DatePickerFragment fragment = DatePickerFragment.newInstance(startupInteractor.getInitialStartTime());
        fragment.show(getSupportFragmentManager(), STARTUP_TIME_TAG);
    }

    @Override
    public void onDateSelected(long dateInMillis) {
        if (dateInMillis != startupInteractor.getInitialStartTime()) {
            startupInteractor.overrideInitialStartTime(dateInMillis);
            restartApp();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Timber.d("Clicked %d", position);
        int newGroup = position + 1;
        if (startupInteractor.overrideGroupNumber(newGroup)) {
            // Restart app when new group was selected
            if (newGroup != currentGroup) {
                restartApp();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Timber.d("Nothing selected");
    }

    private void restartApp() {
        Runtime.getRuntime().exit(0);
    }
}
