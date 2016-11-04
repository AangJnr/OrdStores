package com.shop.ordstore;


import android.util.Log;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * Created by AangJnr on 9/20/16.
 */
public class DateUtil {






    public static String getDateInMillisToString(){

        long now = System.currentTimeMillis();
        String dateTime = String.valueOf(now);

        return dateTime;

    }




    public static String convertStringToPrettyTime(String date){
        PrettyTime prettyTime = new PrettyTime();
        long l = Long.parseLong(date);

        String prettyTimeFormat = prettyTime.format(new Date(l));
        return prettyTimeFormat;
    }


    }








