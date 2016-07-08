package org.coderswithoutborders.deglancer.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.coderswithoutborders.deglancer.MainApplication;
import org.coderswithoutborders.deglancer.R;

/**
 * Created by Lauri Palokangas on 22.6.2016.
 */
public class ResearchInformationSheet extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.research_information_sheet);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle fbAnalyticsBundle = new Bundle();
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "research_info_sheet");
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Research Information Sheet");
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Research Information");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, fbAnalyticsBundle);

    }

}
