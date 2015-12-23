package com.yslt.doulao.chat.dao;

import java.util.List;

import com.mongodb.DBCursor;
import com.yslt.doulao.chat.entity.Group;

public interface GroupDao {
	
	//保存一个群
	Group insert(Group group);
	
	//更改一个群
	int update(Group group);
	
	//根据id查询一个群
	Group findOne(String groupId);

	//根据群号groupNum查询一个群
	Group findOneByGroupNum(String groupNum);

	//多条件查询群
	List<Group> findList(Group group);

	//多条件查询群，返回DBCursor
	DBCursor findList2(Group group);

	//删除一个群
	int delete(String groupId);
}
