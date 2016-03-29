package org.coderswithoutborders.deglancer.di;

import android.content.Context;

import org.coderswithoutborders.deglancer.MainApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Renier on 2016/03/29.
 */
@Module
public class ApplicationModule {
    private static MainApplication sApplication;

    public ApplicationModule(MainApplication application) {
        sApplication = application;
    }

    @Provides
    MainApplication providesApplication() {
        return sApplication;
    }

    @Provides
    Context provideApplicationContext() {
        return sApplication.getApplicationContext();
    }
}
