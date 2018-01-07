package org.vichtisproductions.focusting.di;

import android.content.Context;

import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.interactor.ITargetInteractor;
import org.vichtisproductions.focusting.stagehandlers.IStageHandler;
import org.vichtisproductions.focusting.stagehandlers.Stage1Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage2Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage3Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage4Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage5Handler;
import org.vichtisproductions.focusting.stagehandlers.Stage6Handler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by Renier on 2016/04/27.
 */
@Module
public class StageModule {
    @Singleton
    @Provides
    public Stage1Handler provideStage1Handler(Context context, IDatabaseInteractor databaseInteractor) {
        return new Stage1Handler(context, databaseInteractor);
    }

    @Singleton
    @Provides
    public Stage2Handler provideStage2Handler(Context context, IDatabaseInteractor databaseInteractor) {
        return new Stage2Handler(context, databaseInteractor);
    }

    @Singleton
    @Provides
    public Stage3Handler provideStage3Handler(Context context, IDatabaseInteractor databaseInteractor) {
        return new Stage3Handler(context, databaseInteractor);
    }

    @Singleton
    @Provides
    public Stage4Handler provideStage4Handler(Context context, IDatabaseInteractor databaseInteractor, ITargetInteractor targetInteractor) {
        return new Stage4Handler(context, databaseInteractor, targetInteractor);
    }

    @Singleton
    @Provides
    public Stage5Handler provideStage5Handler(Context context, IDatabaseInteractor databaseInteractor) {
        return new Stage5Handler(context, databaseInteractor);
    }

    @Singleton
    @Provides
    public Stage6Handler provideStage6Handler(Context context, IDatabaseInteractor databaseInteractor) {
        return new Stage6Handler(context, databaseInteractor);
    }
}
