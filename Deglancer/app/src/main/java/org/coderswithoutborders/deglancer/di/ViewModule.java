package org.coderswithoutborders.deglancer.di;

import android.content.Context;

import org.coderswithoutborders.deglancer.func_debug.stage1.DebugStage1View;
import org.coderswithoutborders.deglancer.func_debug.stage2.DebugStage2View;
import org.coderswithoutborders.deglancer.func_debug.stage3.DebugStage3View;
import org.coderswithoutborders.deglancer.func_debug.stage4.DebugStage4View;
import org.coderswithoutborders.deglancer.func_debug.stage5.DebugStage5View;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Renier on 2016/05/06.
 */
@Module
public class ViewModule {
    @Singleton
    @Provides
    public DebugStage1View providesDebugStage1View(Context context) {
        return new DebugStage1View(context);
    }

    @Singleton
    @Provides
    public DebugStage2View providesDebugStage2View(Context context) {
        return new DebugStage2View(context);
    }

    @Singleton
    @Provides
    public DebugStage3View providesDebugStage3View(Context context) {
        return new DebugStage3View(context);
    }

    @Singleton
    @Provides
    public DebugStage4View providesDebugStage4View(Context context) {
        return new DebugStage4View(context);
    }

    @Singleton
    @Provides
    public DebugStage5View providesDebugStage5View(Context context) {
        return new DebugStage5View(context);
    }
}
