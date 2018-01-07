package org.vichtisproductions.focusting.di;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import org.vichtisproductions.focusting.bus.RxBus;
import org.vichtisproductions.focusting.interactor.DatabaseInteractor;
import org.vichtisproductions.focusting.interactor.IDatabaseInteractor;
import org.vichtisproductions.focusting.interactor.IInitialStartupInteractor;
import org.vichtisproductions.focusting.interactor.IPreTestInteractor;
import org.vichtisproductions.focusting.interactor.IScreenActionInteractor;
import org.vichtisproductions.focusting.interactor.IStageInteractor;
import org.vichtisproductions.focusting.interactor.ITargetInteractor;
import org.vichtisproductions.focusting.interactor.IStage6ToastInteractor;
import org.vichtisproductions.focusting.interactor.IUserInteractor;
import org.vichtisproductions.focusting.interactor.InitialStartupInteractor;
import org.vichtisproductions.focusting.interactor.PreTestInteractor;
import org.vichtisproductions.focusting.interactor.ScreenActionInteractor;
import org.vichtisproductions.focusting.interactor.Stage6ToastInteractor;
import org.vichtisproductions.focusting.interactor.StageInteractor;
import org.vichtisproductions.focusting.interactor.TargetInteractor;
import org.vichtisproductions.focusting.interactor.UserInteractor;
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
 * Created by Renier on 2016/04/04.
 */
@Module
public class InteractorModule {
    @Singleton
    @Provides
    IScreenActionInteractor provideScreenActionInteractor(Context context, DatabaseReference firebaseClient, IStageInteractor stageInteractor, Realm realm, IUserInteractor userInteractor, IDatabaseInteractor databaseInteractor    ) {
        return new ScreenActionInteractor(context, firebaseClient, stageInteractor, realm, userInteractor, databaseInteractor);
    }

    @Singleton
    @Provides
    IStageInteractor provideStageInteractor(Context context, RxBus bus, IInitialStartupInteractor initialStartupInteractor, Stage1Handler stage1Handler, Stage2Handler stage2Handler, Stage3Handler stage3Handler, Stage4Handler stage4Handler, Stage5Handler stage5Handler, Stage6Handler stage6Handler, IStage6ToastInteractor stage6ToastInteractor) {
        return new StageInteractor(context, bus, initialStartupInteractor, stage1Handler, stage2Handler, stage3Handler, stage4Handler, stage5Handler, stage6Handler, stage6ToastInteractor);
    }

    @Singleton
    @Provides
    IPreTestInteractor providePreTestInteractor(Context context, IDatabaseInteractor mDatabaseInteractor, RxBus mBus, DatabaseReference firebaseClient, IUserInteractor userInteractor) {
        return new PreTestInteractor(context, mDatabaseInteractor, mBus, firebaseClient, userInteractor);
    }

    @Singleton
    @Provides
    IInitialStartupInteractor providesInitialStartupInteractor(Context context, DatabaseReference firebaseClient, IUserInteractor userInteractor) {
        return new InitialStartupInteractor(context, firebaseClient, userInteractor);
    }

    @Singleton
    @Provides
    IUserInteractor providesUserInteractor(Context context, DatabaseReference firebaseClient) {
        return new UserInteractor(context, firebaseClient);
    }

    @Singleton
    @Provides
    IDatabaseInteractor providesDatabaseInteractor(Context context, Realm realm) {
        return new DatabaseInteractor(context, realm);
    }

    @Singleton
    @Provides
    ITargetInteractor providesTargetInteractor(IDatabaseInteractor databaseInteractor, RxBus bus, DatabaseReference firebaseClient, IUserInteractor userInteractor) {
        return new TargetInteractor(databaseInteractor, bus, firebaseClient, userInteractor);
    }

    @Singleton
    @Provides
    IStage6ToastInteractor providesStage6ToastInteractor(IDatabaseInteractor databaseInteractor, RxBus bus, DatabaseReference firebaseClient, IUserInteractor userInteractor) {
        return new Stage6ToastInteractor(databaseInteractor, bus, firebaseClient, userInteractor);
    }
}
