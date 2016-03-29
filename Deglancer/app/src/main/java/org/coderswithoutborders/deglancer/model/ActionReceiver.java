package org.coderswithoutborders.deglancer.model;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by chris.teli on 3/21/2016.
 */
public class ActionReceiver extends android.content.BroadcastReceiver {

    final public IntentFilter theFilter = new IntentFilter();
    private EventBus bus = EventBus.getDefault();

    public ActionReceiver() {
        /** System Defined Broadcast */
        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);
        theFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        theFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String strAction = intent.getAction();
        bus.post(new ScreenEvent(strAction));
    }

    public Intent register(Context context) {
        return context.registerReceiver(this,theFilter);
    }
};
