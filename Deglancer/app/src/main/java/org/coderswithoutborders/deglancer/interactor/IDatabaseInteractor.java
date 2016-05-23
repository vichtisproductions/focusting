package org.coderswithoutborders.deglancer.interactor;

import org.coderswithoutborders.deglancer.model.Averages;
import org.coderswithoutborders.deglancer.model.ScreenAction;
import org.coderswithoutborders.deglancer.model.Target;

/**
 * Created by Renier on 2016/04/27.
 */
public interface IDatabaseInteractor {
    long getUnlockCountForStage(int stage, int day, int hour);
    long getUnlockCountForStageDay(int stage, int day);

    long getUnlockCountForStageFromAverages(int stage, int day, int hour);
    long getUnlockCountForStageDayFromAverages(int stage, int day);


    long getTotalSFTForStage(int stage, int day, int hour);
    long getTotalSOTForStage(int stage, int day, int hour);

    long getTotalSOTForStageDay(int stage, int day);

    double getAverageSFTForStage(int stage, int day, int hour);
    double getAverageSOTForStage(int stage, int day, int hour);


    long getTotalSFTForStageFromAverages(int stage, int day, int hour);
    long getTotalSOTForStageFromAverages(int stage, int day, int hour);

    double getAverageSFTForStageFromAverages(int stage, int day, int hour);
    double getAverageSOTForStageFromAverages(int stage, int day, int hour);


    long getTotalSFTForStageDayFromAverages(int stage, int day);
    double getAverageSFTForStageDayFromAverages(int stage, int day);


    long getTotalSOTForStageDayFromAverages(int stage, int day);
    double getAverageSOTForStageDayFromAverages(int stage, int day);

    void clearEntriesForStage(int stage);
    void clearEntriesForStageDay(int stage, int day);
    void clearEntriesForStageHour(int stage, int day, int hour);

    void clearAveragesForStage(int stage);
    void clearAveragesForStageDay(int stage, int day);
    void clearAveragesForStageHour(int stage, int day, int hour);


    void commitAverages(Averages averages);
    ScreenAction getLastScreenAction();
    ScreenAction getLastScreenActionOfType(String action);
    void commitScreenAction(ScreenAction action);

    void commitTarget(Target target);
    Target getTargetForStage(int stage);


    long getSumOfUnlockCountForStageDayUpToHourFromAverages(int stage, int day, int hour);
    long getSumOfSOTForStageDayUpToHourFromAverages(int stage, int day, int hour);
    double getAverageSFTForStageDayUpToHourFromAverages(int stage, int day, int hour);
    long getSumOfUnlockCountForStageDayUpToHourFromActions(int stage, int day, int hour);
    long getSumOfSOTForStageDayUpToHourFromActions(int stage, int day, int hour);
    double getAverageSFTForStageDayUpToHourFromActions(int stage, int day, int hour);
}
