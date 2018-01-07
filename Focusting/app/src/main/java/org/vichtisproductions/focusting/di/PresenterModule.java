package org.vichtisproductions.focusting.di;

import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.func_debug.presenter.IStageSelectViewPresenter;
import org.vichtisproductions.focusting.func_debug.presenter.ITargetSetViewPresenter;
import org.vichtisproductions.focusting.presenter.IStage6ToastSetViewPresenter;
import org.vichtisproductions.focusting.func_debug.presenter.StageSelectViewPresenter;
import org.vichtisproductions.focusting.func_debug.presenter.TargetSetViewPresenter;
import org.vichtisproductions.focusting.func_debug.stage1.DebugStage1Presenter;
import org.vichtisproductions.focusting.func_debug.stage1.IDebugStage1Presenter;
import org.vichtisproductions.focusting.func_debug.stage2.DebugStage2Presenter;
import org.vichtisproductions.focusting.func_debug.stage2.IDebugStage2Presenter;
import org.vichtisproductions.focusting.func_debug.stage3.DebugStage3Presenter;
import org.vichtisproductions.focusting.func_debug.stage3.IDebugStage3Presenter;
import org.vichtisproductions.focusting.func_debug.stage4.DebugStage4Presenter;
import org.vichtisproductions.focusting.func_debug.stage4.IDebugStage4Presenter;
import org.vichtisproductions.focusting.func_debug.stage5.DebugStage5Presenter;
import org.vichtisproductions.focusting.func_debug.stage5.IDebugStage5Presenter;
import org.vichtisproductions.focusting.func_debug.stage6.DebugStage6Presenter;
import org.vichtisproductions.focusting.func_debug.stage6.IDebugStage6Presenter;
import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.interactor.IInitialStartupInteractor;
import org.vichtisproductions.focusting.interactor.IPreTestInteractor;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.func_debug.presenter.AveragesSetViewPresenter;
import org.vichtisproductions.focusting.func_debug.presenter.IAveragesSetViewPresenter;
import org.vichtisproductions.focusting.interactor.ITargetInteractor;
import org.vichtisproductions.focusting.interactor.IStage6ToastInteractor;
import org.vichtisproductions.focusting.presenter.IMainActivityPresenter;
import org.vichtisproductions.focusting.func_debug.presenter.IStatsViewPresenter;
import org.vichtisproductions.focusting.presenter.IStage6ToastSetViewPresenter;
import org.vichtisproductions.focusting.presenter.MainActivityPresenter;
import org.vichtisproductions.focusting.func_debug.presenter.StatsViewPresenter;
import org.vichtisproductions.focusting.presenter.Stage6ToastSetViewPresenter;
import org.vichtisproductions.focusting.pretest.IPreTestPresenter;
import org.vichtisproductions.focusting.pretest.PreTestPresenter;

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
    public IMainActivityPresenter providesMainActivityPresenter(IInitialStartupInteractor initialStartupInteractor, IStageInteractor stageInteractor, IDatabaseInteractor databaseInteractor, RxBus bus) {
        return new MainActivityPresenter(initialStartupInteractor, stageInteractor, databaseInteractor, bus);
    }


    @Provides
    public IStatsViewPresenter providesStatsViewPresenter(IDatabaseInteractor databaseInteractor, IStageInteractor stageInteractor, RxBus bus) {
        return new StatsViewPresenter(databaseInteractor, stageInteractor, bus);
    }

    @Singleton
    @Provides
    public IDebugStage1Presenter providesDebugStage1Presenter(IStageInteractor stageInteractor, IDatabaseInteractor databaseInteractor) {
        return new DebugStage1Presenter(stageInteractor, databaseInteractor);
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


    @Singleton
    @Provides
    public IDebugStage6Presenter providesDebugStage6Presenter(IStageInteractor stageInteractor) {
        return new DebugStage6Presenter(stageInteractor);
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
    public ITargetSetViewPresenter providesTargetSetViewPresenter(ITargetInteractor targetInteractor, RxBus bus, IStageInteractor stageInteractor) {
        return new TargetSetViewPresenter(targetInteractor, bus, stageInteractor);
    }

    @Provides
    public IStage6ToastSetViewPresenter providesStage6ToastSetViewPresenter(IStage6ToastInteractor stage6ToastInteractor, RxBus bus, IStageInteractor stageInteractor) {
        return new Stage6ToastSetViewPresenter(stage6ToastInteractor, bus, stageInteractor);
    }

    @Provides
    public IPreTestPresenter providesPreTestPresenter(IPreTestInteractor preTestInteractor, RxBus bus) {
        return new PreTestPresenter(preTestInteractor, bus);
    }

}
