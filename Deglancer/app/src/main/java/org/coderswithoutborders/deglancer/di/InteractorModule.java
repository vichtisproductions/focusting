package org.coderswithoutborders.deglancer.di;

import android.content.Context;

import com.firebase.client.Firebase;

import org.coderswithoutborders.deglancer.interactor.IInitialStartupInteractor;
import org.coderswithoutborders.deglancer.interactor.IScreenActionInteractor;
import org.coderswithoutborders.deglancer.interactor.IStageInteractor;
import org.coderswithoutborders.deglancer.interactor.IUserInteractor;
import org.coderswithoutborders.deglancer.interactor.InitialStartupInteractor;
import org.coderswithoutborders.deglancer.interactor.ScreenActionInteractor;
import org.coderswithoutborders.deglancer.interactor.StageInteractor;
import org.coderswithoutborders.deglancer.interactor.UserInteractor;

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
    IScreenActionInteractor provideScreenActionInteractor(Context context, Firebase firebaseClient, IStageInteractor stageInteractor, Realm realm, IUserInteractor userInteractor    ) {
        return new ScreenActionInteractor(context, firebaseClient, stageInteractor, realm, userInteractor);
    }

    @Singleton
    @Provides
    IStageInteractor provideStageInteractor(Context context, IInitialStartupInteractor initialStartupInteractor) {
        return new StageInteractor(context, initialStartupInteractor);
    }

    @Singleton
    @Provides
    IInitialStartupInteractor providesInitialStartupInteractor(Context context, Firebase firebaseClient, IUserInteractor userInteractor) {
        return new InitialStartupInteractor(context, firebaseClient, userInteractor);
    }

    @Singleton
    @Provides
    IUserInteractor providesUserInteractor(Context context) {
        return new UserInteractor(context);
    }
}
