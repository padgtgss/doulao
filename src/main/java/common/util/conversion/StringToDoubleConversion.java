/*
 * 文件名：LongToDateConversion.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：estn.zuo
 * 修改时间：2014-3-17 下午9:53:59
 */
package common.util.conversion;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * @description String---->Date<br>
 *              时间字符串格式为：yyyy-MM-dd HH:mm:ss 或者 采用时间戳格式如：1395075212913
 * @author shi_lin
 */
public class StringToDoubleConversion implements Converter<String, Double> {

	@Override
	public Double convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		return Double.parseDouble(source);
	}
}
