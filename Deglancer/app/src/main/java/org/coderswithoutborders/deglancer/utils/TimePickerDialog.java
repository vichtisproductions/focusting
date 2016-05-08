package org.coderswithoutborders.deglancer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import org.coderswithoutborders.deglancer.R;

/**
 * Created by Renier on 2016/05/07.
 */
public class TimePickerDialog {
    public interface TimePickerEventListener {
        void onCancel();
        void onTimeSelected(long millis);
    }


    private Activity mActivity;

    private TimePickerEventListener mListener;

    private NumberPicker npHour;
    private NumberPicker npMinute;
    private NumberPicker npSecond;

    public TimePickerDialog(Activity activity) {
        mActivity = activity;
    }

    public void setTimePickerEventListener(TimePickerEventListener listener) {
        mListener = listener;
    }

    public void show() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View dialoglayout = inflater.inflate(R.layout.time_picker_dialog, null);

        npHour = (NumberPicker) dialoglayout.findViewById(R.id.npHour);
        npMinute = (NumberPicker) dialoglayout.findViewById(R.id.npMinute);
        npSecond = (NumberPicker) dialoglayout.findViewById(R.id.npSecond);

        npHour.setMinValue(0);
        npHour.setMaxValue(23);

        npMinute.setMinValue(0);
        npMinute.setMaxValue(59);

        npSecond.setMinValue(0);
        npSecond.setMaxValue(59);

        new AlertDialog
                .Builder(mActivity)
                .setCancelable(true)
                .setView(dialoglayout)
                .setPositiveButton(R.string.time_picker_okay_button_text, (dialog, which) -> {
                    if (mListener != null) {
                        int hours = npHour.getValue();
                        int minutes = npMinute.getValue();
                        int seconds = npSecond.getValue();

                        long duration = (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
                        mListener.onTimeSelected(duration);
                    }
                })
                .setNegativeButton(R.string.time_picker_cancel_button_text, (dialog, which) -> {
                    if (mListener != null) {
                        mListener.onCancel();
                    }
                })
                .show();
    }



}
