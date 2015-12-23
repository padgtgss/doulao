package common.var.exception;

/**
 * @Description: common.var
 * @author: li_bo
 * @CreateTime: 2015-12-03
 */
public enum  AboutUserError implements DoulaoError{
    //常见错误
    UNKNOWN_ERROR("4000", "未知异常"),
    PARAM_IS_NULL("4001", "参数为空"),
    CANNOT_FIND_DATA("4002","查询不到数据"),
    //格式验证错误
    REGEX_MOBILE_ERROR("4100","手机格式错误"),
    //
    ALREADY_HAVA_MOBILE("4200","已存在要求绑定的手机")
    ;



    String errorCode;
    String errorMessage;
    private static final String ns = "SYS";

    AboutUserError(String errorCode, String errorMessage) {
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
