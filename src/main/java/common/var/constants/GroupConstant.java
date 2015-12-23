package common.var.constants;

/**
 * Created by Libo on 2015/11/27.
 */
public class GroupConstant {
    //子账户相关
    //创建子账户
    public static final String SUBACCOUNTS_METHOD_CREATE_SUBACCOUNT = "/SubAccounts";
    //查询子账户
    public static final String SUBACCOUNTS_METHOD_QUERY_SUBAACOUNT = "/QuerySubAccountByName";

    //群相关
    //容联创建群方法
    public static final String REQUEST_METHOD_CREATE_GROUP = "/Group/CreateGroup";
    //容联批量邀请方法
    public static final String REQUEST_METHOD_INVITE_USER = "/Group/InviteJoinGroup";
    //容联修改群组属性方法
    public static final String REQUEST_METHOD_MODIFY_GROUP = "/Group/ModifyGroup";
    //容联个人申请加入群方法
    public static final String REQUEST_METHOD_JOIN_GROUP = "/Group/JoinGroup";
    //容联群成员主动退出群方法
    public static final String REQUEST_METHOD_LOGOUT_GROUP= "/Group/LogoutGroup";
    //容联群解散方法
    public static final String REQUEST_METHOD_DELETE_GROUP= "/Group/DeleteGroup";
    //容联删除群成员方法
    public static final String REQUEST_METHOD_DELETE_GROUP_MEMBER= "/Group/DeleteGroupMember";
    //容联群主处理申请方法
    public static final String REQUEST_METHOD_ASK_JOIN= "/Member/AskJoin";
    //容联群主处理申请方法
    public static final String REQUEST_METHOD_FORBID_SPEAK= "/Member/ForbidSpeak";
    //容联用户设置群组消息接收规则
    public static final String REQUEST_METHOD_SET_GROUP_MSG="/Member/SetGroupMsg";
    //容联查询群成员
    public static final String REQUEST_METHOD_QUERY_MEMEBER="/Member/QueryMember";
    //容联查询群属性
    public static final String REQUEST_METHOD_QUERY_GROUP_DETAIL = "/Group/QueryGroupDetail";

    //----消息推送id----
    //群召唤推送id
    public static final String GROUPCALL_ID = "4c93008615c2d041e33ebac605d14b5b";
}
