package common.var.exception;

/**
 * Created by Libo on 2015/11/26.
 */
public enum  GroupError implements DoulaoError{
    //常见错误
    UNKNOWN_ERROR("3000", "未知异常"),
    PARAM_IS_NULL("3001", "参数为空"),
    REQUEST_FAILED("3002","请求失败"),
    FAILED_GET_SUBACCOUNT("3003","没有获取到子账户"),
    CANNOT_ANALIZE_BECAUSEOF_NULL("3004","数据为空，不能解析"),
    FORMAT_URL_NOT_CORRECT("3005","url格式不对"),
    PARAM_IS_FAULT("3006","参数错误"),
    XML_ANALIZE_FAILED("3007","Xml解析错误"),
    SHOULD_GREATER_THAN_0("3008","必须大于0"),
    PHP_NOT_SUCCESS("3009","排除干扰状态后，PHP返回不成功"),
    //与数据库相关的错误
    USER_NOT_EXISTS("3100", "用户不存在"),
    BATCH_PARAM_TOO_SHORT("3101", "批量参数过短"),
    CATEGORY_NOT_EXISTS("3102","没有这个群分类"),
    TYPE_NOT_EXISTS("3103", "没有这个群类型"),
    PERMISSION_NOT_EXISTS("3104","没有这个加群验证方式"),
    GROUP_NOT_EXISTS("3105","没有这个群"),
    NO_ID("3106", "没有主键"),
    FAILED_UPDATE("3107","更新数据失败"),
    DIAMOND_NOT_EXISTS("3108","没有这个等级对应的钻卡等级"),
    NOT_SPECIFIED_FIELD("3109","未指定字段"),
    //容联那边的
    RL_CREATE_SUBACCOUNT_FAILED("3200","创建子账户失败"),
    RL_CREATE_GROUP_FAILED("3201","建群失败"),
    RL_NOT_000000("3202","请求成功但返回的statusCode不是000000"),
    PARAM_RL_NOT_ACCEPT("3203","参数值容联不接收"),
    //与权限职能相关的
    NOT_GROUPMASTER("3302","不是群主"),
    ALREADY_IS_GROUPMEMBER("3303","已经是群成员"),
    NOT_GROUPMEMBER("3304","不是群成员"),
    NOT_MASTER_NOR_SHAMMASTER("3305","不是群主，也不是伪群主"),
    NOT_POWER_OF_TYPE("3306","建此类型群的权限不足"),
    NOT_APPLYING("3307","此用户没有在申请加入群"),
    NOT_POWER_OF_INVITING("3308","没有邀请权限"),
    ;



    String errorCode;
    String errorMessage;
    private static final String ns = "SYS";

    GroupError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getNamespace() {
        return ns.toUpperCase();
    }

    public String getErrorCode() {
        return ns + "." + errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
