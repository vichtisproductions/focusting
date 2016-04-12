package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.interactor.IInitialStartupInteractor;
import org.coderswithoutborders.deglancer.view.IMainActivityView;

/**
 * Created by chris.teli on 3/21/2016.
 */
public class MainActivityPresenter implements IMainActivityPresenter {
    private IInitialStartupInteractor mInitialStartupInteractor;

    private IMainActivityView mView;

    public MainActivityPresenter(IInitialStartupInteractor initialStartupInteractor) {
        mInitialStartupInteractor = initialStartupInteractor;
    }

    @Override
    public void setView(IMainActivityView view) {
        mView = view;
    }

    @Override
    public void clearView() {
        mView = null;
    }

    @Override
    public void init() {
        mInitialStartupInteractor.captureInitialDataIfNotCaptured();
    }
}

