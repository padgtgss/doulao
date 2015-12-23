package com.yslt.doulao.dulao.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.yslt.doulao.dulao.dao.SysUserCatchScaleDao;
import com.yslt.doulao.dulao.entity.*;
import com.yslt.doulao.dulao.enums.*;
import com.yslt.doulao.dulao.pojo.OneCatchFromPHPPojo;
import com.yslt.doulao.dulao.pojo.OneCatchSharePojo;
import com.yslt.doulao.info.dao.DiamondDao;
import com.yslt.doulao.info.dao.UserDao;
import com.yslt.doulao.info.entity.Diamond;
import com.yslt.doulao.info.entity.User;
import common.entity.BaseBusinessException;
import common.util.HttpUtil;
import common.util.MD5Util;
import common.var.exception.SysError;
import common.var.constants.SystemConstant;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.yslt.doulao.dulao.dao.UserCatchCashDetailDao;
import com.yslt.doulao.dulao.dao.UserCatchDao;
import com.yslt.doulao.dulao.dao.UserPrivilegeDetailDao;
import com.yslt.doulao.dulao.service.OneCatchService;
import com.yslt.doulao.info.entity.GoldRecord;
import com.yslt.doulao.info.service.GoldRecordService;
import com.yslt.doulao.info.service.UserActiveService;
import com.yslt.doulao.info.service.UserService;

/**
 * @Description: OneCatchServiceImpl
 * @anthor: shi_lin
 * @CreateTime: 2015-11-11
 */
@Service
public class OneCatchServiceImpl implements OneCatchService {

    @Resource
    private UserPrivilegeDetailDao userPrivilegeDetailDao;
    @Resource
    private UserService userService;
    @Resource
    private UserCatchDao userCatchDao;
    @Resource
    private UserCatchCashDetailDao userCatchCashDetailDao;
    @Resource
    private GoldRecordService goldRecordService;
    @Resource
    private UserActiveService userActiveService;
    @Resource
    private UserDao userDao;
    @Resource
    private SysUserCatchScaleDao sysUserCatchScaleDao;
    @Resource
    private DiamondDao diamondDao;


    public String excute(String userId, int extraCost) {

        // 使用检查次数
        UserCatch userCatch = userCatchDao.getByUser(userId);
        int sum =  this.getOneCatchSum(userId);
        boolean overSum = sum > 0 ? false : true; // 次数超出为true
        if (overSum && extraCost != 1) {
            return JSON.toJSONString(ImmutableMap.of("status", "0", "message", "需要花费60个经验值哦！")); // 次数使用完毕,提示抽奖额外花费
        }
        /// 次数使用完毕，用经验值捞
        if (overSum && extraCost == 1) {
            if(Integer.parseInt(userDao.getById(userId).getExperience()) < 60){
               throw new BaseBusinessException(SysError.USER_EXPERENCE_DEFICIENCY);
            }
            if(sum <=-2){
                return JSON.toJSONString(ImmutableMap.of("status", "-1", "message", "每天只能使用经验值捞两次哟！"));
            }
            GoldRecord goldRecord = new GoldRecord();
            goldRecord.setTypeId("2");
            goldRecord.setCountent("-60");
            goldRecord.setUserId(userId);
            goldRecord.setMessage("都捞额外花费");
            goldRecordService.saveExperienceDetail(goldRecord);
        }

        Map<String, Object> map = new HashMap<String, Object>();

        OneCatchFromPHPPojo oneCatchFromPHPPojo = this.getDetailFromPHP(userId);

        OneCatchTypeEnum oneCatchTypeEnum = oneCatchFromPHPPojo.getOneCatchTypeEnum();

        map.put("type", oneCatchTypeEnum.getNumber());
        map.put("nocash",oneCatchFromPHPPojo.getNocash());
        map.put("targetId","");
        map.put("shareregCashId",oneCatchFromPHPPojo.getShareregCashId());
        if (OneCatchTypeEnum.CASH.equals(oneCatchTypeEnum)) {
            map.put("description", oneCatchFromPHPPojo.getValue());
            //写入数据
            UserCatchCashDetail catchCashDetail = new UserCatchCashDetail();
            catchCashDetail.setCountent(Double.parseDouble(oneCatchFromPHPPojo.getValue()));
            catchCashDetail.setUserId(userId);
            catchCashDetail.setMessage("都捞获取");
            catchCashDetail.setCashFromEnum(CashFromEnum.ONE_CATCH);
            catchCashDetail.setAvailable("0");
            catchCashDetail.setId(oneCatchFromPHPPojo.getTargetId());
            userCatchCashDetailDao.save(catchCashDetail);
            map.put("targetId",oneCatchFromPHPPojo.getTargetId());
        } else if (OneCatchTypeEnum.Empirical.equals(oneCatchTypeEnum)) {
            map.put("description", oneCatchFromPHPPojo.getValue());
        } else if (OneCatchTypeEnum.PRIVILEGE_CARD.equals(oneCatchTypeEnum)) { // 特权卡
            map.put("description", oneCatchFromPHPPojo.getPrivilegeCardTypeEnum().toString());
        }
        // 增加使用次数
        userCatchDao.addCatchSum(userCatch);
        map.put("share", oneCatchFromPHPPojo.getCash()); // 分享
        return JSON.toJSONString(map);
    }

