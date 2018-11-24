package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ASUS-PC
 */

public class getElementsUsingSelect {

    public static void main(String[] args) throws IOException {

        //the URL of the webpage of interest
        String url = "https://www.propublica.org/article/oregon-psychiatric-security-review-board-timeline";

        //connect to url and get document
        Document document = Jsoup.connect(url).get();

        //get links using select
        Elements newscontent = document.select(".article-body")
                                       .select("p");

        Elements titles = document.select(".article-header")
                                  .select("h1");

        Elements titlep = document.select(".article-header")
                                  .select("h2");

        Elements time = document.select("time.timestamp");




}
}