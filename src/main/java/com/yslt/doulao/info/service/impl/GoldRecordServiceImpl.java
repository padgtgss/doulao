package com.yslt.doulao.info.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yslt.doulao.info.dao.GoldRecordDao;
import com.yslt.doulao.info.dao.UserDao;
import com.yslt.doulao.info.entity.GoldRecord;
import com.yslt.doulao.info.service.GoldRecordService;

/**
 * @Description: GoldRecordServiceImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */
@Service
public class GoldRecordServiceImpl implements GoldRecordService {

	@Resource
	private UserDao userDao;
	@Resource
	private GoldRecordDao goldRecordDao;

	@Override
	public void saveExperienceDetail(GoldRecord goldRecord) {
		goldRecordDao.save(goldRecord);
		userDao.updateExperience(goldRecord.getUserId(),goldRecord.getCountent());
	}
}
