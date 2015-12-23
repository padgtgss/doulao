/*
 * 文件名：StringTrimmerConversion.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：estn.zuo
 * 修改时间：2014-3-19 下午11:01:19
 */
package common.util.conversion;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringTrimmerConversion implements Converter<String, String> {

	@Override
	public String convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		return source;
	}

}
