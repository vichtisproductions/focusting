package org.coderswithoutborders.deglancer.view;

import org.coderswithoutborders.deglancer.model.Stage;

/**
 * Created by Renier on 2016/04/12.
 */
public interface IMainActivityView {
    void showStage1View(Stage stage);
    void showStage2View(Stage stage);
    void showStage3View(Stage stage);
    void showStage4View(Stage stage);
    void showStage5View(Stage stage);
    void showStage6View(Stage stage);
    // void setStageText(String stage);
    void setIntroText(int stage, int day);
    void showPreTest();
    void showRIS();
}
