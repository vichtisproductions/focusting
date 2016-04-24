package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;

import org.coderswithoutborders.deglancer.model.Stage;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IStageInteractor {
    Observable<Stage> getCurrentStage();
    void advanceCurrentStage();
}
