/*
 * 文件名：LongToDateConversion.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：estn.zuo
 * 修改时间：2014-3-17 下午9:53:59
 */
package common.util.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

import common.var.constants.SystemConstant;

/**
 * @description String---->Date<br>
 *              时间字符串格式为：yyyy-MM-dd HH:mm:ss 或者 采用时间戳格式如：1395075212913
 * @author shi_lin
 */
public class StringToDateConversion implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}

		// 直接采用系统统一的时间格式yyyy-MM-dd HH:mm:ss
		SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT);
		try {
			Date d = sdf.parse(source);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 尝试解析时间戳格式
		try {
			return new Date(Long.parseLong(source));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 尝试解析时间格式yyyy-MM-dd
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = sdf.parse(source);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
}
