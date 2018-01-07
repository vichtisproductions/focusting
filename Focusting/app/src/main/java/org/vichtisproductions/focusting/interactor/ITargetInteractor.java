package org.vichtisproductions.focusting.interactor;

import rx.Observable;

/**
 * Created by Renier on 2016/05/14.
 */
public interface ITargetInteractor {
    Observable<Integer> getTargetForStage(int stage);
    int getTargetForStageSynchronous(int stage);
    int getTargetInFactForStageSynchronous(int stage);
    void setTargetForStage(int stage, int target);
}
