package com.bokov.test.rssreader;

/**
 * Created by std_bokov on 26.03.2014.
 */

/**
 * Class RSSItem represents <item> elements in RSS
 */
public class RSSItem {

    // All <item> node name
    String _title;
    String _link;
    String _description;
    String _pubDate;

    // constructor
    public RSSItem(){

    }

    // constructor with parameters
    public RSSItem(String title, String link, String description, String pubDate) {
        this._title = title;
        this._link = link;
        this._description = description;
        this._pubDate = pubDate;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_link() {
        return _link;
    }

    public void set_link(String _link) {
        this._link = _link;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_pubDate() {
        return _pubDate;
    }

    public void set_pubDate(String _pubDate) {
        this._pubDate = _pubDate;
    }
}
