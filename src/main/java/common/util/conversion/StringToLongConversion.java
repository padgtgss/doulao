/*
 * 文件名：StringToIntegerConversion.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：estn.zuo
 * 修改时间：2014-3-21 上午5:09:18
 */
package common.util.conversion;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToLongConversion implements Converter<String, Long> {

	@Override
	public Long convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		return Long.parseLong(source);
	}

}
