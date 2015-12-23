/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package common.var.exception;

/**
 * @Description: DefaultError
 * @Author: lin.shi
 * @CreateTime: 2015-11-15 18:53
 */
public enum DefaultError implements DoulaoError {

	SYSTEM_SUCCESS("0000", "操作成功"), SYSTEM_INTERNAL_ERROR("0001", "系统内部错误"), ACCESS_DENIED_ERROR("0002",
			"拒绝访问"), DEVICE_NOT_EXIST("0003", "设备不存在");

	String errorCode;
	String errorMessage;
	private static final String ns = "DFT";

	DefaultError(String errorCode, String errorMessage) {
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