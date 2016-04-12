package org.coderswithoutborders.deglancer.di;

import android.content.Context;

import org.coderswithoutborders.deglancer.receivers.ScreenActionReceiver;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Renier on 2016/03/29.
 */
@Module
public class ReceiverModule {

    @Provides
    ScreenActionReceiver provideActionReceiver(Context context) {
        return new ScreenActionReceiver(context);
    }

}
