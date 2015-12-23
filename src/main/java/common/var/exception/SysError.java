package common.var.exception;

public enum SysError implements DoulaoError {
	/*- 用户类错误 -*/
	USERNAME_HAS_EXISTENCE("0001", "用户名已存在"), PASSWORD_ERROR("0002", "密码错误"),
	USER_EXPERENCE_DEFICIENCY("0003","经验值不足"),
	SIGNATURE_ERROR("1001", "数据异常,请联系管理员！");

	String errorCode;
	String errorMessage;
	private static final String ns = "SYS";

	SysError(String errorCode, String errorMessage) {
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