package com.yslt.doulao.dulao.dao;

import com.yslt.doulao.dulao.entity.SysUserCatchScale;

/**
 * @Description: SysUserCatchScaleDao
 * @anthor: shi_lin
 * @CreateTime: 2015-11-18
 */
public interface SysUserCatchScaleDao {


    /**
     * 根据用户等级获取关系系数
     * @param userLeave
     * @return
     */
    SysUserCatchScale getByUserleave(String userLeave);
}
