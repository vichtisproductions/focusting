package org.vichtisproductions.focusting.di;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.func_debug.EditSetupActivity;
import org.vichtisproductions.focusting.func_debug.stage1.DebugStage1Activity;
import org.vichtisproductions.focusting.func_debug.stage2.DebugStage2Activity;
import org.vichtisproductions.focusting.func_debug.stage3.DebugStage3Activity;
import org.vichtisproductions.focusting.func_debug.stage4.DebugStage4Activity;
import org.vichtisproductions.focusting.func_debug.stage5.DebugStage5Activity;
import org.vichtisproductions.focusting.func_debug.stage6.DebugStage6Activity;
import org.vichtisproductions.focusting.func_debug.view.StageSelectView;
import org.vichtisproductions.focusting.func_debug.view.TargetSetView;
import org.vichtisproductions.focusting.interactor.Stage6ToastInteractor;
import org.vichtisproductions.focusting.model.Stage6Toast;
import org.vichtisproductions.focusting.pretest.PreTestActivity;
import org.vichtisproductions.focusting.receivers.ScreenActionReceiver;
import org.vichtisproductions.focusting.services.TrackerService;
import org.vichtisproductions.focusting.func_debug.view.AveragesSetView;
import org.vichtisproductions.focusting.view.MainActivity;
import org.vichtisproductions.focusting.func_debug.view.StatsView;
import org.vichtisproductions.focusting.view.Stage6ToastSetView;
import org.vichtisproductions.focusting.view.UsernameDialog;

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
    void inject(DebugStage6Activity view);
    void inject(AveragesSetView view);
    void inject(StageSelectView view);
    void inject(TargetSetView view);
    void inject(Stage6ToastSetView view);
    void inject(PreTestActivity view);
    void inject(Stage6ToastInteractor interactor);
    void inject(UsernameDialog dialog);
    void inject(EditSetupActivity activity);

    final class Initializer {
        public static DIGraph init(MainApplication application) {
            return DaggerDIGraph
                    .builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }
    }
}
