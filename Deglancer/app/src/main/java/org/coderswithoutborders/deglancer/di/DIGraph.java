package org.coderswithoutborders.deglancer.di;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.func_debug.stage1.DebugStage1Activity;
import org.coderswithoutborders.deglancer.func_debug.stage2.DebugStage2Activity;
import org.coderswithoutborders.deglancer.func_debug.stage3.DebugStage3Activity;
import org.coderswithoutborders.deglancer.func_debug.stage4.DebugStage4Activity;
import org.coderswithoutborders.deglancer.func_debug.stage5.DebugStage5Activity;
import org.coderswithoutborders.deglancer.receivers.ScreenActionReceiver;
import org.coderswithoutborders.deglancer.services.TrackerService;
import org.coderswithoutborders.deglancer.view.AveragesSetView;
import org.coderswithoutborders.deglancer.view.MainActivity;
import org.coderswithoutborders.deglancer.view.StatsView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Renier on 2016/03/29.
 */
@Component(modules = { ApplicationModule.class, ReceiverModule.class, BusModule.class, DataModule.class, InteractorModule.class, PresenterModule.class, StageModule.class})
@Singleton
public interface DIGraph {
    void inject(TrackerService service);
    void inject(ScreenActionReceiver receiver);
    void inject(MainActivity activity);
    void inject(StatsView view);
    void inject(DebugStage1Activity view);
    void inject(DebugStage2Activity view);
    void inject(DebugStage3Activity view);
    void inject(DebugStage4Activity view);
    void inject(DebugStage5Activity view);
    void inject(AveragesSetView view);

    final class Initializer {
        public static DIGraph init(MainApplication application) {
            return DaggerDIGraph
                    .builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }
    }
}
