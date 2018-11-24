package com.example;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author ASUS-PC
 */
public class GetDocumentFromHTMLFile {



    public static void main(String[] args) throws IOException {



        //the HTML File of the webpage of interest
        File input = new File("/keet/index.html");



        //get document of given html file
        Document document = Jsoup.parse(input , "UTF-8");



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