    @Override
    public int getOneCatchSum(String userId) {
        int brickLeave = userService.getBrickLeave(userId);
        String userLeave = userDao.getById(userId).getUserLeave();
        Diamond diamond = diamondDao.getByLeave(brickLeave);   //转卡特权

        UserCatch userCatch = userCatchDao.getByUser(userId);    //已经使用次数
        //获取次数关系系数
        SysUserCatchScale byUserleave = sysUserCatchScaleDao.getByUserleave(userLeave);

        BigDecimal scale = new BigDecimal(byUserleave.getScale()).multiply(new BigDecimal(diamond.getRedTimes()));
        int temp = scale.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return temp - userCatch.getCatchSum();
    }

    @Override
    public String callBackShare(OneCatchSharePojo oneCatchSharePojo) {
        OneCatchTypeEnum oneCatchTypeEnum = OneCatchTypeEnum.getByNumber(oneCatchSharePojo.getTypeId());
        if (OneCatchTypeEnum.CASH.equals(oneCatchTypeEnum)) { // 现金
            //调用PHP
            userCatchCashDetailDao.updateAvailableById(oneCatchSharePojo.getTargetId());
                return this.cashCallBack(oneCatchSharePojo.getUserId(), oneCatchSharePojo.getTargetId());
        } else if (OneCatchTypeEnum.Empirical.equals(oneCatchTypeEnum)) { // 经验值
            GoldRecord goldRecord = new GoldRecord();
            goldRecord.setTypeId("2");
            goldRecord.setCountent(oneCatchSharePojo.getDescription());
            goldRecord.setUserId(oneCatchSharePojo.getUserId());
            goldRecord.setMessage("都捞获取");
            goldRecordService.saveExperienceDetail(goldRecord);
        } else if (OneCatchTypeEnum.PRIVILEGE_CARD.equals(oneCatchTypeEnum)) { // 特权卡
            this.privlegeCardShare(oneCatchSharePojo);
        }
        return "0";
    }

    // =========================== private methods========================

