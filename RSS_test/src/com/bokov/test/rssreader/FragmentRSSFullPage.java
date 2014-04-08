package com.bokov.test.rssreader;

/**
 * Created by vladimirbokov on 27/03/14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A fragment representing a single article in WebView from RSS feed.
 * The fragment is either contained in a {@link ActivityListRSS}
 * for tablets or a {@link ActivityRSSFullPage} in case of handsets.
 */
public class FragmentRSSFullPage extends Fragment {

    public static final String ARTICLE_URL = "article_url";

    private String articleURL;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentRSSFullPage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARTICLE_URL)) {
            // retrieve address
            articleURL = getArguments().getString("article_url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rss_full_page,
                container, false);

        // setting up WebView
        WebView webView = (WebView) rootView.findViewById(R.id.article_webpage);
        webView.setWebViewClient(new WebViewClient());
        // needed to display article on tablets in mobile version of web site
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like " +
                "Mac OS X; en) AppleWebKit/420+ " +
                "(KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        if (articleURL != null) {
            webView.loadUrl(articleURL);
        }

        return rootView;
    }
}

