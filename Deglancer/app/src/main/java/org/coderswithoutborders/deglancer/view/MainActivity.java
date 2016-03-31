package org.coderswithoutborders.deglancer.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.presenter.MainPresenter;
import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.services.TrackerService;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusActivity;

/**
 * Created by chris.teli on 3/20/2016.
 */
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusActivity<MainPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO - Remove later, just for testing
        Intent i = new Intent(getApplicationContext(), TrackerService.class);
        getApplicationContext().startService(i);
    }

    /**
     * @param message
     */
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
