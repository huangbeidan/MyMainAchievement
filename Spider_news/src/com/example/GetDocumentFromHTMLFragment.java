package com.example;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author ASUS-PC
 */
public class GetDocumentFromHTMLFragment {



    public static void main(String[] args) throws IOException {



        //the HTML Fragment of the webpage of interest
        String html = "<div><p>Lorem ipsum.</p>";



        //get document of given html fragment
        Document document = Jsoup.parseBodyFragment(html);



        //get the outerhtml of the document
        String outerHtml = document.outerHtml();



        //get the body element of the document
        Element bodyElement = document.body();
        String body = bodyElement.toString();
    }

}