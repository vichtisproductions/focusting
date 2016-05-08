package org.coderswithoutborders.deglancer.bus.events;

import org.coderswithoutborders.deglancer.model.Stage;

/**
 * Created by Renier on 2016/05/08.
 */
public class StageSelectEvent {
    private Stage mStage;

    public StageSelectEvent(Stage stage) {
        this.mStage = stage;
    }

    public Stage getStage() {
        return mStage;
    }
}
