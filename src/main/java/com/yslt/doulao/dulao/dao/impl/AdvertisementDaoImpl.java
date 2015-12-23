package com.yslt.doulao.dulao.dao.impl;

import com.yslt.doulao.dulao.dao.AdvertisementDao;
import com.yslt.doulao.dulao.entity.Advertisement;
import org.springframework.stereotype.Repository;

/**
 * @Description: AdvertisementDaoImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-27
 */
@Repository
public class AdvertisementDaoImpl  implements AdvertisementDao{

    @Override
    public void save(Advertisement advertisement) {
        /*BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("endTime", advertisement.getEndTime());
        basicDBObject.put("imgPath", advertisement.getImgPath());
        basicDBObject.put("startTime", advertisement.getStartTime());
        basicDBObject.put("typeId", advertisement.getType().getTypeId());
        basicDBObject.put("createTime", new SimpleDateFormat(SystemConstant.DATE_SIMPLE_FORMAT).format(new Date()));
        basicDBObject.put("available", 1);
        userPrivilegeTable.insert(basicDBObject);*/
    }
}
