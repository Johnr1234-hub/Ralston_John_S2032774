package com.example.assisgnmentjr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements
        OnClickListener {
    private String[] items;
    private TextView rawDataDisplay;
    private Button startButton;
    private String result = "";
    private LinkedList  <WidgetClass> list;


    private String url1 = "";
    // Traffic Scotland Planned Roadworks XML link
    private String
            urlSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinkedList<WidgetClass> list = null;
        items = new String[50];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        Log.e("MyTag", "after startButton");

        list = parseData(result);

        // Write list to Log for testing
        if (list != null) {
            Log.e("MyTag", "List not null");
            int count = 0;
            for (Object o : list) {
                Log.e("MyTag", o.toString());
                items[count] = o.toString();
                count = count + 1;
            }
            Log.e("My Tag", "Array is " + items.toString());
        } else {
            Log.e("MyTag", "List is null");
        }


    } // End of onCreate

    public void startProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //


    @Override
    public void onClick(View v) {
        Log.e("MyTag", "in onClick");
        startProgress();
        Log.e("MyTag", "after startProgress");
    }


    private LinkedList<WidgetClass> parseData(String dataToParse) {
        WidgetClass widget = null;
        LinkedList<WidgetClass> alist = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Found a start tag
                if (eventType == XmlPullParser.START_TAG) {
                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("rss")) {
                        alist = new LinkedList<WidgetClass>();
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        Log.e("MyTag", "Item Start Tag found");
                        widget = new WidgetClass();
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        Log.e("MyTag", "Bolt is " + temp);
                        widget.setTitle(temp);
                    } else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("MyTag", "Nut is " + temp);
                            widget.setDescription(temp);
                        } else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("date")) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Washer is " + temp);
                                widget.setDate(temp);
                            }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("link")) {
                        Log.e("MyTag", "widget is " + widget.toString());
                        alist.add(widget);
                    } else if (xpp.getName().equalsIgnoreCase("rss")) {
                        int size;
                        size = alist.size();
                        Log.e("MyTag", "rss size is " + size);
                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            return alist;

        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }

        Log.e("MyTag", "End document");

        return alist;
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            Log.e("MyTag", "in run");
            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new
                        InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");
                //

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    Log.e("MyTag", inputLine);
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }


                // Now that you have the xml data you can parse it
                //
                // Now update the TextView to display raw XML data
                // Probably not the best way to update TextView
                // but we are just getting started !

                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("UI thread", "I am the UI thread");
                        rawDataDisplay.setText(result);

                    }
                });

            }
        }
    }





