package org.coderswithoutborders.deglancer.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.R;

/**
 * Created by chris.teli on 5/4/2016.
 */
public class ToastUtils {
    public static void showToast(Context mContext,
                                 long actionDuration,
                                 long unlockCount,
                                 long totalSOTTime) {
        String lastSleepString = TimeUtils.getTimeStringFromMillis(actionDuration, true, false, true);
        String unlockString = mContext.getString(R.string.toast_unlocks_today_label) + " " + unlockCount;
        String totalSOTTimeString = TimeUtils.getTimeStringFromMillis(totalSOTTime, true, false, true);

        LayoutInflater inflater = LayoutInflater.from(mContext.getApplicationContext());
        View layout = inflater.inflate(R.layout.custom_toast, new LinearLayout(mContext), false)
                .findViewById(R.id.toast_layout_root);

        TextView textLastSleep = (TextView) layout.findViewById(R.id.text_last_sleep_time);
        textLastSleep.setText(lastSleepString);

        TextView textUnlockCount = (TextView) layout.findViewById(R.id.text_unlock_count);
        textUnlockCount.setText(lastSleepString);

        TextView textTotalSOTTime = (TextView) layout.findViewById(R.id.text_screen_on_time);
        textTotalSOTTime.setText(lastSleepString);

        // Create custom toast and display it
        /*Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();*/
    }
}
