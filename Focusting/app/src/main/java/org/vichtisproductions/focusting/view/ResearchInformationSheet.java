package org.vichtisproductions.focusting.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.api.model.StringList;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.interactor.IUserInteractor;

import java.util.UUID;

import timber.log.Timber;

/**
 * Created by Lauri Palokangas on 22.6.2016.
 */
public class ResearchInformationSheet extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // TODO - Rewrite Research Information Sheet to match with the new research
        // MOST LIKELY ALL CHANGES CAN BE DONE TO THE RESOURCE FILES
        super.onCreate(savedInstanceState);
        setContentView(R.layout.research_information_sheet);

        SharedPreferences prefs = this.getSharedPreferences("UserInteractorSP", Context.MODE_PRIVATE);
        String deviceIDText = prefs.getString("InstanceId", "");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.labelRISTitle));

        ((TextView) findViewById(R.id.tvDeviceIDText)).setText(deviceIDText);

        TextView view = (TextView)findViewById(R.id.tvCreditsText);
        String HtmlText = getString(R.string.tvCreditsText);
        Spanned SpannedText = Html.fromHtml(HtmlText);
        view.setText(SpannedText);
        view.setClickable(true);
        view.setMovementMethod(LinkMovementMethod.getInstance());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle fbAnalyticsBundle = new Bundle();
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "research_info_sheet");
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Research Information Sheet");
        fbAnalyticsBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Research Information");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, fbAnalyticsBundle);

    }

}
