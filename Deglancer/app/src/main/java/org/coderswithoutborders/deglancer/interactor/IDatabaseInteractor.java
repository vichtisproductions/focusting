package org.coderswithoutborders.deglancer.interactor;

import org.coderswithoutborders.deglancer.model.Averages;
import org.coderswithoutborders.deglancer.model.ScreenAction;

/**
 * Created by Renier on 2016/04/27.
 */
public interface IDatabaseInteractor {
    long getUnlockCountForStage(int stage, int day, int hour);
    double getAverageSFTForStage(int stage, int day, int hour);
    double getAverageSOTForStage(int stage, int day, int hour);
    void commitAverages(Averages averages);
    ScreenAction getLastScreenAction();
    ScreenAction getLastScreenActionOfType(String action);
    void commitScreenAction(ScreenAction action);
    long getUnlockCountForStageDay(int stage, int day);
    long getTotalSOTForStageDay(int stage, int day);
}
