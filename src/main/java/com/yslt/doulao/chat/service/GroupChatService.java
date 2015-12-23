package com.yslt.doulao.chat.service;

import com.yslt.doulao.chat.entity.GroupMember;

import java.util.List;
import java.util.Map;

public interface GroupChatService {

	/**
	 * 建群
	 * @param groupName	  （容联）群名称
	 * @param groupMaster （容联）群主
	 * @param groupMember （容联）群成员
	 * @param categoryCode 群分类代号
	 * @param type （容联）群类型（群组类型  
					0：临时组(上限100人) 
					1：普通组(上限300人) 
					2：普通组(上限500人) 
					3：付费普通组 (上限1000人) 
					4：付费VIP组（上限2000人））
	 * @param permission （容联）申请加入模式
							0：默认直接加入 
							1：需要身份验证 
							2：私有群组
	 * @param declared （容联）群公告
	 * @return
	 */
	Map<String,Object> createGroup(String groupName,String groupMaster, String[] groupMember,String categoryCode,String type,
			String permission,String declared,String imgPath);
	
	/**
	 * 获取群信息
	 * @param groupId
	 * @return
	 */
	Map<String,Object> getGroupInfo(String groupId);

	/**
	 * 主动申请加入群
	 * @param groupId
	 * @param userId
	 * @param declared
	 */
	Map<String ,Object> joinGroup(String groupId,String userId,String declared);

	/**
	 * 我创建的群列表
	 * @param userId
	 */
	Map<String ,Object> myGroupList(String userId);

	/**
	 * 我是群成员，不是群主的列表
	 * @param userId
	 */
	Map<String ,Object> IAmMember(String userId);

	/**
	 * 我在群内的群列表
	 * @param userId
	 * @return
	 */
	Map<String,Object> groupAboutMe(String userId);
	/**
	 * 邀请群成员
	 * @param groupId
	 * @param senderId
	 * @param receiverId
	 * @return
	 */
	Map<String,Object> inviteGroup(String groupId,String senderId,String[] receiverId,String confirm);

	/**
	 * 邀请群成员
	 * @param groupId
	 * @param senderId
	 * @param receiverId
	 * @return
	 */
	Map<String,Object> letJoinGroup(String groupId,String senderId,String[] receiverId);

	/**
	 * 修改群资料
	 * @param groupId
	 * @param userId
	 * @param permission
	 * @param name
	 * @param declared
	 * @param categoryCode
	 * @param imgPath
	 * @return
	 */
	Map<String,Object> modifyGroup(String groupId,String userId,String permission,String name,String declared,String categoryCode,String imgPath);

	/**
	 * 成员主动退出群
	 * @param groupId
	 * @param userId
	 * @return
	 */
	Map<String ,Object> logoutGroup(String groupId,String userId);

	/**
	 * 解散群
	 * @param userId
	 * @param groupId
	 * @return
	 */
	Map<String,Object> deleteGroup(String userId,String groupId);

	/**
	 * 批量删除群成员
	 * @param groupId
	 * @param userId
	 * @param memberIds
	 * @return
	 */
	Map<String,Object> deleteGroupMember(String groupId,String userId,String[] memberIds);

	/**
	 * 群主处理申请
	 * @param groupId
	 * @param masterId
	 * @param askerId
	 * @param confirm
	 * @return
	 */
	Map<String,Object> askJoin(String groupId,String masterId,String askerId,String confirm);

	/**
	 * 查询群
	 * @param queryContent
	 * @return
	 */
	Map<String,Object> queryGroup(String queryContent);

	/**
	 * 修改群名片
	 * @param groupId
	 * @param userId
	 * @param remarkName
	 * @return
	 */
	Map<String,Object> modifyGroupMember(String groupId,String userId,String remarkName);

	/**
	 * 禁言
	 * @param groupId
	 * @param masterId
	 * @param memberId
	 * @param confirm
	 * @return
	 */
	Map<String,Object> forbidSpeak(String groupId,String masterId,String memberId,String confirm);

	/**
	 * 用户设置群组消息接收规则
	 * @param groupId
	 * @param memberId
	 * @param rule
	 * @return
	 */
	Map<String,Object> setGroupMsg(String groupId,String memberId,String rule);

	/**
	 * 查询群成员（从容联那边去查询）
	 * @param groupId
	 * @param userId
	 * @return
	 */
	Map<String,Object> queryMember(String groupId,String userId);

	/**
	 * 查询群组属性（从容联那边去查询）
	 * @param groupId
	 * @param userId
	 * @return
	 */
	Map<String,Object> queryGroupDetail(String groupId,String userId);


}
