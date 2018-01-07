package org.vichtisproductions.focusting.func_debug.stage5;

import org.vichtisproductions.focusting.model.Stage;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage5View {
    void finishActivity();
    void moveToStage4View();
    void moveToStage6View();
    void setStage(Stage stage);
    void setTitleStage(String stage);
}
