package org.vichtisproductions.focusting.func_debug.stage4;

import org.vichtisproductions.focusting.model.Stage;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage4View {
    void finishActivity();
    void moveToStage5View();
    void moveToStage3View();
    void setStage(Stage stage);
    void setTitleStage(String stage);
}
