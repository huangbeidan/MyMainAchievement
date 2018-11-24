package com.example;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author ASUS-PC
 */
public class GetHtmlCode {

    public static void main(String[] args) throws IOException {

        //the URL of the webpage of interest
        String url = "http://en.wikipedia.org/";

        //connect to url and get document
        Document document = Jsoup.connect(url).get();

        //get the outerhtml of the document
        String outerHtml = document.outerHtml();

        //get the head element of the document
        Element headElement = document.head();
        String head = headElement.toString();

        //get the body element of the document
        Element bodyElement = document.body();
        String body = bodyElement.toString();
    }
}