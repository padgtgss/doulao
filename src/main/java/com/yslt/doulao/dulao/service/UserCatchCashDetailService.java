package com.yslt.doulao.dulao.service;

import java.util.List;

import com.yslt.doulao.dulao.pojo.CashPojo;

/**
 * @Description: UserCatchCashDetailService
 * @anthor: shi_lin
 * @CreateTime: 2015-11-12
 */
public interface UserCatchCashDetailService {

	List getDetails(String userId,String recordtype);

	//获取smallCash和Cash
	CashPojo getCash(String userId);
}
