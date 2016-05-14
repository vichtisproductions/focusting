package org.coderswithoutborders.deglancer.di;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.func_debug.presenter.IStageSelectViewPresenter;
import org.coderswithoutborders.deglancer.func_debug.presenter.ITargetSetViewPresenter;
import org.coderswithoutborders.deglancer.func_debug.presenter.StageSelectViewPresenter;
import org.coderswithoutborders.deglancer.func_debug.presenter.TargetSetViewPresenter;
import org.coderswithoutborders.deglancer.func_debug.stage1.DebugStage1Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage1.IDebugStage1Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage2.DebugStage2Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage2.IDebugStage2Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage3.DebugStage3Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage3.IDebugStage3Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage4.DebugStage4Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage4.IDebugStage4Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage5.DebugStage5Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage5.IDebugStage5Presenter;
import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.interactor.IInitialStartupInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.func_debug.presenter.AveragesSetViewPresenter;
import org.coderswithoutborders.deglancer.func_debug.presenter.IAveragesSetViewPresenter;
import org.coderswithoutborders.deglancer.interactor.ITargetInteractor;
import org.coderswithoutborders.deglancer.presenter.IMainActivityPresenter;
import org.coderswithoutborders.deglancer.func_debug.presenter.IStatsViewPresenter;
import org.coderswithoutborders.deglancer.presenter.MainActivityPresenter;
import org.coderswithoutborders.deglancer.func_debug.presenter.StatsViewPresenter;

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
    public IMainActivityPresenter providesMainActivityPresenter(IInitialStartupInteractor initialStartupInteractor, IStageInteractor stageInteractor, RxBus bus) {
        return new MainActivityPresenter(initialStartupInteractor, stageInteractor, bus);
    }


    @Provides
    public IStatsViewPresenter providesStatsViewPresenter(IDatabaseInteractor databaseInteractor, IStageInteractor stageInteractor, RxBus bus) {
        return new StatsViewPresenter(databaseInteractor, stageInteractor, bus);
    }

    @Singleton
    @Provides
    public IDebugStage1Presenter providesDebugStage1Presenter(IStageInteractor stageInteractor) {
        return new DebugStage1Presenter(stageInteractor);
    }

    @Singleton
    @Provides
    public IDebugStage2Presenter providesDebugStage2Presenter(IStageInteractor stageInteractor) {
        return new DebugStage2Presenter(stageInteractor);
    }

    @Singleton
    @Provides
    public IDebugStage3Presenter providesDebugStage3Presenter(IStageInteractor stageInteractor) {
        return new DebugStage3Presenter(stageInteractor);
    }

    @Singleton
    @Provides
    public IDebugStage4Presenter providesDebugStage4Presenter(IStageInteractor stageInteractor) {
        return new DebugStage4Presenter(stageInteractor);
    }

    @Singleton
    @Provides
    public IDebugStage5Presenter providesDebugStage5Presenter(IStageInteractor stageInteractor) {
        return new DebugStage5Presenter(stageInteractor);
    }


    @Provides
    public IAveragesSetViewPresenter providesAveragesSetViewPresenter(IDatabaseInteractor databaseInteractor, RxBus bus) {
        return new AveragesSetViewPresenter(databaseInteractor, bus);
    }

    @Provides
    public IStageSelectViewPresenter providesStageSelectViewPresenter(RxBus bus) {
        return new StageSelectViewPresenter(bus);
    }

    @Provides
    public ITargetSetViewPresenter providesTargetSetViewPresenter(ITargetInteractor targetInteractor, RxBus bus) {
        return new TargetSetViewPresenter(targetInteractor, bus);
    }

}
