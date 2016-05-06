package org.coderswithoutborders.deglancer.di;

import android.content.Context;

import com.firebase.client.Firebase;

import org.coderswithoutborders.deglancer.bus.RxBus;
import org.coderswithoutborders.deglancer.interactor.DatabaseInteractor;
import org.coderswithoutborders.deglancer.interactor.IDatabaseInteractor;
import org.coderswithoutborders.deglancer.interactor.IInitialStartupInteractor;
import org.coderswithoutborders.deglancer.interactor.IScreenActionInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.interactor.IUserInteractor;
import org.coderswithoutborders.deglancer.interactor.InitialStartupInteractor;
import org.coderswithoutborders.deglancer.interactor.ScreenActionInteractor;
import org.coderswithoutborders.deglancer.interactor.StageInteractor;
import org.coderswithoutborders.deglancer.interactor.UserInteractor;
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
 * Created by Renier on 2016/04/04.
 */
@Module
public class InteractorModule {
    @Singleton
    @Provides
    IScreenActionInteractor provideScreenActionInteractor(Context context, Firebase firebaseClient, IStageInteractor stageInteractor, Realm realm, IUserInteractor userInteractor, IDatabaseInteractor databaseInteractor    ) {
        return new ScreenActionInteractor(context, firebaseClient, stageInteractor, realm, userInteractor, databaseInteractor);
    }

    @Singleton
    @Provides
    IStageInteractor provideStageInteractor(Context context, RxBus bus, IInitialStartupInteractor initialStartupInteractor, Stage1Handler stage1Handler, Stage2Handler stage2Handler, Stage3Handler stage3Handler, Stage4Handler stage4Handler, Stage5Handler stage5Handler) {
        return new StageInteractor(context, bus, initialStartupInteractor, stage1Handler, stage2Handler, stage3Handler, stage4Handler, stage5Handler);
    }

    @Singleton
    @Provides
    IInitialStartupInteractor providesInitialStartupInteractor(Context context, Firebase firebaseClient, IUserInteractor userInteractor) {
        return new InitialStartupInteractor(context, firebaseClient, userInteractor);
    }

    @Singleton
    @Provides
    IUserInteractor providesUserInteractor(Context context, Firebase firebaseClient) {
        return new UserInteractor(context, firebaseClient);
    }

    @Singleton
    @Provides
    IDatabaseInteractor providesDatabaseInteractor(Context context, Realm realm) {
        return new DatabaseInteractor(context, realm);
    }
}
