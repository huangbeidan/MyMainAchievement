package com.example;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author ASUS-PC
 */
public class GetDocumentFromHTML {



    public static void main(String[] args) throws IOException {



        //the HTML of the webpage of interest
        String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p></body></html>";



        //get document of given html
        Document document = Jsoup.parse(html);



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


