package com.news;

import com.news.URL_FILTERS.path_name_generator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.news.Allnewspuller.WriteAllNews;
import static com.news.Allnewspuller.allnewspuller;
import static com.news.URL_FILTERS.URL_filter.createFile;
import static com.news.URL_FILTERS.URL_filter.url_filter;
import static com.news.util.DropStopWords.dropstopwords;
import static com.news.util.Html2TextWithRegExp.html2text;
import static com.news.util.WordCount.Get_topN;
import static com.news.util.WordCount.count_words;

public class NEWS_ONEStep {

    public static void main(String[] args) throws IOException {
        /** All results would be saved into the subfolder named by date */

        // Define some public variables
        String path = path_name_generator.pathname();

        // Step one: filter the original result.txt (1st url list) to delete unwanted links
        String final_url_name = path + "final_urls.txt";
         List<String> after = url_filter("/Users/beidan/Spider/result.txt");
         createFile(final_url_name,after);

        // Step two: Pull all the news from the filtered-url-list and save into txt file (in html format)
        List<List<Object>> news_lists = allnewspuller(path+"final_urls.txt");
        WriteAllNews(path+"allnews_10facts.txt",news_lists);

//        // Step three: Clean up the allnews.txt file (delete html tags) and generate clean_10FactNews.txt file
        html2text(path+"allnews_10facts.txt");

        // Step four: Get rid of all stop words and stem them! This will generate allkeywords_10Facts.txt file
        dropstopwords(path + "clean_10FactNews.txt");

        // Step five: Count top N words of each news piece and this will generate TopNwords_10Facts.txt file
        List<HashMap<String,Integer>> MAPS = count_words(path + "allkeywords_10Facts.txt");
        Get_topN(MAPS);
    }

}
