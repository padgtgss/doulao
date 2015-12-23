package common.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import common.entity.BaseBusinessException;
import common.entity.Response;
import common.var.exception.DefaultError;

public class ServerExceptionResolver extends AbstractHandlerExceptionResolver {

	private ObjectMapper objectMapper;

	private String contentType;

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		if (logger.isDebugEnabled()) {
			logger.debug("ServerExceptionResolver [ex:" + ex.getMessage() + "]");
		}

		PrintWriter pw = null;

		try {
			response.setContentType(contentType);
			response.setStatus(508);
			pw = response.getWriter();
			pw.write(objectMapper.writeValueAsString(handleException(ex)));
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (pw != null)
				pw.close();
		}

		return null;
	}

	private Response handleException(Exception ex) {
		Response response = new Response();

		// 系统自定义异常
		if (ex instanceof BaseBusinessException) {
			BaseBusinessException bbe = (BaseBusinessException) ex;
			response.setCode(bbe.getError().getErrorCode());
			response.setData(bbe.getError().getErrorMessage());
		}
		// 拒绝访问异常，当权限不足时发生
		else if (ex instanceof AccessDeniedException) {
			response.setCode(DefaultError.ACCESS_DENIED_ERROR.getErrorCode());
			response.setData(DefaultError.ACCESS_DENIED_ERROR.getErrorMessage());
		}
		// 系统自带异常，主要指JVM抛出的异常
		else {
			response.setCode(DefaultError.SYSTEM_INTERNAL_ERROR.getErrorCode());
			response.setData(DefaultError.SYSTEM_INTERNAL_ERROR.getErrorMessage());
		}

		return response;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
