package org.coderswithoutborders.deglancer.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;

/**
 * Created by Lauri Palokangas on 22.6.2016.
 */
public class ResearchInformationSheet extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.research_information_sheet);

        // MainApplication.from(getApplicationContext()).getGraph().inject(this);

    }

}
