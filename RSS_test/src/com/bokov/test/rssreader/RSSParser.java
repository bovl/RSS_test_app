package com.bokov.test.rssreader;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by std_bokov on 26.03.2014.
 */

/**
 * RSSParser class used for parsing RSS
 */
public class RSSParser {

    // RSS XML tags
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_LINK = "link";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";

    // constructor
    public RSSParser() {

    }

    /**
     * Get node value
     *
     * @param elem element
     */
    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE ||
                            ( child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * Getting value of selected tag of element
     *
     * @param item -
     * @param tagName -
     * */
    public String getValue(Element item, String tagName) {
        NodeList list = item.getElementsByTagName(tagName);
        return this.getElementValue(list.item(0));
    }

    /**
     * Getting RSS feed items <item>
     *
     * @param rssURL - URL of RSS
     * @return - List of RSSItem class objects
     * */
    public List<RSSItem> getRSSFeedItems(String rssURL) {
        List<RSSItem> rssItemsList = new ArrayList<RSSItem>();
        String rssFeedXML;

        // get RSS XML from rss url
        rssFeedXML = this.getXmlFromUrl(rssURL);

        // check whether RSS XML fetched or not
        if (rssFeedXML != null) {
            // successfully fetched rss xml
            // parse the xml
            try {
                Document document = this.getDomElement(rssFeedXML);
                NodeList nodeList = document.getElementsByTagName(TAG_CHANNEL);
                Element element = (Element) nodeList.item(0);

                // getting items array
                NodeList items = element.getElementsByTagName(TAG_ITEM);

                // go through each item
                for (int i = 0; i < items.getLength(); i++) {
                    Element element1 = (Element) items.item(i);

                    String title = this.getValue(element1, TAG_TITLE);
                    String link = this.getValue(element1, TAG_LINK);
                    String description = this.getValue(element1, TAG_DESRIPTION);
                    String pubDate = this.getValue(element1, TAG_PUB_DATE);

                    RSSItem rssItem = new RSSItem(title, link, description, pubDate);

                    // adding item to list
                    rssItemsList.add(rssItem);
                }
            } catch(Exception e) {
                // Check log for errors
                e.printStackTrace();
            }
        }
        // return items list
        return rssItemsList;
    }

    /**
     * Method to get xml content from HTTP Get request
     * @param url - is URL used for RSS
     * @return XML stored in String object
     * */
    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * Getting XML DOM element
     *
     * @param xml - string representation of XML
     * */
    public Document getDomElement(String xml) {
        Document document = null;
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        // need this for correct parsing of CDATA
        builderFactory.setCoalescing(true);

        try {

            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            document = (Document) documentBuilder.parse(inputSource);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return document;
    }
}