    private int getRandomNumByScope(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 特权卡数据写入
     ***/
    private void privlegeCardShare(OneCatchSharePojo pojo) {
        if (PrivilegeCardTypeEnum.ACTIVE_CARD.toString().equals(pojo.getDescription())) {
            userActiveService.updateByPrivilegeCard(pojo.getUserId());

        } else if (PrivilegeCardTypeEnum.REWARD_CARD.toString().equals(pojo.getDescription())) {
            UserPrivilegeDetail detail = new UserPrivilegeDetail();
            detail.setUserId(pojo.getUserId());
            detail.setTypeId(String.valueOf(PrivilegeCardTypeEnum.REWARD_CARD.getTypeId()));
            userPrivilegeDetailDao.save(detail);

        } else if (PrivilegeCardTypeEnum.SIGN_CARD.toString().equals(pojo.getDescription())) {
            UserPrivilegeDetail detail = new UserPrivilegeDetail();
            detail.setUserId(pojo.getUserId());
            detail.setTypeId(String.valueOf(PrivilegeCardTypeEnum.SIGN_CARD.getTypeId()));
            userPrivilegeDetailDao.save(detail);
        } else if (PrivilegeCardTypeEnum.PASS_THROUGH_CARD.toString().equals(pojo.getDescription())) {
            UserPrivilegeDetail detail = new UserPrivilegeDetail();
            detail.setUserId(pojo.getUserId());
            detail.setTypeId(String.valueOf(PrivilegeCardTypeEnum.PASS_THROUGH_CARD.getTypeId()));
            userPrivilegeDetailDao.save(detail);

        }
    }


    /**
     * 转盘数据调用
     **/
    private OneCatchFromPHPPojo getDetailFromPHP(String userId) {
        int brickLeave = userService.getBrickLeave(userId);
         User user = userDao.getById(userId);
        String userLeave =user.getUserLeave();
        String mobile = user.getMobile();

        SysUserCatchScale byUserleave = sysUserCatchScaleDao.getByUserleave(userLeave);

        Diamond diamond = diamondDao.getByLeave(brickLeave);
        double money = new BigDecimal(diamond.getMaxSmallCash()).multiply(new BigDecimal(byUserleave.getScale())).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        MD5Util md5 = new MD5Util();
        String hash = md5.getMD5("username"+mobile + "brickleave"+brickLeave+"nowlimit"+ money + "action"+"app_share"+"appkey"+"X7J28D9CK3"+"ysltllb");
        map.put("username", mobile);
        map.put("brickleave", brickLeave);
        map.put("nowlimit", money);
        map.put("action", "app_share");
        map.put("appkey", "X7J28D9CK3");
        map.put("hash", hash);
        String json = HttpUtil.post(SystemConstant.PHP_API_URL, map);
        OneCatchFromPHPPojo oneCatchFromPHPPojo = new OneCatchFromPHPPojo();
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject data = JSONObject.parseObject(jsonObject.get("data").toString());
        oneCatchFromPHPPojo.setTargetId(data.get("xjid").toString());
        oneCatchFromPHPPojo.setCash(data.get("bonus").toString());
        oneCatchFromPHPPojo.setOneCatchTypeEnum(OneCatchTypeEnum.getByNumber(Integer.parseInt(data.get("gifttype").toString())));
        oneCatchFromPHPPojo.setValue(data.get("num").toString());
        oneCatchFromPHPPojo.setNocash(data.get("nocash").toString());
        oneCatchFromPHPPojo.setShareregCashId(data.get("shareregid").toString());
        if (oneCatchFromPHPPojo.getOneCatchTypeEnum().equals(OneCatchTypeEnum.PRIVILEGE_CARD)) {
            oneCatchFromPHPPojo.setPrivilegeCardTypeEnum(PrivilegeCardTypeEnum.getByTypeId(Integer.parseInt(data.get("cardtype").toString())));
        }
        return oneCatchFromPHPPojo;
    }

    /**
     * 转盘现金回调
     ***/
    private String cashCallBack(String userId, String targetId) {
        String mobile = userDao.getById(userId).getMobile();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        MD5Util md5 = new MD5Util();
        String hash = md5.getMD5(mobile + "app_shareed");
        map.put("username", mobile);
        map.put("action", "app_shareed");
        map.put("xjid", targetId);
        map.put("appkey", "X7J28D9CK3");
        map.put("hash", hash);
        String json = HttpUtil.post(SystemConstant.PHP_API_URL, map);
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject data = JSONObject.parseObject(jsonObject.get("data").toString());
        return data.get("ischange").toString();
    }

}
