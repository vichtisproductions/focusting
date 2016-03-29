package org.coderswithoutborders.deglancer.Presenter;

import android.content.Intent;
import android.os.Bundle;

import org.coderswithoutborders.deglancer.model.ActionReceiver;
import org.coderswithoutborders.deglancer.model.ScreenEvent;
import org.coderswithoutborders.deglancer.model.ScreenTime;
import org.coderswithoutborders.deglancer.view.MainActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import nucleus.presenter.RxPresenter;

/**
 * Created by chris.teli on 3/21/2016.
 */
public class MainPresenter extends RxPresenter<MainActivity> {

    private ActionReceiver actionReceiver;
    private ScreenTime screen_time;
    private EventBus bus = EventBus.getDefault();

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        // Initialize  BroadcastReceiver
        actionReceiver = new ActionReceiver();
        // Register as a subscriber
        bus.register(this);
    }

    @Override
    public void onTakeView(MainActivity view) {
        super.onTakeView(view);
        actionReceiver.register(view.getApplicationContext());
    }

    @Override
    public void onDropView()
    {
        super.onDropView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister
        bus.unregister(this);
    }

    @Subscribe
    public void onEvent(ScreenEvent strAction){
        // Handle different states of the event
        if (strAction.getMessage().equals(Intent.ACTION_SCREEN_OFF)) {
            System.out.println("Screen off " + "LOCKED");
        } else if (strAction.getMessage().equals(Intent.ACTION_SCREEN_ON)) {
            System.out.println("Screen off " + "UNLOCKED");
        } else if (strAction.getMessage().equals(Intent.ACTION_POWER_CONNECTED)) {
            System.out.println("Screen off " + "Connected");
        } else if (strAction.getMessage().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            System.out.println("Screen off " + "Disconnected");
        }
    }
}

