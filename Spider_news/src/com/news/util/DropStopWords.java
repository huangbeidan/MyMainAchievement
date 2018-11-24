package com.news.util;

import com.news.URL_FILTERS.path_name_generator;

import java.io.*;

import static com.news.util.Stopwords.isStemmedStopword;
import static com.news.util.Stopwords.isStopword;

public class DropStopWords {

    // Read line by line, keep only the key words each line
    public static void dropstopwords(String filename) throws IOException {

        BufferedWriter writer = null;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String path = path_name_generator.pathname();
        writer = new BufferedWriter(new FileWriter(path + "allkeywords_10Facts.txt"));
        String line;
        String after;
        int i = 0;

        while ((line = br.readLine()) != null) {
            String result = "";
            String[] words = line.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
            for (String word : words) {
                if (!word.isEmpty() && !isStopword(word) && !isStemmedStopword(word)) {
                    result += (word + " ");
                }
            }

            try {
                writer.write(result);
                writer.newLine();
                i += 1;

            } catch (IOException e) {
                System.out.println("Fail to write to txt file!");
            }
        }

        System.out.println(String.format("Successfully wrote %s lines of news", i));
        writer.close();


//                    if(isStopword(word)) continue; //remove stopwords
//                if(isStemmedStopword(word)) continue;; //remove stemstopwords
//                if(word.charAt(0) >= '0' && word.charAt(0) <= '9') continue; //remove numbers, "25th", etc
        // result += (word+" ");
//            }
//            System.out.println(result);


    }
//        System.out.println(String.format("Successfully wrote %s lines of news", i));
//        writer.close();


    public static void main(String[] args) throws IOException {
        String path = path_name_generator.pathname();
        dropstopwords(path + "clean_10FactNews.txt");


        // Below for the purpose of testing
//        String test = "I am doing my homework, can you help me finish all the studies";
//        String after = Stopwords.removeStemmedStopWords(test);
//        System.out.println(after);
//        System.out.println(Stopwords.stemString("studies"));
//        String line = "it's that's a great idea, Beidan's idea works well";
//        String[] words = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
//        for (String word:words){
//            System.out.println(word);
        }



    }

