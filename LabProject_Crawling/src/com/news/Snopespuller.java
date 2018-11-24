package com.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Snopespuller {

    public static List<Object> Snopespuller(String filename) throws IOException {
        List<Object> elements_list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line=br.readLine())!=null){
            if (line.contains("www.snopes.com")){
                Document document = Jsoup.connect(line).get();
                Elements newscontent = document.select(".post-body-card.post-card.card")
                        .select("p");
                Elements claim = document.select("p.claim");
                Elements titles = document.select("header")
                        .select("div.card-body")
                        .select("h1");
                Elements rating = document.select("div.rating-text");

                elements_list.add("<a href=\""+line+"\">Snopes</a>");
                elements_list.add(titles);
                elements_list.add("Claim is: "+claim);
                elements_list.add("Rating is: "+rating);
                elements_list.add(newscontent);
            }
        }
        return elements_list;
    }

    public static void main(String[] args) throws IOException {
        List<Object> lists = new ArrayList<>();
        lists = Snopespuller("final_urls.txt");
        System.out.println(lists.get(0));
        System.out.println(lists.get(1));
        System.out.println(lists.get(2));
        System.out.println(lists.get(3));
        System.out.println(lists.get(4));
        System.out.println(lists.get(5));
        System.out.println(lists.get(6));
    }

}
