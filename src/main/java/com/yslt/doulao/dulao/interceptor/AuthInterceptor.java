package com.yslt.doulao.dulao.interceptor;

import common.entity.BaseBusinessException;
import common.util.AESUtil;
import common.util.MD5Util;
import common.util.SignatureUtil;
import common.var.exception.SysError;
import common.var.constants.SystemConstant;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description: AuthInterceptor
 * @anthor: shi_lin
 * @CreateTime: 2015-11-23
 */

public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String signString = this.doAssembleSignString(request);
        String aesString = AESUtil.encrypt(signString, SystemConstant.DES_ALGORITHM_KEY);
        String sign = request.getParameter("sign");
        if ("".equals(sign) || sign == null)
             throw new BaseBusinessException(SysError.SIGNATURE_ERROR);
        if(!sign.equals(MD5Util.encrypt(aesString)))
            throw new BaseBusinessException(SysError.SIGNATURE_ERROR);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }


    private String doAssembleSignString(HttpServletRequest request){
        Map<String, String[]> param = request.getParameterMap();
        return SignatureUtil.createSignString(param);
    }
}
