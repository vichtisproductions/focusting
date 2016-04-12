package org.coderswithoutborders.deglancer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.MainApplication;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    /**
     * @param message
     */
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
