package com.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBusterspuller {

    public static List<Object> Nbusterpuller(String filename) throws IOException {
        List<Object> elements_list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line=br.readLine())!=null){
            if (line.contains("www.newsbusters.org")){
                Document document = Jsoup.connect(line).get();
                Elements newscontent = document.select(".field-item")
                        .select("p");
                Elements titles = document.select("h1.page-header");


                elements_list.add("<a href=\""+line+"\">NewsBusters</a>");
                elements_list.add(titles);
                elements_list.add("an");
                elements_list.add("the");
                elements_list.add(newscontent);
            }
        }
        return elements_list;
    }

    public static void main(String[] args) throws IOException {
        List<Object> lists = new ArrayList<>();
        lists = Nbusterpuller("final_urls.txt");
        System.out.println(lists.get(0));
        System.out.println(lists.get(1));
        System.out.println(lists.get(2));
    }
}
