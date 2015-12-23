/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package common.var.constants;

import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @Description: SystemConstant
 * @Author: lin.shi
 * @CreateTime: 2015-11-15 19:51
 */
public class SystemConstant {
	//图片服务器路径
	public static final String SERVER_IMAGE_PATH = "http://120.24.76.144:8080/images";

	//官方兜捞帐号userId
	public static final String FOMRAL_ACCOUNT_USERID = "552dcda30cf20a796827f4fc";

	// 时间格式
	public static final String DATE_FULL_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_SIMPLE_FORMAT = "yyyy-MM-dd";
	// i18n
	public static Locale LOCALE = Locale.CHINA;
	// token在请求头中的key
	public static final String TOKEN_KEY = "token";
	// 分隔符
	public static final String SEPARATOR_SYMBOL = ",";

	public static final String REDIS_KEY_SEPARATOR_SYMBOL = ":";

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_VIP = "ROLE_VIP";

	//PHP 接口调用地址
	public static final String PHP_API_URL = "http://demo.jiaohuan.idoulao.com/Api/Kf/index";

	// DES算法使用的key,主要用于敏感数据的加密解密
	public static final String DES_ALGORITHM_KEY = "6475ff6c37cf4751";
	public static String APP_ID = "911a2e0996504b4689ae30d2b5283e87";


	public static String ACCOUNTS_ID = "8a48b5514fd49643014fda42a90311cc";//主账号id
	public static String ACCOUNTS_TOKEN= "92092172061b41e9b231800c45e9c95e";//主账号授权令牌
	public static String DOULAO_APP_ID= "8a48b5514fd49643014fda46ebdf11e6";//兜捞应用id

	// 用户盐值长度
	public static final Integer SALT_LENGTH = 8;

	// Body字段类型
	public static final String BODY_METATYPE_TXT = "TXT";
	public static final String BODY_METATYPE_IMG = "IMG";
	public static final String BODY_METATYPE_URL = "URL";
	public static final String BODY_METATYPE_VIDEO = "VIDEO";
	public static final String BODY_METATYPE_PHONE = "PHONE";

	// 将来某个时间点,泛指将来
	public static final Date FUTURE_TIME = new Date(2000000000000L);

	public static final String RESOURCE_DIR = "resources";

	public static final Pattern URL_REG_PATTERN = Pattern
			.compile("^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=()]*)?$");

	// http开头字符串
	public static final String HTTP_START = "http";

	// https 开头字符串
	public static final String HTTPS_START = "https";

	// web开头字符串
	public static final String PREFIX_PEMASS = "doulao://";
}