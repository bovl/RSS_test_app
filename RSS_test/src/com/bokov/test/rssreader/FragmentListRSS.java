package com.bokov.test.rssreader;

/**
 * Created by vladimirbokov on 27/03/14.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A ListFragment represents a list of RSS entries. List items
 * can be given state 'activated'. Due to this we can see the
 * item currently being viewed in a {@link FragmentRSSFullPage}.
 */
public class FragmentListRSS extends ListFragment {

    // Array list for list view
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String,String>>();

    RSSParser rssParser = new RSSParser();

    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESСRIPTION = "description";
    private static String TAG_PUB_DATE = "pubDate";

    // Progress Dialog
    private ProgressDialog progressDialog;

    private static String rssUrl;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks callbacks = sDummyCallbacks;

    // currently active position
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate
     * the fragment (e.g. upon screen orientation changes).
     */
    public FragmentListRSS() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        // TODO: handle possibility of empty extras
        if (extras != null) {
            rssUrl = extras.getString("RSS_URL");
            // new loadRSSFeed().execute(rssUrl);
        } else if (rssUrl == null) {
            rssUrl = "http://feeds.feedburner.com/TechCrunch/";
        }

        // calling a background thread will loads recent articles
        // from RSS
        new loadRSSFeedItems().execute(rssUrl);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // restore the previously saved activated item position
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // activities containing this fragment MUST implement its callbacks
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // reset the active callbacks interface to the dummy implementation
        callbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position, id);

        // notify activity via callbacks that item has been selected
        callbacks.onItemSelected(rssItemList.get(position).get(TAG_LINK));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

    /**
     * Background Async Task to get RSS Feed Items data from URL
     * */
    class loadRSSFeedItems extends AsyncTask<String, String, String> {

        /**
         * Before starting background load show progress dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(
                    getActivity());
            progressDialog.setMessage("Loading recent articles...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        /**
         * get all recent articles and set adapter for the ListView
         * */
        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);

            // looping through each entry
            for(RSSItem item : rssItems){
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_TITLE, item.get_title());
                map.put(TAG_LINK, item.get_link());
                map.put(TAG_PUB_DATE, item.get_pubDate()); // If you want parse the date
                String description = item.get_description();
                // trim description to 100 signs
                if(description.length() > 100){
                    description = description.substring(0, 97) + "..";
                }
                map.put(TAG_DESСRIPTION, description);
                // adding HashList to ArrayList
                rssItemList.add(map);
            }

            // updating UI from Work thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    // setting up adapter for ListView
                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(),
                            rssItemList, R.layout.rss_item_list_row,
                            new String[]{TAG_TITLE, TAG_PUB_DATE, TAG_DESСRIPTION},
                            new int[]{R.id.title, R.id.pub_date, R.id.link});
                    // updating ListView
                    setListAdapter(adapter);
                }
            });
            return null;
        }

        /**
         * Dismiss the progress dialog after task is done
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            progressDialog.dismiss();
        }
    }
}

