package com.bokov.test.rssreader;

/**
 * Created by vladimirbokov on 27/03/14.
 */

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

/**
 * This activity representing an RSS article in WebView, only called on
 * handset devices. On tablets RSS article presented side-by-side with
 * RSS list in {@link ActivityListRSS}.
 * */
public class ActivityRSSFullPage extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: handle portrait and landscape orientation for all devices
        // lock portrait orientation for handsets and
        // landscape orientation for tablets
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }  else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        setContentView(R.layout.activity_rss_full_page);
        // show the Up button in the action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // creating the detail fragment and add it to the activity
            // using a fragment transaction
            Bundle arguments = new Bundle();
            arguments.putString(FragmentRSSFullPage.ARTICLE_URL, getIntent()
                    .getStringExtra(FragmentRSSFullPage.ARTICLE_URL));
            FragmentRSSFullPage fragment = new FragmentRSSFullPage();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rss_full_page_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // handle "return" button in action bar, allow user
                // to navigate up one level in the application
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

