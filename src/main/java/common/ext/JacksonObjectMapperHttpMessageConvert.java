package common.ext;

import java.io.IOException;
import java.nio.charset.Charset;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import common.entity.Response;

public class JacksonObjectMapperHttpMessageConvert extends AbstractHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private ObjectMapper objectMapper;

	public JacksonObjectMapperHttpMessageConvert() {
		super(new MediaType("application", "json", DEFAULT_CHARSET));
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		JavaType javaType = getJavaType(clazz);
		return (this.objectMapper.canDeserialize(javaType) && canRead(mediaType));
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		JavaType javaType = getJavaType(clazz);
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), javaType);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		try {
			if (logger.isDebugEnabled()) {
				logger.info("object" + object);
			}

			this.objectMapper.writeValue(outputMessage.getBody(), object);

		} catch (JsonProcessingException ex) {
			logger.error("Could not write JSON: " + ex.getMessage());
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("deprecation")
	protected JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.type(clazz);
	}

	private Response handleResponse(Object object) {
		if (logger.isDebugEnabled()) {
			logger.debug("handle object with json  object : " + object);
		}

		Response response = new Response();
		response.setData(object);

		return response;
	}

}
