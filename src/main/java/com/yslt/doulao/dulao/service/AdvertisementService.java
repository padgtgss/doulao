package com.yslt.doulao.dulao.service;

import com.yslt.doulao.dulao.enums.AdvertisementTypeEnum;
import common.util.db.BaseDao;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: AdvertisementService
 * @anthor: shi_lin
 * @CreateTime: 2015-11-26
 */
public interface AdvertisementService {



    /**
     *
     * @param startTime
     * @param endTime
     * @param typeEnum
     * @param imgFile
     * @return   异常信息
     */
    String save(Long startTime,Long endTime,AdvertisementTypeEnum typeEnum,MultipartFile imgFile);
}
