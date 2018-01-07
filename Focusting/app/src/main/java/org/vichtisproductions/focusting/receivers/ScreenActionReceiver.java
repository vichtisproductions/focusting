package org.vichtisproductions.focusting.receivers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.bus.events.ActionEvent;

import javax.inject.Inject;

/**
 * Created by chris.teli on 3/21/2016.
 */
public class ScreenActionReceiver extends android.content.BroadcastReceiver {

    private Context mContext;
    private boolean mIsRegistered = false;

    @Inject
    RxBus mBus;

    public ScreenActionReceiver(Context context) {
        mContext = context;

        MainApplication.from(context).getGraph().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String strAction = intent.getAction();

        mBus.post(new ActionEvent(strAction));
    }

    public void register() {
        IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);
        theFilter.addAction(Intent.ACTION_USER_PRESENT);
        theFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        theFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        mContext.registerReceiver(this, theFilter);

        mIsRegistered = true;
    }

    public void unregister() {
        mContext.unregisterReceiver(this);
        mIsRegistered = false;
    }

    public boolean isRegistered() {
        return mIsRegistered;
    }
}
