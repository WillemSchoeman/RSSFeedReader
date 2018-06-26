package com.example.shumy.rss_feed_reader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listItems = findViewById(R.id.feedListView);

        Log.d(TAG, "onCreate: starting the data download");

        GetRawData getRawData = new GetRawData();
        getRawData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");

        Log.d(TAG, "onCreate: download done");

    }

    private class GetRawData extends AsyncTask<String,Void,String> {

        private static final String TAG = "GetRawData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);

            ParseXML parseXML = new ParseXML();
            parseXML.parse(s);

            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(MainActivity.this,R.layout.list_item,parseXML.getFeedList());
            listItems.setAdapter(arrayAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);

            String rssFeed = downloadXML(strings[0]);

            if(rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading data");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {

            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];

                while(true) {
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0) {
                        break;
                    }
                    if(charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer,0,charsRead));
                    }
                }
                reader.close();

                return xmlResult.toString();
            }
            catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            }
            catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data " + e.getMessage());
            }
            catch (SecurityException e) {
                Log.e(TAG, "downloadXML: Security Exception. Needs Permission?" + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }

}

























