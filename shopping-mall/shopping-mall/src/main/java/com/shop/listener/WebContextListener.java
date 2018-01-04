package com.shop.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Description: 程序启动的监听事件
 * @ClassName: WebContextListener
 * @author liquor
 * @date 2017年9月2日 下午3:12:40
 *
 */
public class WebContextListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebContextListener.class);

	/**
	 * 启动监听
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.info("========================================================》程序启动《========================================================");
	}

	/**
	 * 结束监听
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
