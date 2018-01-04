package com.shop.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.shop.common.UserConstant;
import com.shop.entity.UserInfo;
import com.shop.exception.BusinessRuntimeException;

/**
 * 
 * @Description: WebSocket握手监听类
 * @ClassName: WebSocketHandshakeInterceptor
 * @author liquor
 * @date 2017年9月6日 下午3:46:36
 *
 */
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	private final Logger LOGGER = Logger.getLogger(WebSocketHandshakeInterceptor.class);

	/**
	 * 握手前
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			if (session != null) {
				// 使用userName区分WebSocketHandler，以便定向发送消息
				UserInfo user = (UserInfo) session.getAttribute(UserConstant.SESSION_USERNAME);
				LOGGER.info(user.getUserName() + " login");
				attributes.put(UserConstant.WEBSOCKET_USERNAME, user.getUserName());
			} else {
				throw new BusinessRuntimeException("请先登录");
			}
		}
		return true;
	}

	/**
	 * 握手后
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		
	}

}
