package org.coderswithoutborders.deglancer.interactor;

import android.content.Context;

import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.stagehandlers.IStageHandler;

import rx.Observable;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IStageInteractor {
    Observable<Stage> getCurrentStage();
    Stage getCurrentStageSynchronous();
    void goToNextStage();
    void goToPreviousStage();
    IStageHandler getCurrentStageHandler();
    void setStageAsCurrentStage(int stage, int day, int hour);
}
