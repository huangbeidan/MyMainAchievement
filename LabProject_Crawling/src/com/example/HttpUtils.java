package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {
    public static String get(String url) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) getUrl
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "*/*");
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; CIBA)");
            connection.setRequestProperty("Accept-Language", "zh-cn");
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null){
                result.append(line);
            }
            reader.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String url = "https://www.toutiao.com/a6568327638044115460/";
        String html = HttpUtils.get(url);
        var imgUrls = getImageSrc(html);
        for (String imgSrc : imgUrls) {
            System.out.println(imgSrc);
        }
    }
    public static List<String> getImageSrc(String html) {
        // 获取img标签正则
        String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
        // 获取src路径的正则
        String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);
        List<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            Matcher m = Pattern.compile(IMGSRC_REG).matcher(matcher.group());
            while (m.find()) {
                listImgUrl.add(m.group().substring(0, m.group().length() - 1));
            }
        }
        return listImgUrl;
    }

}