package com.test.shop.testWebSocket;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * websocket握手拦截器
 * 拦截握手前，握手后的两个切面
 */
public class MyHandShakeInterceptor implements HandshakeInterceptor {

	Logger LOGGER = Logger.getLogger(MyHandShakeInterceptor.class);
	
	@Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        System.out.println("Websocket:用户[ID:" + ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getSession(false).getAttribute("loginUser") + "]已经建立连接");
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
//            HttpSession session = servletRequest.getServletRequest().getSession(false);
            LOGGER.info("开始握手。。。。。。。。。。。。。。。。。。。");
            // 标记用户
//            User user = (User) session.getAttribute("user");
//            if(user!=null){
//                map.put("uid", user.getUserId());//为服务器创建WebSocketSession做准备
//                System.out.println("用户id："+user.getUserId()+" 被加入");
//            }else{
//                System.out.println("user为空");
//                return false;
//            }
        }
        return true;
    }

	@Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
		LOGGER.info("握手结束。。。。。。。。。。。。。。。。。。。");
    }
}