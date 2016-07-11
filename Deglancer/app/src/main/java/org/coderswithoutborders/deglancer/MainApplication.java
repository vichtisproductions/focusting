package org.coderswithoutborders.deglancer;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import org.acra.*;
import org.acra.annotation.*;

import org.coderswithoutborders.deglancer.di.DIGraph;
import org.coderswithoutborders.deglancer.utils.AwesomeRadioButton.utils.TypefaceProvider;

import timber.log.Timber;

/**
 * Created by Renier on 2016/03/29.
 */

@ReportsCrashes(
        formUri = "https://collector.tracepot.com/15c51f2e"
)
public class MainApplication extends Application {
    private DIGraph mGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        // Awesome radio button
        TypefaceProvider.registerDefaultIconSets();

        mGraph = DIGraph.Initializer.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        //This is used so that the app theme applies to views inflated from app context rather than activity/fragment context
        getApplicationContext().setTheme(R.style.AppTheme);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        ACRA.init(this);
    }

    public DIGraph getGraph() {
        return mGraph;
    }

    public static MainApplication from(@NonNull Context context) {
        return (MainApplication) context.getApplicationContext();
    }
}
