package org.coderswithoutborders.deglancer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.func_debug.stage1.DebugStage1View;
import org.coderswithoutborders.deglancer.func_debug.stage2.DebugStage2View;
import org.coderswithoutborders.deglancer.func_debug.stage3.DebugStage3View;
import org.coderswithoutborders.deglancer.func_debug.stage4.DebugStage4View;
import org.coderswithoutborders.deglancer.func_debug.stage5.DebugStage5View;
import org.coderswithoutborders.deglancer.model.Stage;
import org.coderswithoutborders.deglancer.presenter.IMainActivityPresenter;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.services.TrackerService;

import javax.inject.Inject;

/**
 * Created by chris.teli on 3/20/2016.
 */
public class MainActivity extends AppCompatActivity implements IMainActivityView {

    @Inject
    IMainActivityPresenter mPresenter;

    @Inject
    DebugStage1View mDebugStage1View;

    @Inject
    DebugStage2View mDebugStage2View;

    @Inject
    DebugStage3View mDebugStage3View;

    @Inject
    DebugStage4View mDebugStage4View;

    @Inject
    DebugStage5View mDebugStage5View;

    private RelativeLayout mainContainer;
    private RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        MainApplication.from(this).getGraph().inject(this);

        Intent i = new Intent(getApplicationContext(), TrackerService.class);
        getApplicationContext().startService(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.setView(this);
        mPresenter.init();
    }

    @Override
    protected void onPause() {
        mPresenter.clearView();

        super.onPause();
    }


    @Override
    public void showStage1View(Stage stage) {
        mDebugStage1View.setStage(stage);

        if (mDebugStage1View.getParent() == null) {
            mainContainer.addView(mDebugStage1View, layoutParams);
        }
    }

    @Override
    public void showStage2View(Stage stage) {
        mDebugStage2View.setStage(stage);

        if (mDebugStage2View.getParent() == null) {
            mainContainer.addView(mDebugStage2View, layoutParams);
        }
    }

    @Override
    public void showStage3View(Stage stage) {
        mDebugStage3View.setStage(stage);

        if (mDebugStage3View.getParent() == null) {
            mainContainer.addView(mDebugStage3View, layoutParams);
        }
    }

    @Override
    public void showStage4View(Stage stage) {
        mDebugStage4View.setStage(stage);

        if (mDebugStage4View.getParent() == null) {
            mainContainer.addView(mDebugStage4View, layoutParams);
        }
    }

    @Override
    public void showStage5View(Stage stage) {
        mDebugStage5View.setStage(stage);

        if (mDebugStage5View.getParent() == null) {
            mainContainer.addView(mDebugStage5View, layoutParams);
        }
    }

    @Override
    public void removeAllViewsFromMain() {
        mainContainer.removeAllViews();
    }
}
