package com.yslt.doulao.dulao.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: com.yslt.doulao.dulao.controller
 * @anthor: shi_lin
 * @CreateTime: 2015-11-26
 */
public class AdvertisementDaoImplControllerTest {


    @Test
    public void testSaveAdvertisement() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("startTime", 1440604979L);
        param.put("endTime", 1473383349L);
        param.put("type", 4);

        HttpClient httpclient =  HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost("http://localhost:8888/doulao/advertisement");
        FileBody userAvatar = new FileBody(new File(new String("C:\\Users\\idoulao java\\Desktop\\000.jpg".getBytes())), "image/*");
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));

    //    reqEntity.addPart("sign", new StringBody(MD5Util.encrypt(newSignString)));
        for (Map.Entry entry : param.entrySet()) {
            reqEntity.addPart(entry.getKey().toString(), new StringBody(entry.getValue().toString(), Charset.forName("utf-8")));
        }
        reqEntity.addPart("img", userAvatar);
        httppost.setEntity(reqEntity);
      //  httppost.setHeader("token","bf83ebbded5b491cb62d750d89956eb2");

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(EntityUtils.toString(resEntity));

    }
}