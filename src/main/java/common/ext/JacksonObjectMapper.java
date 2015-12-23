package common.ext;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import common.var.constants.SystemConstant;

public class JacksonObjectMapper extends ObjectMapper {

	private Log logger = LogFactory.getLog(JacksonObjectMapper.class);

	public JacksonObjectMapper() {

		if (logger.isDebugEnabled()) {
			// 在调试模式下,格式化输出JSON
			configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		}

		configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);

		// 不予输出null字段
		setSerializationInclusion(Inclusion.NON_NULL);

		configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);

		// 当数组中只有一个元素时，也按照数组输出
		configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		// 时间格式化输出
		setDateFormat(new SimpleDateFormat(SystemConstant.DATE_FULL_FORMAT));
	}
}
