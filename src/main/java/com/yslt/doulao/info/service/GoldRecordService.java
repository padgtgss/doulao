package com.yslt.doulao.info.service;

import com.yslt.doulao.info.entity.GoldRecord;

/**
 * @Description: GoldRecordService
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */
public interface GoldRecordService {

	/**
	 * 增加经验值记录，并更新用户经验值
	 * 
	 * @param goldRecord
	 */
	void saveExperienceDetail(GoldRecord goldRecord);
}
