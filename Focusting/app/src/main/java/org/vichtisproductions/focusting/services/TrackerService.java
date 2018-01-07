package org.vichtisproductions.focusting.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.bus.events.ActionEvent;
import org.vichtisproductions.focusting.interactor.IScreenActionInteractor;
import org.vichtisproductions.focusting.receivers.ScreenActionReceiver;
import org.vichtisproductions.focusting.view.MainActivity;

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
    public int onStartCommand(Intent intent, int flags, int startteId) {
        register();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.tracker_service_foreground_content_title_text))
                .setTicker(getString(R.string.tracker_service_foreground_ticker_text))
                .setContentText(getString(R.string.tracker_service_foreground_content_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        startForeground(1337, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregister();

        super.onDestroy();
    }

    private void register() {
        if (mSubscriptions == null || mSubscriptions.isUnsubscribed()) {
            mSubscriptions = new CompositeSubscription();
        } else if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
            mSubscriptions = new CompositeSubscription();
        }

        if (!mScreenActionReceiver.isRegistered())
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
