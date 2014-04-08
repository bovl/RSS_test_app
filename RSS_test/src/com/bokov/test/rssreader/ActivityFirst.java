package com.bokov.test.rssreader;

/**
 * Created by vladimirbokov on 27/03/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

/**
 * Entry point of RSS reader app. Uses predefined RSS addresses
 * for Lenta.ru, BBC & Techcrunch. Touching each logo leads to
 * the list of RSS entries.
 */
// TODO: use android annotations more extensively
//@EActivity(R.layout.activity_first)
public class ActivityFirst extends Activity {

//    ImageView ivLenta;
//    ImageView ivBBC;
//    ImageView ivTechcrunch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: handle portrait and landscape orientation for all devices
        // lock portrait orientation for handsets and
        // landscape orientation for tablets
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        setContentView(R.layout.activity_first);

//        ivLenta = (ImageView) findViewById(R.id.start_lenta);
//        ivBBC = (ImageView) findViewById(R.id.start_bbc);
//        ivTechcrunch  = (ImageView) findViewById(R.id.start_techcrunch);
    }

    // handle onClick method for each ImageView of this activity
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_lenta:
                Intent intent = new Intent(ActivityFirst.this, ActivityListRSS.class);
                intent.putExtra("RSS_URL", "http://lenta.ru/rss/");
                ActivityFirst.this.startActivity(intent);
                break;
            case R.id.start_bbc:
                intent = new Intent(ActivityFirst.this, ActivityListRSS.class);
                intent.putExtra("RSS_URL", "http://feeds.bbci.co.uk/news/rss.xml");
                ActivityFirst.this.startActivity(intent);
                break;
            case R.id.start_techcrunch:
                intent = new Intent(ActivityFirst.this, ActivityListRSS.class);
                intent.putExtra("RSS_URL", "http://feeds.feedburner.com/TechCrunch/");
                ActivityFirst.this.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
