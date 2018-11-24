package com.news.URL_FILTERS;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class URL_filter {

    public static List<String> url_filter(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        List<String> after = new ArrayList<>();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(date).toString();

        while ((line = br.readLine()) != null) {
            // process the line.

//
            String[] elements = line.split("\\/");


            List<String> criterias = new ArrayList<>();
            // add criteria below
            criterias.add(String.format("%s",year));
            criterias.add("story");
            criterias.add("blog");
            criterias.add("blogs");
            criterias.add("news");
            criterias.add("article");
            criterias.add("fact-check");
            criterias.add("politics");
            criterias.add("atpropublica");
            criterias.add("punditfact");

            if (elements[1].equals("www.politifact.com")){
                if (elements.length>=6){
                    after.add(line);
                    System.out.println(line);
                }
            }
            else{
                if (elements.length>=5 && criterias.contains(elements[3])|criterias.contains(elements[4])){
                    after.add(line);
                    System.out.println(line);}
            }
        }
        return after;
    }

    public static void createFile(String filename, List<String> arrData) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        int size = arrData.size();
        for (int i = 0; i < size; i++) {
            String str = arrData.get(i).toString();
            writer.write(str);
            if (i < size - 1) {//This prevent creating a blank like at the end of the file**
                writer.write("\n");
            }
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {

//        String dir = "/Users/beidan/Spider/results/";
//        List<String> alllinks = getalllinks(dir);
//
//        createFile("result",alllinks);
//        System.out.println(alllinks);

//
//        List<String> links = extracturl.links(file);
//        Object[] seedlinks = links.toArray();

        String path = path_name_generator.pathname();
        String filename = path + "final_urls.txt";


        List<String> after = new ArrayList<>();
        after = url_filter("/Users/beidan/Spider/result.txt");
        createFile(filename,after);

    }







}
