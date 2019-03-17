package org.vichtisproductions.focusting.interactor;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IInitialStartupInteractor {
    void captureInitialDataIfNotCaptured();
    long getInitialStartTime();
    int getGroupNumber();
    void overrideInitialStartTime(long newStartTime);
    boolean overrideGroupNumber(int groupNumber);
}
