package org.vichtisproductions.focusting.func_debug.stage3;

import org.vichtisproductions.focusting.model.Stage;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage3View {
    void finishActivity();
    void moveToStage4View();
    void moveToStage2View();
    void setStage(Stage stage);
    void setTitleStage(String stage);
}
