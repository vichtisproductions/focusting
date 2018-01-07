package org.vichtisproductions.focusting.model;

/**
 * Created by Renier on 2016/05/23.
 */
public class TriState {
    public enum State {
        Better,
        Same,
        Worse
    }

    private State mState;

    public TriState(State state) {
        this.mState = state;
    }

    public State getState() {
        return this.mState;
    }
}
