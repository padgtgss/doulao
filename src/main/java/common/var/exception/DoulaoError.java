/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package common.var.exception;

/**
 * @Description: DoulaoError
 * @Author: lin.shi
 * @CreateTime: 2015-11-15 18:53
 */
public interface DoulaoError {

	public String getNamespace();

	public String getErrorCode();

	public String getErrorMessage();
}