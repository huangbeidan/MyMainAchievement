package com.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProPublicaPuller {
    public static List<Object> Propuller(String filename) throws IOException{
        List<Object> elements_list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line=br.readLine())!=null){
            if (line.contains("www.propublica.org")){
                Document document = Jsoup.connect(line).get();
                Elements newscontent = document.select(".article-body")
                        .select("p");
                Elements titles = document.select(".article-header")
                        .select("h1");
                Elements titlep = document.select(".article-header")
                        .select("h2");
                Elements time = document.select("time.timestamp");
                elements_list.add("<a href=\""+line+"\">Pro Publica</a>");
                elements_list.add(titles);
                elements_list.add("an");
                elements_list.add("the");
//                elements_list.add(titlep);
//                elements_list.add(time);
                elements_list.add(newscontent);
            }
        }
        return elements_list;
    }


    public static void propuller(String url, String filename) throws IOException{
        //the URL of the webpage of interest
        //This is the input
        //String url = "https://www.propublica.org/article/oregon-psychiatric-security-review-board-timeline";

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
        // Write document to txt files

        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter( new FileWriter( filename));
            for (Element element:titles){
                writer.write(element.toString());
                writer.newLine();
            }
            for (Element element:titlep){
                writer.write(element.toString());
                writer.newLine();
            }
            for (Element element:time){
                writer.write(element.toString());
                writer.newLine();
            }
            for (Element element:newscontent){
                writer.write(element.toString());
                writer.newLine();
                System.out.println(String.format("%s",element.toString() ));
            }
        }
        catch ( IOException e)
        {System.out.println("Fail to write to txt file!");
        }
        writer.close();
    }

    public static void pullallnews(String filename) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line=br.readLine())!=null){

        }
    }

    public static void newswriter(List<Object> elements_list) throws IOException{
        BufferedWriter writer = null;
        String filename = "newsfrom10Fact.txt";
        try{
            writer = new BufferedWriter( new FileWriter( filename));
            for(int i=0;i<elements_list.size();i+=5){
                writer.write("\n\n"+elements_list.get(i).toString());
                writer.newLine();
                writer.write(elements_list.get(i+1).toString());
                writer.newLine();
                writer.write(elements_list.get(i+2).toString());
                writer.newLine();
                writer.write(elements_list.get(i+3).toString());
                writer.newLine();
                writer.write(elements_list.get(i+4).toString());
                writer.newLine();
                writer.write(" ");
            }

        }
        catch ( IOException e)
        {System.out.println("Fail to write to txt file!");
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException{
        String url = "https://www.propublica.org/article/chicago-city-council-ticketing-and-debt-reforms";
        String filename = "newsfrom10Fact";
        List<Object> lists_news = new ArrayList<>();
        lists_news = Propuller("final_urls.txt");
        newswriter(lists_news);
    }



}
