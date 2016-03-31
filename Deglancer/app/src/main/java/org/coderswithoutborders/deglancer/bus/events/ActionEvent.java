package org.coderswithoutborders.deglancer.bus.events;

/**
 * Created by Renier on 2016/03/29.
 */
public class ActionEvent {
    private String mAction;

    public ActionEvent(String action) {
        mAction = action;
    }

    public String getAction() {
        return mAction;
    }
}
