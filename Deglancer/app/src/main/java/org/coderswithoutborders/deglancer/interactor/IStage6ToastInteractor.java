package org.coderswithoutborders.deglancer.interactor;

import rx.Observable;

/**
 * Created by Renier on 2016/05/14.
 */
public interface IStage6ToastInteractor {
    Observable<Integer> getStage6Toast(int stage);
    int getStage6ToastSynchronous(int toast);
    void setStage6Toast(int stage, int target);
}
