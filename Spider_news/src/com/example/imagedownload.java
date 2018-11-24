package com.example;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.example.HttpUtils.getImageSrc;

public class imagedownload {


    public static void main(String[] args) throws MalformedURLException, IOException {
        String url = "https://www.toutiao.com/a6568327638044115460/";
        String html = HttpUtils.get(url);
        List<String> imgUrls = getImageSrc(html);
        for (String imgSrc : imgUrls) {
            Files.copy(new URL(imgSrc).openStream(), Paths.get("./img/"+ UUID.randomUUID()+".png"));
        }
    }



}
