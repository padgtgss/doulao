package com.yslt.doulao.chat.service;

import java.util.Map;

/**
 * @Description: com.yslt.doulao.chat.service
 * @author: li_bo
 * @CreateTime: 2015-12-15
 */
public interface GroupFunctionService {

    /**
     * 群召唤
     * @param groupId
     * @param userId
     * @return
     */
    Map<String,Object> groupCall(String groupId,String userId,String content);
}
