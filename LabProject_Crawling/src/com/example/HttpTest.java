package com.example;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import com.example.OutJson;

public class HttpTest {

    // Send GET requests to get json back

    public static String getHttpResult(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        String json = null;
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                json = EntityUtils.toString(entity, "UTF-8").trim();

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpget.abort();
        }
        return json;
    }

    @Test
    public void test() {
        String rs = getHttpResult(
                "https://www.googleapis.com/customsearch/v1?key=AIzaSyBocsN503B8KseIGe50x6ljCngV19WdaFI&cx=002464989021538692093:eithhs-8rfs&q=site:http://www.stevepoizner.com/&dateRestrict=m1");
        System.out.println(rs);
        OutJson.createJsonFile(rs,"/Users/beidan","getjson");
        System.out.println("生成文件完成！");

    }

}
