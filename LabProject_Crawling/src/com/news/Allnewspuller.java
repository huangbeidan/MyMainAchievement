package com.news;

import com.news.URL_FILTERS.path_name_generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Allnewspuller {
    public static List<List<Object>> allnewspuller(String filename) throws IOException {
        List<List<Object>> news_lists = new ArrayList<>();
        List<Object> news_politifact = PolitifactPuller.Politifactpuller(filename);
        List<Object> news_propublica = ProPublicaPuller.Propuller(filename);
        List<Object> news_allsides = Allsidespuller.allsidepuller(filename);
        List<Object> news_factchecker = Factcheckpuller.Factcheckpuller(filename);
        List<Object> news_mmater = MMatterpuller.Mmaterpuller(filename);
        List<Object> news_nbusters = NBusterspuller.Nbusterpuller(filename);
        List<Object> news_opensecrets = OpenSecretsPuller.Opensecretpuller(filename);
        List<Object> news_snopes = Snopespuller.Snopespuller(filename);
        List<Object> news_sunlight = Sunlightpuller.sunlightpuller(filename);

        news_lists.add(news_politifact);
        news_lists.add(news_propublica);
        news_lists.add(news_allsides);
        news_lists.add(news_factchecker);
        news_lists.add(news_mmater);
        news_lists.add(news_nbusters);
        news_lists.add(news_opensecrets);
        news_lists.add(news_snopes);
        news_lists.add(news_sunlight);
        return news_lists;
    }

    public static void WriteAllNews(String filename,List<List<Object>> news_lists) throws IOException{
        BufferedWriter writer = null;

//        String filename = String.format("/Users/beidan/Spider_news/%s/allnews_10facts.txt", date);

        File file = new File(filename);
        file.getParentFile().mkdirs();
        try{
            writer = new BufferedWriter( new FileWriter(file));
            for (List<Object> news_list : news_lists) {

                for (int i = 0; i < news_list.size(); i += 5) {
                    writer.write("\n\n" + news_list.get(i).toString()+" "+"Thisthenewarticlestart") ;
                    writer.newLine();
                    writer.write(news_list.get(i + 1).toString());
                    writer.newLine();
                    writer.write(news_list.get(i + 2).toString());
                    writer.newLine();
                    writer.write(news_list.get(i + 3).toString());
                    writer.newLine();
                    writer.write(news_list.get(i + 4).toString());
                    writer.newLine();
                    writer.write(" ");
                }
            }
        }
        catch ( IOException e)
        {System.out.println("Fail to write to txt file!");
        }
        writer.close();

    }

    public static void main(String[] args) throws IOException{
        String path = path_name_generator.pathname();

        List<List<Object>> news_lists = new ArrayList<>();
        news_lists = allnewspuller("final_urls.txt");
        WriteAllNews(path+"test_allnews.txt",news_lists);
    }

}
