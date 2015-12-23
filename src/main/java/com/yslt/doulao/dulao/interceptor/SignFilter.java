package com.yslt.doulao.dulao.interceptor;


import common.entity.BaseBusinessException;
import common.util.AESUtil;
import common.util.MD5Util;
import common.util.SignatureUtil;
import common.var.exception.SysError;
import common.var.constants.SystemConstant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @Description: SignFilter
 * @anthor: shi_lin
 * @CreateTime: 2015-11-23
 */

public class SignFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String signString = this.doAssembleSignString((HttpServletRequest)servletRequest);
        String aesString = AESUtil.encrypt(signString, SystemConstant.DES_ALGORITHM_KEY);
        String sign = servletRequest.getParameter("sign");

        if ("".equals(sign) || sign == null)
            throw new BaseBusinessException(SysError.SIGNATURE_ERROR);
        if(!sign.equals(MD5Util.encrypt(aesString)))
            throw new BaseBusinessException(SysError.SIGNATURE_ERROR);
        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {

    }
    private String doAssembleSignString(HttpServletRequest request){
        Map<String, String[]> param = request.getParameterMap();
        return SignatureUtil.createSignString(param);
    }
}
