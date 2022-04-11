package com.example.assisgnmentjr;

import android.appwidget.AppWidgetProvider;

public class WidgetClass extends AppWidgetProvider {

        private String title;
        private String description;
        private String date;

        public WidgetClass()
        {
            title = "";
            description = "";
            date = "";
        }

        public WidgetClass(String atitle,String adescription,String adate)
        {
            title = atitle;
            description = adescription;
            date = adate;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String atitle)
        {
            title = atitle;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String adescription)
        {
            description = adescription;
        }

        public String getDate()
        {
            return date;
        }

        public void setDate(String adate)
        {
            date = adate;
        }

        public String toString()
        {
            String temp;

            temp = title + " " + description + " " + date;

            return temp;
        }

    } // End of class

