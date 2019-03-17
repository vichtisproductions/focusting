package org.vichtisproductions.focusting.func_debug;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import timber.log.Timber;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface Callback {
        void onDateSelected(long dateInMillis);
    }

    private static final String STARTUP_TIME_KEY = "initial_startup_time";

    private final Calendar calendar = Calendar.getInstance();
    private Callback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            this.callback = (Callback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            long startupTime = args.getLong(STARTUP_TIME_KEY, System.currentTimeMillis());
            calendar.setTimeInMillis(startupTime);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Timber.d("Year: %d, month: %d, day: %d", year, month, day);
        if (callback != null) {
            calendar.set(year, month, day);
            callback.onDateSelected(calendar.getTimeInMillis());
        }
    }

    public static DatePickerFragment newInstance(long initialStartupTime) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putLong(STARTUP_TIME_KEY, initialStartupTime);
        fragment.setArguments(args);
        return fragment;
    }
}
