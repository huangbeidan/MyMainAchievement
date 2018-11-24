package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class JSOUPIMAGE {

    public static void main(String[] args) throws MalformedURLException, IOException {
        String url = "https://www.toutiao.com/a6568327638044115460/";
        String html = HttpUtils.get(url);
        Document doc = Jsoup.parse(html);
        Elements imgs = doc.getElementsByTag("img");
        for (Element img : imgs) {
            String imgSrc = img.attr("src");
            if (imgSrc.startsWith("//")) {
                imgSrc = "http:" + imgSrc;
            }
            Files.copy(new URL(imgSrc).openStream(), Paths.get("./img/"+ UUID.randomUUID()+".png"));
        }
    }
}
