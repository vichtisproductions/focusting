package org.coderswithoutborders.deglancer.interactor;

import org.coderswithoutborders.deglancer.model.Averages;
import org.coderswithoutborders.deglancer.model.ScreenAction;

/**
 * Created by Renier on 2016/04/27.
 */
public interface IDatabaseInteractor {
    long getUnlockCountForStage(int stage, int day, int hour);
    long getUnlockCountForStageDay(int stage, int day);


    long getTotalSFTForStage(int stage, int day, int hour);
    long getTotalSFTForStageDay(int stage, int day);
    double getAverageSFTForStage(int stage, int day, int hour);
    double getAverageSFTForStageDay(int stage, int day);


    long getTotalSOTForStage(int stage, int day, int hour);
    long getTotalSOTForStageDay(int stage, int day);
    double getAverageSOTForStage(int stage, int day, int hour);
    double getAverageSOTForStageDay(int stage, int day);



    void commitAverages(Averages averages);
    ScreenAction getLastScreenAction();
    ScreenAction getLastScreenActionOfType(String action);
    void commitScreenAction(ScreenAction action);
}
