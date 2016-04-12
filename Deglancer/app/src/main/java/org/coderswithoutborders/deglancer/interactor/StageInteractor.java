package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public class StageInteractor implements IStageInteractor {
    private Context mContext;

    public StageInteractor(Context context) {
        mContext = context;
    }

    @Override
    public Observable<Integer> getCurrentStage() {
        return Observable.defer(() -> Observable.create(subscriber -> {
            int currentStage = -1;




            subscriber.onNext(currentStage);
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
