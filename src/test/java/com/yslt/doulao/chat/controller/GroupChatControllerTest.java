package com.yslt.doulao.chat.controller;

import java.util.HashMap;
import java.util.Map;

import common.var.constants.SystemConstant;
import org.junit.Test;

import common.util.AESUtil;
import common.util.HttpUtil;
import common.util.MD5Util;
import common.util.SignatureUtil;


public class GroupChatControllerTest {

    //测试
    @Test
    public void test() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/test";
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试2
    @Test
    public void test1() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        map.put("userId","5476f100c4aac457fd46adf8");
        String url ="http://localhost:8888/doulao/groupchat/test1";
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

	//测试-创建群
    @Test
	public void createGroup() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/createGroup";
        map.put("groupMaster","56286d680cf26a7a9b5baa16");
        map.put("groupMember","53f4599a0cf20392492bd5dd,567151fa848ed22c770d2d15,55d432ae0cf29799a6c50933");
        map.put("categoryCode","0");
        map.put("type","0");
        map.put("groupName","水群");
        map.put("permission","0");
        map.put("declared","灌水用的");
        map.put("imgPath","/2015/10/23/5629dc8f0cf20dcf640476a7");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-修改群
    @Test
	public void modifyGroup() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/modifyGroup";
        map.put("userId","53a79679e4b04dd588b22b2d");
        map.put("groupId","gg80009009106");
        map.put("categoryCode","3");
        map.put("groupName","LOL新人群");
        map.put("permission","0");
        map.put("declared","群公告：群内大神免费带你打大龙");
        map.put("imgPath","/2014/7/3/53b50e9ae4b0ce188b7b27fa");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-获取群信息
    @Test
	public void groupInfo() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/groupInfo";
        map.put("groupId","gg80009009107");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-解散群
    @Test
	public void deleteGroup() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/deleteGroup";
        map.put("groupId","gg8000900972");
        map.put("userId","56286d680cf26a7a9b5baa16");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-邀请加入群
    @Test
	public void inviteGroup() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/inviteGroup";
        map.put("groupId","gg80009009196");
        map.put("senderId","53f4599a0cf20392492bd5dd");
        map.put("receiverIds","5438cdeec4aae15bc51c0fb8,55f787f60cf29799a6d0d2ba,55c305ca0cf29799a6c122f7");
        map.put("confirm","1");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-确认用户加入群
    @Test
	public void letJoin() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/letJoinGroup";
        map.put("groupId","gg80009009196");
        map.put("senderId","53f4599a0cf20392492bd5dd");
        map.put("receiverIds","5438cdeec4aae15bc51c0fb8");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-删除群成员
    @Test
    public void deleteGroupMember() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/deleteGroupMember";
        map.put("groupId","gg80009009196");
        map.put("userId","53f4599a0cf20392492bd5dd");
        map.put("memberIds","5438cdeec4aae15bc51c0fb8,55f787f60cf29799a6d0d2ba,55c305ca0cf29799a6c122f7");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-我创建的群
    @Test
	public void myGroupList() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/myGroupList";
        map.put("userId","56286d680cf26a7a9b5baa16");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-我是群成员的群列表
    @Test
	public void IAmMember() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/IAmMember";
        map.put("userId","54a0395c0cf21f731a0d08f1");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-我在群内的群列表
    @Test
	public void groupAboutMe() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/groupAboutMe";
        map.put("userId","55f787f60cf29799a6d0d2ba");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-个人申请加入群
    @Test
	public void joinGroup() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/joinGroup";
        map.put("groupId","gg8000900960");
        map.put("userId","537c1b7e45ce8b2f1dfed170");
        map.put("declared","我喜欢这个群的名称，所以我想来这个群。");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }
    //测试-单人主动退出群
    @Test
	public void logoutGroup() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/logoutGroup";
        map.put("groupId","gg80009009107");
        map.put("userId","5438cdeec4aae15bc51c0fb8");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-查询群
    @Test
	public void queryGroup() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/queryGroup";
        map.put("queryContent","123");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-禁言
    @Test
	public void forbidSpeak() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/forbidSpeak";
        map.put("groupId","gg8000900987");
        map.put("masterId","56286d680cf26a7a9b5baa16");
        map.put("memberId","55f787f60cf29799a6d0d2ba");
        map.put("confirm","1");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-用户设置群消息接收规则
    @Test
	public void setGroupMsg() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/setGroupMsg";
        map.put("groupId","gg8000900987");
        map.put("memberId","55f787f60cf29799a6d0d2ba");
        map.put("rule","0");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //测试-查询群成员（从容联那边查询）
    @Test
	public void queryMember() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupchat/queryMember";
        map.put("groupId","gg8000900987");
        map.put("userId","55f787f60cf29799a6d0d2ba");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }

    //----------------------------------------------------------------------------------

    //群功能
    @Test
    public void groupCall() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appid", SystemConstant.APP_ID);
        String url ="http://localhost:8888/doulao/groupfunc/groupCall";
        map.put("groupId","gg80009009196");
        map.put("userId","56286d680cf26a7a9b5baa16");
        map.put("content","zzzxxx");
        String newSignString = SignatureUtil.createSignStrings(map);
        map.put("sign", MD5Util.encrypt(AESUtil.encrypt(newSignString,SystemConstant.DES_ALGORITHM_KEY)));
        String result = HttpUtil.post(url, map);
        System.out.println(result);
    }
}
