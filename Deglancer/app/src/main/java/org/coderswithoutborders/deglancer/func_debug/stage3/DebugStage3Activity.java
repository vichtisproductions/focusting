package org.coderswithoutborders.deglancer.func_debug.stage3;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.func_debug.stage2.IDebugStage2Presenter;
import org.coderswithoutborders.deglancer.func_debug.stage2.IDebugStage2View;
import org.coderswithoutborders.deglancer.view.StatsView;

import javax.inject.Inject;

/**
 * Created by Renier on 2016/05/06.
 */
public class DebugStage3Activity extends Activity implements IDebugStage2View {

    @Inject
    IDebugStage2Presenter mPresenter;

    private Button btnAdvance;
    private Button btnBack;
    private StatsView mStatsView;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.debug_stage2);

            MainApplication.from(getApplicationContext()).getGraph().inject(this);

            mStatsView = (StatsView)findViewById(R.id.statsView);
            mStatsView.setStagePickerState(false, true, true);

            btnAdvance = (Button) findViewById(R.id.btnAdvance);
            btnAdvance.setOnClickListener(buttonClickListener);

            btnBack = (Button) findViewById(R.id.btnBack);
            btnBack.setOnClickListener(buttonClickListener);

    }

    View.OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnAdvance) {
            mPresenter.advanceStageClicked();
        } else if (v.getId() == R.id.btnBack) {
            mPresenter.previousStageClicked();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (mPresenter != null) {
            mPresenter.setView(this);
            mPresenter.onAttached();
        }
    }

    @Override
    protected void onPause() {
        if (mPresenter != null) {
            mPresenter.onDetached();
            mPresenter.clearView();
        }

        super.onPause();
    }
}
