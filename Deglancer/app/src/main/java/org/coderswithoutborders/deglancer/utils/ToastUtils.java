package org.coderswithoutborders.deglancer.utils;

import android.content.Context;
import android.media.Image;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.model.TriState;
import org.coderswithoutborders.deglancer.stagehandlers.Keystore;

import timber.log.Timber;

/**
 * Created by chris.teli on 5/4/2016.
 */
public class ToastUtils {
    public static void showToast(Context mContext,
                                 long actionDuration,
                                 long unlockCount,
                                 long totalSOTTime) {
        String lastSleepString = TimeUtils.getTimeStringFromMillis(actionDuration, true, false, true, false);
        String unlockString = String.valueOf(unlockCount);
        String totalSOTTimeString = TimeUtils.getTimeStringFromMillis(totalSOTTime, true, true, true, true);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, new LinearLayout(mContext), false)
                .findViewById(R.id.toast_layout_root);

        TextView textLastSleep = (TextView) layout.findViewById(R.id.text_last_sleep_time);
        textLastSleep.setText(lastSleepString);

        TextView textUnlockCount = (TextView) layout.findViewById(R.id.text_unlock_count);
        textUnlockCount.setText(unlockString);

        TextView textTotalSOTTime = (TextView) layout.findViewById(R.id.text_screen_on_time);
        textTotalSOTTime.setText(totalSOTTimeString);

