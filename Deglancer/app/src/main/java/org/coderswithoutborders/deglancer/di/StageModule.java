package org.coderswithoutborders.deglancer.di;

import android.content.Context;

import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.stagehandlers.IStageHandler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage1Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage2Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage3Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage4Handler;
import org.coderswithoutborders.deglancer.stagehandlers.Stage5Handler;

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
    public Stage4Handler provideStage4Handler(Context context, IDatabaseInteractor databaseInteractor) {
        return new Stage4Handler(context, databaseInteractor);
    }

    @Singleton
    @Provides
    public Stage5Handler provideStage5Handler(Context context, IDatabaseInteractor databaseInteractor) {
        return new Stage5Handler(context, databaseInteractor);
    }
}
