package com.yslt.doulao.dulao.service.impl;

import com.alibaba.fastjson.JSON;
import com.yslt.doulao.dulao.dao.AdvertisementDao;
import com.yslt.doulao.dulao.entity.Advertisement;
import com.yslt.doulao.dulao.enums.AdvertisementTypeEnum;
import com.yslt.doulao.dulao.service.AdvertisementService;
import common.entity.BaseBusinessException;
import common.util.db.BaseDao;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @Description: AdvertisementServiceImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-26
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    @Resource
    private BaseDao baseDao;

    @Override
    public String save(Long startTime, Long endTime, AdvertisementTypeEnum typeEnum, MultipartFile imgFile) {

        //保存图片、
        String uploadUrl = "http://120.24.76.144:8080/doulao/upload";
        String imgPath = "";
        HttpClient httpclient =  HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(uploadUrl);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
       try{
           FileItem fileItem = ((CommonsMultipartFile) imgFile).getFileItem();
           DiskFileItem fi = (DiskFileItem)fileItem;
           FileBody fileBody = new FileBody(fi.getStoreLocation(),"images/*");
          reqEntity.addPart("pic", fileBody);
           httppost.setEntity(reqEntity);
           HttpResponse response = httpclient.execute(httppost);
           HttpEntity resEntity = response.getEntity();
          String result = EntityUtils.toString(resEntity);
          imgPath =  JSON.parseObject(JSON.parseObject(result).get("data").toString()).get("avatar").toString();
       }catch (Exception e){
            return "文件上传失败！";
       }
        if(StringUtils.isBlank(imgPath)){
            return "文件上传失败！";
        }

        Advertisement advertisement = new Advertisement();
        advertisement.setImgPath(imgPath);
        advertisement.setStartTime(startTime);
        advertisement.setEndTime(endTime);
        advertisement.setType(typeEnum);
        advertisement.setBusinessPageUrl("");
        baseDao.save(advertisement);
        return null;
    }
}
