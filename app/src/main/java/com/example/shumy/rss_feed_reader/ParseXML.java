package com.example.shumy.rss_feed_reader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseXML {

    private static final String TAG = "ParseXML";

    public ParseXML() {this.mFeedList = new ArrayList<>();}

    private ArrayList<FeedEntry> mFeedList;

    public ArrayList<FeedEntry> getFeedList() {
        return mFeedList;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        boolean gotImage = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();

                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        else if(("image".equalsIgnoreCase(tagName)) && inEntry) {
                            String imageResolution = xpp.getAttributeValue(null,"height");
                            if(imageResolution != null) {
                                gotImage = "53".equalsIgnoreCase(imageResolution);
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(inEntry) {
                            if("entry".equalsIgnoreCase(tagName)) {
                                mFeedList.add(currentRecord);
                                inEntry = false;
                            }
                            else if("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setName(textValue);
                            }
                            else if("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            }
                            else if("releaseDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setReleaseDate(textValue);
                            }
                            else if("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            }
                            else if("image".equalsIgnoreCase(tagName)) {
                                if(gotImage) {
                                    currentRecord.setImageURL(textValue);
                                }
                            }
                        }
                        break;

                     default:
                }
                eventType = xpp.next();
            }
        }
        catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
