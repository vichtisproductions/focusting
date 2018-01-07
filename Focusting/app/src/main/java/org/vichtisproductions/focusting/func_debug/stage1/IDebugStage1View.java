package org.vichtisproductions.focusting.func_debug.stage1;

import org.vichtisproductions.focusting.model.Stage;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage1View {
    void finishActivity();
    void moveToStage2View();
    void setStage(Stage stage);
    void setTitleStage(String stage);
}
