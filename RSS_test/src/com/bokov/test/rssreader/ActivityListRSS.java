package com.bokov.test.rssreader;

/**
 * Created by vladimirbokov on 26/03/14.
 */

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

/**
 * This activity represents a list of RSS entries. This activity has
 * different looks for handset and tablet. In case of phone, the
 * activity presents a list of items, which when touched, leads to a
 * {@link ActivityRSSFullPage} showing RSS entry using WebView.
 * If case of tablet, the activity shows the list of RSS entries and
 * RSS entry full page side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of RSS entries
 * is a {@link FragmentListRSS} and the RSS full page is a
 * {@link FragmentRSSFullPage}.
 */
public class ActivityListRSS extends FragmentActivity implements
        FragmentListRSS.Callbacks {

    // differentiates phones and tablets
    private boolean isTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rss);

        // TODO: handle portrait and landscape orientation for all devices
        // lock portrait orientation for handsets and
        // landscape orientation for tablets
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        if (findViewById(R.id.rss_full_page_container) != null) {
            // The full page container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). Activity should be in tablet(two-pane)
            // mode if this view present
            isTwoPaneMode = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((FragmentListRSS) getSupportFragmentManager()
                    .findFragmentById(R.id.rss_list))
                    .setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link FragmentListRSS.Callbacks} indicating
     * that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (isTwoPaneMode) {
            // in two-pane mode, show RSS page in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction
            Bundle arguments = new Bundle();
            arguments.putString(FragmentRSSFullPage.ARTICLE_URL, id);
            FragmentRSSFullPage fragment = new FragmentRSSFullPage();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rss_full_page_container, fragment).commit();
        } else {
            // In case of handset, start new activity for selected item ID
            Intent rssFullPageIntent = new Intent(this, ActivityRSSFullPage.class);
            rssFullPageIntent.putExtra(FragmentRSSFullPage.ARTICLE_URL, id);
            startActivity(rssFullPageIntent);
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
