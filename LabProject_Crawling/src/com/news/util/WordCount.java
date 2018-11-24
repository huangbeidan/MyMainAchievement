package com.news.util;

import com.news.URL_FILTERS.path_name_generator;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class WordCount {
    // count words frequencies and return the top N words
    // My goal is to count words for individual article

    /** My custom unique id is thisthenewarticlestart */

    public static boolean new_article_detector(String line){
//        String[] words = line.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
//        for (String word:words){
//            if (word.equals("Thisthenewarticlestart")) {
//                return true;
//        }
//        }
//        return false;
        if (line.contains("thisthenewarticlestart")){
            return true;
        }
        return false;
    }


    public static List<HashMap<String,Integer>> count_words(String filename) throws IOException {
//        Scanner file = new Scanner(new File(filename)).useDelimiter("[^a-zA-Z]+");
        HashMap<String, Integer> map = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        List<HashMap<String, Integer>> MAPS = new ArrayList<>();
        int counter = 0;
        int note = 0;

        while ((line = br.readLine()) != null) {
//            String word = file.next().toLowerCase();
            //Read the first two lines
            if (counter ==0){
                note+=1;
            }

            if (!new_article_detector(line) || (new_article_detector(line) && counter==2)) {
                String[] words = line.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
                for (String word : words) {
                    if (map.containsKey(word)) {
                        map.put(word, map.get(word) + 1);
                    } else {
                        map.put(word, 0);
//                        System.out.println("Successfully put +1 records");
                    }
                }

            }

            else if (new_article_detector(line) && counter >0) {
                MAPS.add(map);
                System.out.println("add+1+articles"+"NO."+counter);
                map = new HashMap<>();

            }
            counter +=1;
        }
        // Remember to add the last map
        MAPS.add(map);
        return MAPS;
    }


    public static void Get_topN(List<HashMap<String,Integer>> MAPS) throws IOException {
        // Get the current date for filename
        String path = path_name_generator.pathname();
        String filename = path + "TopNwords_10Facts.txt";


        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter(filename));

        for (HashMap<String, Integer> map : MAPS) {
            ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
            Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                    return a.getValue().compareTo(b.getValue());
                }
            });
            for (int i = 0; i < 100; i++) {
                if (entries.size() == 0) {
                    System.out.println("there is no record!");
                } else {
                    try {
                        writer.write(entries.get(entries.size() - i - 1).getKey());
                        writer.write("=" + entries.get(entries.size() - i - 1).getValue() + " ");
                    } catch (Exception e) {
                        System.out.println("Something goes wrong in the writer! Check!!");
                    }
                }
            }
            writer.newLine();
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException{
        String path = path_name_generator.pathname();
        String filename = path + "allkeywords_10Facts.txt";
        List<HashMap<String,Integer>> MAPS = count_words(filename);
        Get_topN(MAPS);
        for (HashMap<String, Integer> map : MAPS) {

            System.out.println(map.entrySet());
//
//
//
//        }


        // Test new article detector
//        String line = "fdjakjkl Thisthenewarticlest fdaklfjd233";
//        if (new_article_detector(line)){
//            System.out.println("Yes it is");
//        }

        // Test count words
//        count_words("allkeywords_10Facts.txt");


        // Test read first blank line
//        BufferedReader br = new BufferedReader(new FileReader(filename));
//        int i=1;
//        while ((br.readLine()!=null)){
//            System.out.println(String.format("read%s ", br.readLine()));
//            i+=1;
        }

    }


}
