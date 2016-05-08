package org.coderswithoutborders.deglancer.func_debug.stage2;

import org.coderswithoutborders.deglancer.model.Stage;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IDebugStage2View {
    void finishActivity();
    void moveToStage3View();
    void moveToStage1View();
    void setStage(Stage stage);
    void setTitleStage(String stage);
}
