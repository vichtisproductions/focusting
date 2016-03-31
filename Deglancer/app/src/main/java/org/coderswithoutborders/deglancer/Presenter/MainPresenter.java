package org.coderswithoutborders.deglancer.presenter;

import android.os.Bundle;

import org.coderswithoutborders.deglancer.model.ScreenTime;
import org.coderswithoutborders.deglancer.view.MainActivity;

import nucleus.presenter.RxPresenter;

/**
 * Created by chris.teli on 3/21/2016.
 */
public class MainPresenter extends RxPresenter<MainActivity> {

    private ScreenTime screen_time;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    public void onTakeView(MainActivity view) {
        super.onTakeView(view);
    }

    @Override
    public void onDropView()
    {
        super.onDropView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

