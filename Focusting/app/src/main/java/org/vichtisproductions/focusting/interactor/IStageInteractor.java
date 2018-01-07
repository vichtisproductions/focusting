package org.vichtisproductions.focusting.interactor;

import android.content.Context;

import org.vichtisproductions.focusting.model.Stage;
import org.vichtisproductions.focusting.stagehandlers.IStageHandler;

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
