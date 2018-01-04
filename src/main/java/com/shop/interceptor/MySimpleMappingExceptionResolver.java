package com.shop.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * @Description: 异常拦截器
 * @ClassName: MySimpleMappingExceptionResolver
 * @author liquor
 * @date 2017年8月22日 下午3:28:38
 * 
 */
public class MySimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
			Exception ex) {
		try {
			PrintWriter writer = response.getWriter();
			writer.write(ex.getMessage()==null ? "未知异常请联系开发者处理" : ex.getMessage());
		} catch (IOException e) {
//			e.printStackTrace();
		}
		return null;
	}

}
