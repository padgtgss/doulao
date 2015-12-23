package com.yslt.doulao.dulao.service;

import com.yslt.doulao.dulao.pojo.OneCatchSharePojo;

/**
 * @Description: OneCatchService
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
public interface OneCatchService {
	/**
	 * 执行捞一捞
	 * 
	 * @param userId
	 * @return
	 */
	String excute(String userId, int extraCost);

	/**
	 * 获取用户当天的剩余捞一捞次数
	 * 
	 * @param userId
	 * @return
	 */
	int getOneCatchSum(String userId);

	/**
	 * 分享成功，系统回调
	 * 
	 * @param oneCatchSharePojo
	 */
	String callBackShare(OneCatchSharePojo oneCatchSharePojo);
}
