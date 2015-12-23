package com.yslt.doulao.chat.dao;

import java.util.List;

import com.yslt.doulao.chat.entity.GroupMember;

public interface GroupMemberDao {

	//插入一个群-用户数据
	GroupMember insert(GroupMember groupMember);
	
	//多条件查询群成员
	List<GroupMember> findList(GroupMember groupMember);

	//多条件查询群成员
	List<GroupMember> findListIdNameImg(GroupMember groupMember);
	
	//更改群成员
	int update(GroupMember groupMember);
	
	//查询群成员
	GroupMember findOne(GroupMember groupMember);

	//删除数据
	int delete(GroupMember groupMember);
}
