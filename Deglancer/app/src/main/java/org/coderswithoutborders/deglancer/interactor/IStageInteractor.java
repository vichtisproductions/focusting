package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IStageInteractor {
    Observable<Integer> getCurrentStage();
    void advanceCurrentStage();
}
