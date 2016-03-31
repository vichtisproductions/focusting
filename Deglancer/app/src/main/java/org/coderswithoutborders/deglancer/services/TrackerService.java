package org.coderswithoutborders.deglancer.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.bus.events.ActionEvent;
import org.coderswithoutborders.deglancer.model.ActionReceiver;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

public class TrackerService extends Service {

    private static final String TAG = "TrackerService";

    @Inject
    ActionReceiver mActionReceiver;

    @Inject
    RxBus mBus;

    private CompositeSubscription mSubscriptions;


    @Override
    public void onCreate() {
        super.onCreate();

        mSubscriptions = new CompositeSubscription();

        MainApplication.from(getApplicationContext()).getGraph().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        register();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregister();

        super.onDestroy();
    }

    private void register() {
        if (mSubscriptions == null || mSubscriptions.isUnsubscribed())
            mSubscriptions = new CompositeSubscription();

        mActionReceiver.register();

        mSubscriptions.add(mBus.toObserverable().subscribe((event) -> {
            if (event instanceof  ActionEvent)
                handleAction((ActionEvent) event);
        }));
    }

    private void handleAction(ActionEvent action) {
        if (action.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i(TAG, "Screen Event - " + "LOCKED");
        } else if (action.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i(TAG, "Screen Event - " + "SCREEN_ON");
        } else if (action.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.i(TAG, "Screen Event - " + "USER_PRESENT");
        }  else if (action.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Log.i(TAG, "Screen Event - " + "POWER_CONNECTED");
        } else if (action.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Log.i(TAG, "Screen Event - " + "POWER_DISCONNECTED");
        }
    }

    private void unregister() {
        mActionReceiver.unregister();

        if (!mSubscriptions.isUnsubscribed())
            mSubscriptions.unsubscribe();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
