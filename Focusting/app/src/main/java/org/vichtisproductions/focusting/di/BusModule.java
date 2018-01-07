package org.vichtisproductions.focusting.di;

import org.vichtisproductions.focusting.bus.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Renier on 2016/03/29.
 */
@Module
@Singleton
public class BusModule {
    @Singleton
    @Provides
    RxBus providesBus() { return new RxBus(); }
}
