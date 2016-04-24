package org.coderswithoutborders.deglancer.interactor;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IInitialStartupInteractor {
    void captureInitialDataIfNotCaptured();
    long getInitialStartTime();
}
