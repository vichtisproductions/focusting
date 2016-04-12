package org.coderswithoutborders.deglancer.di;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.receivers.ScreenActionReceiver;
import org.coderswithoutborders.deglancer.services.TrackerService;
import org.coderswithoutborders.deglancer.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Renier on 2016/03/29.
 */
@Component(modules = { ApplicationModule.class, ReceiverModule.class, BusModule.class, DataModule.class, InteractorModule.class, PresenterModule.class})
@Singleton
public interface DIGraph {
    void inject(TrackerService service);
    void inject(ScreenActionReceiver receiver);
    void inject(MainActivity activity);


    final class Initializer {
        public static DIGraph init(MainApplication application) {
            return DaggerDIGraph
                    .builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }
    }
}
