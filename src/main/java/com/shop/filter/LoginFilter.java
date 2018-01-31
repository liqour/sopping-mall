package com.shop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shop.common.UserConstant;

/**
 * 
 * @Description: 登录过滤器 
 * @ClassName: LoginFilter 
 * @author liquor
 * @date 2017年11月17日 下午2:22:02 
 *
 */
public class LoginFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("过滤器初始化成功");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		logger.info("操作登录信息过滤开始");
		HttpServletRequest request = (HttpServletRequest)req;   
        HttpServletResponse response = (HttpServletResponse)res;  
        HttpSession session = request.getSession();  
        if(session.getAttribute(UserConstant.SESSION_USERNAME)== null/* && request.getRequestURI().indexOf("login.jsp")==-1*/ ){
        	logger.info("用户未登录,将返回登录页面");
            response.sendRedirect("../login.html");   
            return ;   
        }   
        chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		
		
	}

}
