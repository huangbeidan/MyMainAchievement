package com.news.URL_FILTERS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class path_name_generator {

    public static String pathname(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(date).toString();
        String path = String.format("/Users/beidan/Spider_news/%s/", today);
        return path;
    }


}
