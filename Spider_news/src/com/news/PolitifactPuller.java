package com.news;

import org.eclipse.jetty.util.IO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolitifactPuller {

    public static List<Object> Politifactpuller(String filename) throws IOException {
        List<Object> elements_list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line=br.readLine())!=null){
            if (line.contains("www.politifact.com")){
                Document document = Jsoup.connect(line).get();
                Elements newscontent = document.select(".article__text")
                        .select("p");
                Elements titles = document.select(".article__title")
                        .select("h1");
                Elements titlep = document.select("p.article__meta");

                elements_list.add("<a href=\""+line+"\">Politifact</a>");
                elements_list.add(titles);
                elements_list.add("an");
                elements_list.add("the");
//                elements_list.add(titlep);
//                elements_list.add("time is");
                elements_list.add(newscontent);
            }
        }
        return elements_list;
    }

    public static void main(String[] args) throws IOException {
        List<Object> lists = new ArrayList<>();
        lists = Politifactpuller("final_urls.txt");
        System.out.println(lists.get(5));
    }
}
