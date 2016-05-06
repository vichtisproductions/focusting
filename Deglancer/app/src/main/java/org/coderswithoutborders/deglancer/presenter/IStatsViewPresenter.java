package org.coderswithoutborders.deglancer.presenter;

import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.view.IStatsView;

/**
 * Created by Renier on 2016/05/06.
 */
public interface IStatsViewPresenter {
    void setView(IStatsView view);
    void clearView();
    void onAttached();
    void onDetached();
    void refresh();
    void setStage(Stage stage);
}
