package org.vichtisproductions.focusting.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import org.vichtisproductions.focusting.services.TrackerService;

/**
 * Created by Renier on 2016/03/29.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, TrackerService.class);
        ContextCompat.startForegroundService(context, i);
    }
}
