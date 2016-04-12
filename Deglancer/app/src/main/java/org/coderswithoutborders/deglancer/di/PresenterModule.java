package org.coderswithoutborders.deglancer.di;

import org.coderswithoutborders.deglancer.interactor.IInitialStartupInteractor;
import org.coderswithoutborders.deglancer.presenter.IMainActivityPresenter;
import org.coderswithoutborders.deglancer.presenter.MainActivityPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Renier on 2016/04/12.
 */
@Module
public class PresenterModule {
    @Singleton
    @Provides
    public IMainActivityPresenter providesMainActivityPresenter(IInitialStartupInteractor initialStartupInteractor) {
        return new MainActivityPresenter(initialStartupInteractor);
    }

}
