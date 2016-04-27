package org.coderswithoutborders.deglancer;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import org.acra.*;
import org.acra.annotation.*;

import org.coderswithoutborders.deglancer.di.DIGraph;

/**
 * Created by Renier on 2016/03/29.
 */

@ReportsCrashes(
        formUri = "hhttps://collector.tracepot.com/15c51f2e"
)
public class MainApplication extends Application {
    private DIGraph mGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        mGraph = DIGraph.Initializer.init(this);
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
