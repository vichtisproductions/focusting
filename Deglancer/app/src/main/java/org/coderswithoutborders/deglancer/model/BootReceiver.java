package org.coderswithoutborders.deglancer.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.coderswithoutborders.deglancer.services.TrackerService;

/**
 * Created by Renier on 2016/03/29.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, TrackerService.class);
        context.startService(i);
    }
}
