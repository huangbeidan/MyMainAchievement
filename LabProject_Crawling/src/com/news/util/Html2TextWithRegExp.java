package com.news.util;

import com.news.URL_FILTERS.path_name_generator;

import java.io.*;

public class Html2TextWithRegExp {

    public static void html2text(String filename) throws IOException {

        BufferedWriter writer = null;
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String path = path_name_generator.pathname();
        writer = new BufferedWriter( new FileWriter( path+"clean_10FactNews.txt"));
        String line;
        int i=0;
        while ( (line=br.readLine()) != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(line);
            String nohtml = sb.toString().replaceAll("\\<.*?>","");
            try{
                writer.write(nohtml);
                writer.newLine();
                i+=1;
            }catch ( IOException e)
            {System.out.println("Fail to write to txt file!");}
        }
        System.out.println(String.format("Successfully wrote %s lines of news", i));
        writer.close();
    }



    public static void main(String[] args) throws IOException{
        String path = path_name_generator.pathname();
        String filename = path + "allnews_10facts.txt";
        html2text(filename);
    }


}