        // Create custom toast and display it
        if (System.currentTimeMillis() > getSnooze(mContext)) {
            Timber.d("Toasting!");
            Toast toast = new Toast(mContext);
            toast.setGravity(Gravity.BOTTOM, 0, 30);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            Timber.d("No toast - it's snoozed.");
        }
    }

    public static void showToast(Context mContext,
                                 long actionDuration,
                                 long unlockCount,
                                 long totalSOTTime,
                                 TriState unlockState,
                                 TriState sotState,
                                 TriState sftState,
                                 double unlockDiffPercentage,
                                 double sotDiffPercentage,
                                 double sftDiffPercentage) {

        String lastSleepString = TimeUtils.getTimeStringFromMillis(actionDuration, true, false, true, false);
        String unlockString = String.valueOf(unlockCount);
        String totalSOTTimeString = TimeUtils.getTimeStringFromMillis(totalSOTTime, true, true, true, true);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, new LinearLayout(mContext), false)
                .findViewById(R.id.toast_layout_root);

        TextView textLastSleep = (TextView) layout.findViewById(R.id.text_last_sleep_time);
        textLastSleep.setText(lastSleepString);

        TextView textUnlockCount = (TextView) layout.findViewById(R.id.text_unlock_count);
        textUnlockCount.setText(unlockString);

        TextView textTotalSOTTime = (TextView) layout.findViewById(R.id.text_screen_on_time);
        textTotalSOTTime.setText(totalSOTTimeString);

        ImageView unlockImage = (ImageView) layout.findViewById(R.id.unlock_image);
        ImageView sotImage = (ImageView) layout.findViewById(R.id.screen_on_image);
        ImageView sftImage = (ImageView) layout.findViewById(R.id.last_sleep_image);

        if (unlockState.getState() == TriState.State.Better) {
            unlockImage.setImageResource(R.drawable.ic_image_better);
        } else if (unlockState.getState() == TriState.State.Worse) {
            unlockImage.setImageResource(R.drawable.ic_image_worse);
        } else {
            unlockImage.setImageResource(R.drawable.ic_image_same);
        }

        if (sotState.getState() == TriState.State.Better) {
            sotImage.setImageResource(R.drawable.ic_image_better);
        } else if (sotState.getState() == TriState.State.Worse) {
            sotImage.setImageResource(R.drawable.ic_image_worse);
        } else {
            sotImage.setImageResource(R.drawable.ic_image_same);
        }

        if (sftState.getState() == TriState.State.Better) {
            sftImage.setImageResource(R.drawable.ic_image_better);
        } else if (sftState.getState() == TriState.State.Worse) {
            sftImage.setImageResource(R.drawable.ic_image_worse);
        } else {
            sftImage.setImageResource(R.drawable.ic_image_same);
        }

        // Create custom toast and display it
        if (System.currentTimeMillis() > getSnooze(mContext)) {
            Timber.d("Toasting!");
            Toast toast = new Toast(mContext);
            toast.setGravity(Gravity.BOTTOM, 0, 30);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            Timber.d("No toast - it's snoozed.");
        }

    }

    public static void showToast(Context mContext,
                                 long actionDuration,
                                 long unlockCount,
                                 long totalSOTTime,
                                 TriState unlockState,
                                 TriState sotState,
                                 TriState sftState,
                                 double unlockDiffPercentage,
                                 double sotDiffPercentage,
                                 double sftDiffPercentage,
                                 int targetPercentage) {
        String lastSleepString = TimeUtils.getTimeStringFromMillis(actionDuration, true, false, true, false);
        String unlockString = String.valueOf(unlockCount);
        String totalSOTTimeString = TimeUtils.getTimeStringFromMillis(totalSOTTime, true, true, true, true);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, new LinearLayout(mContext), false)
                .findViewById(R.id.toast_layout_root);

        TextView textLastSleep = (TextView) layout.findViewById(R.id.text_last_sleep_time);
        textLastSleep.setText(lastSleepString);

        TextView textUnlockCount = (TextView) layout.findViewById(R.id.text_unlock_count);
        textUnlockCount.setText(unlockString);


        TextView textTotalSOTTime = (TextView) layout.findViewById(R.id.text_screen_on_time);
        textTotalSOTTime.setText(totalSOTTimeString);

        ImageView unlockImage = (ImageView) layout.findViewById(R.id.unlock_image);
        ImageView sotImage = (ImageView) layout.findViewById(R.id.screen_on_image);
        ImageView sftImage = (ImageView) layout.findViewById(R.id.last_sleep_image);

        if (unlockState.getState() == TriState.State.Better) {
            unlockImage.setImageResource(R.drawable.ic_image_better);
        } else if (unlockState.getState() == TriState.State.Worse) {
            unlockImage.setImageResource(R.drawable.ic_image_worse);
        } else {
            unlockImage.setImageResource(R.drawable.ic_image_same);
        }

        if (sotState.getState() == TriState.State.Better) {
            sotImage.setImageResource(R.drawable.ic_image_better);
        } else if (sotState.getState() == TriState.State.Worse) {
            sotImage.setImageResource(R.drawable.ic_image_worse);
        } else {
            sotImage.setImageResource(R.drawable.ic_image_same);
        }

        if (sftState.getState() == TriState.State.Better) {
            sftImage.setImageResource(R.drawable.ic_image_better);
        } else if (sftState.getState() == TriState.State.Worse) {
            sftImage.setImageResource(R.drawable.ic_image_worse);
        } else {
            sftImage.setImageResource(R.drawable.ic_image_same);
        }


        // Create custom toast and display it
        if (System.currentTimeMillis() > getSnooze(mContext)) {
            Timber.d("Toasting!");
            Toast toast = new Toast(mContext);
            toast.setGravity(Gravity.BOTTOM, 0, 30);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            Timber.d("No toast - it's snoozed.");
        }

    }

    public static void showToastStage2(Context mContext,
                                       long actionDuration,
                                       long unlockCount,
                                       long totalSOTTime) {
        String lastSleepString = TimeUtils.getTimeStringFromMillis(actionDuration, true, false, true, false);
        String unlockString = String.valueOf(unlockCount);
        String totalSOTTimeString = TimeUtils.getTimeStringFromMillis(totalSOTTime, true, true, true, true);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, new LinearLayout(mContext), false)
                .findViewById(R.id.toast_layout_root);

        TextView textLastSleep = (TextView) layout.findViewById(R.id.text_last_sleep_time);
        textLastSleep.setText(lastSleepString);

        TextView textUnlockCount = (TextView) layout.findViewById(R.id.text_unlock_count);
        textUnlockCount.setText(unlockString);

        TextView textTotalSOTTime = (TextView) layout.findViewById(R.id.text_screen_on_time);
        textTotalSOTTime.setText(totalSOTTimeString);

        // Create custom toast and display it

        if (System.currentTimeMillis() > getSnooze(mContext)) {
            Timber.d("Toasting!");
            Toast toast = new Toast(mContext);
            toast.setGravity(Gravity.BOTTOM, 0, 30);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            Timber.d("No toast - it's snoozed.");
        }
    }

    public static void setSnooze(Context mContext) {
        Keystore store;
        store = Keystore.getInstance(mContext);//Creates or Gets our key pairs.  You MUST have access to current context!
        long snoozeEndsAt = System.currentTimeMillis() + 7200000L;
        Timber.d("Time now: " + System.currentTimeMillis() + ", snooze ends at " + Long.toString(snoozeEndsAt));
        store.putLong("snoozeEndsAt", snoozeEndsAt);
    }

    public static long getSnooze(Context mContext) {
        Timber.d("Time now: " + System.currentTimeMillis() + ", snooze ends at " + Long.toString(Keystore.getInstance(mContext).getLong("snoozeEndsAt")));
        return Keystore.getInstance(mContext).getLong("snoozeEndsAt");
    }
}
