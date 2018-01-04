package com.shop.listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * 
 * @Description: session属性监听类 
 * @ClassName: SessionAttributeListener 
 * @author liquor
 * @date 2017年9月16日 上午9:34:59 
 *
 */
public class SessionAttributeListener implements HttpSessionAttributeListener {

	/**
	 * session 属性添加监听
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		if(event.getName()!=null && event.getName().equals(event)){
			
		}
	}

	/**
	 * session 属性移除监听
	 */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		
	}

	/**
	 * session 属性替换监听
	 */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		
	}

}
