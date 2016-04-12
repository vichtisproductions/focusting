package org.coderswithoutborders.deglancer.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.firebase.client.Firebase;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.bus.events.ActionEvent;
import org.coderswithoutborders.deglancer.interactor.IScreenActionInteractor;
import org.coderswithoutborders.deglancer.receivers.ScreenActionReceiver;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

public class TrackerService extends Service {

    private static final String TAG = "TrackerService";

    @Inject
    ScreenActionReceiver mScreenActionReceiver;

    @Inject
    RxBus mBus;

    @Inject
    IScreenActionInteractor mScreenActionInteractor;

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

        mScreenActionReceiver.register();

        mSubscriptions.add(mBus.toObserverable().subscribe((event) -> {
            if (event instanceof  ActionEvent)
                handleAction((ActionEvent) event);
        }));
    }

    private void handleAction(ActionEvent action) {
        mScreenActionInteractor.handleScreenAction(action);
    }

    private void unregister() {
        mScreenActionReceiver.unregister();

        if (!mSubscriptions.isUnsubscribed())
            mSubscriptions.unsubscribe();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
