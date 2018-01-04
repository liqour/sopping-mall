package com.shop.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @Description: servletRequestListener 
 * @ClassName: RequestListener 
 * @author liquor
 * @date 2017年9月11日 上午9:28:38 
 *
 */
public class RequestListener implements ServletRequestListener {

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		// 将所有request请求都携带上httpSession
        ((HttpServletRequest) sre.getServletRequest()).getSession(false);
	}
	
	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {
		
	}
	
}
