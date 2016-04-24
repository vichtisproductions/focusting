package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;

import org.coderswithoutborders.deglancer.model.Stage;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public class StageInteractor implements IStageInteractor {
    private Context mContext;
    private IInitialStartupInteractor mInitialStartupInteractor;

    public StageInteractor(Context context, IInitialStartupInteractor initialStartupInteractor) {
        mContext = context;
        mInitialStartupInteractor = initialStartupInteractor;
    }

    @Override
    public Observable<Stage> getCurrentStage() {
        return Observable.defer(() -> Observable.create(subscriber -> {
            Calendar c = Calendar.getInstance(Locale.getDefault());

            DateTime initialStartTime = new DateTime(mInitialStartupInteractor.getInitialStartTime());
            int days = Days.daysBetween(initialStartTime, new DateTime()).getDays();

            int stageNr = -1;
            int day = -1;

            if (days < 7) {
                //Stage1
                stageNr = 1;
                day = days + 1;
            } else if (days < 14) {
                //Stage2
                stageNr = 2;
                day = days - 7 + 1;
            } else if (days < 21) {
                //Stage3
                stageNr = 3;
                day = days - 14 + 1;
            } else if (days < 28) {
                //Stage4
                stageNr = 4;
                day = days - 21 + 1;
            }

            Stage stage = new Stage(
                    stageNr,
                    day,
                    c.get(Calendar.HOUR_OF_DAY)
            );

            subscriber.onNext(stage);
            subscriber.onCompleted();
        }));
    }

    @Override
    public void advanceCurrentStage() {
        Observable.create(subscriber -> {




            subscriber.onNext(null);
            subscriber.onCompleted();
        })
                .subscribe(result -> {

                }, error -> {

                });
    }
}
