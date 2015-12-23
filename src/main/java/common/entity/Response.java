/*
 * 文件名：Response.java
 * 版权：
 * 描述：
 * 修改人：estn.zuo
 * 修改时间：2013-6-24 下午10:26:35
 */
package common.entity;

/**
 * @author shi_lin
 * @description API返回数据结构
 */
public class Response {

	/**
	 * 返回状态码 默认返回2000表示成功
	 */
	private String code = "2000";

	/**
	 * 数据返回节点
	 */
	private Object data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
