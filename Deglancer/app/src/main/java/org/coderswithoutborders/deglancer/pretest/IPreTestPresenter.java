package org.coderswithoutborders.deglancer.pretest;

import org.coderswithoutborders.deglancer.pretest.IPreTestView;
import org.coderswithoutborders.deglancer.model.Results;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IPreTestPresenter {
    void onAttached();
    void onDetached();
    void submitPreTestResults(String preTestQ1, String preTestQ2, String preTestQ3, String preTestQ4, String preTestQ5, String preTestQ6, String preTestQ7, String preTestQ8, String preTestQ9, String preTestQ10);
}
