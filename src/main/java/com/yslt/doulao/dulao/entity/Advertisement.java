package com.yslt.doulao.dulao.entity;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.yslt.doulao.dulao.enums.AdvertisementTypeEnum;
import common.util.pojo.BaseDomain;

/**
 * @Description: 豆捞广告图片
 * @anthor: shi_lin
 * @CreateTime: 2015-11-26
 */
@Entity(value = "t_advertisement",noClassnameStored = true)
public class Advertisement extends BaseDomain {

    @Property("start_time")
    private Long startTime;  //生效时间

    @Property("end_time")
    private Long endTime;    //过期时间

    @Property("advertisement_type")
    private AdvertisementTypeEnum type;  //图片类型

    @Property("img_path")
    private String imgPath;        //图片url

    @Property("business_page_url")
    private String businessPageUrl;   //广告主页

    public Advertisement() {
    }

//=============================getter and setter============================


    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public AdvertisementTypeEnum getType() {
        return type;
    }

    public void setType(AdvertisementTypeEnum type) {
        this.type = type;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getBusinessPageUrl() {
        return businessPageUrl;
    }

    public void setBusinessPageUrl(String businessPageUrl) {
        this.businessPageUrl = businessPageUrl;
    }
}
