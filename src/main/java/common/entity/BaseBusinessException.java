/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package common.entity;

import common.var.exception.DefaultError;
import common.var.exception.DoulaoError;

/**
 * @Description: 系统异常，自定义异常必须继承本类,业务模块需要抛出业务异常时使用该异常。
 * @Author: lin.shi
 * @CreateTime: 2015-11-15 18:51
 */
public class BaseBusinessException extends RuntimeException {
	private static final long serialVersionUID = -6293662498600553602L;
	private DoulaoError error = DefaultError.SYSTEM_INTERNAL_ERROR;
	// 存在在IError中没定义的异常信息，比如批量提交数据，其中一条数据出错了，报的异常信息放在extMessage中。
	private String extMessage = null;

	public BaseBusinessException() {
	}

	public BaseBusinessException(String message) {
		super(message);
		this.extMessage = message;
	}

	public BaseBusinessException(String message, Throwable cause) {
		super(message, cause);
		this.extMessage = message;
	}

	public BaseBusinessException(Throwable cause) {
		super(cause);
	}

	public BaseBusinessException(DoulaoError error) {
		this.error = error;
	}

	public BaseBusinessException(String message, DoulaoError error) {
		super(message);
		this.extMessage = message;
		this.error = error;
	}

	public BaseBusinessException(String message, Throwable cause, DoulaoError error) {
		super(message, cause);
		this.extMessage = message;
		this.error = error;
	}

	public BaseBusinessException(Throwable cause, DoulaoError error) {
		super(cause);
		this.error = error;
	}

	public DoulaoError getError() {
		return error;
	}

	public String getExtMessage() {
		return extMessage;
	}

	public void setExtMessage(String extMessage) {
		this.extMessage = extMessage;
	}

	public String toString() {
		return super.toString() + ",ErrorCode : " + error.getErrorCode() + ", ErrorMessage : " + error.getErrorMessage()
				+ ", ExtMessage : " + extMessage;
	}
}