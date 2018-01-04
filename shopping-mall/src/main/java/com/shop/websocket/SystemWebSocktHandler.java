package com.shop.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.shop.common.UserConstant;
import com.shop.util.ThreadUtil;

/**
 * 
 * @Description: WebSocket处理类 
 * @ClassName: SystemWebSocktHandler 
 * @author liquor
 * @date 2017年9月6日 下午5:02:34 
 *
 */
public class SystemWebSocktHandler implements WebSocketHandler {

	private static final Logger logger = Logger.getLogger(SystemWebSocktHandler.class);
	
	/**
	 * 用户连接超时map
	 * key 用户名 value 上一次操作时间
	 */
	private final Map<String, Long> timeMap = new HashMap<>();
	
	/**
	 * 用户和websocketSession
	 * key 用户名 value websocketSession集合(同一用户同一浏览器打开多个窗口)
	 */
	public static final Map<String, List<WebSocketSession>> USER_WEBSESSION = new HashMap<String, List<WebSocketSession>>();
	
	/**
	 * 超时时间
	 */
	private final Long receiveTimeDelay = 60000l;
	
	/**
	 * 在WebSocket协商成功后调用，WebSocket连接已打开并可以使用
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 同一用户多个窗体
		if(USER_WEBSESSION.containsKey(session.getAttributes().get(UserConstant.WEBSOCKET_USERNAME))){
			List<WebSocketSession> sessionList = USER_WEBSESSION.get(session.getAttributes().get(UserConstant.WEBSOCKET_USERNAME));
			sessionList.add(session);
			USER_WEBSESSION.put((String)session.getAttributes().get(UserConstant.WEBSOCKET_USERNAME), sessionList);
		}else{// 不同用户登录
			List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
			sessionList.add(session);
			USER_WEBSESSION.put((String)session.getAttributes().get(UserConstant.WEBSOCKET_USERNAME), sessionList);
		}
	}

	/**
	 * 当新的WebSocket消息到达时调用
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		logger.info("获取到消息"+message.getPayload());
//		session.sendMessage(new TextMessage("接收消息成功",true));
		timeMap.put(session.getId(), System.currentTimeMillis());
		listener(session,session.getId());
	}

	/**
	 * 处理底层WebSocket消息传输中的错误
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.info("连接错误");
	}

	/**
	 * 在WebSocket连接已被任何一方关闭之后，或在发生传输错误之后调用
	 */	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// 获取当前用户的在线列表
		List<WebSocketSession> sessionList = USER_WEBSESSION.get(session.getAttributes().get(UserConstant.WEBSOCKET_USERNAME));
		// 空的session列表
		List<WebSocketSession> sessions = new ArrayList<>();
		// 遍历移除退出的session列表
		for(WebSocketSession s : sessionList){
			if(s.getId().equals(session.getId())){
				sessions.add(s);
			}
		}
		// 如果移除的session列表不为空则移除这部分session
		if(!CollectionUtils.isEmpty(sessions)){
			sessionList.removeAll(sessions);
		}
		// 如果移除退出的session后用户列表为空,则直接移除用户
		if(CollectionUtils.isEmpty(sessionList)){
			USER_WEBSESSION.remove(session.getAttributes().get(UserConstant.WEBSOCKET_USERNAME));
		}else{// 如果移除后不为空则重新覆盖原来的session
			USER_WEBSESSION.put((String)session.getAttributes().get(UserConstant.WEBSOCKET_USERNAME), sessionList);
		}
	}

	/**
	 * WebSocketHandler是否处理部分消息
	 */
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(WebSocketMessage<?> message) {
    	try {
	    	for(Map.Entry<String, List<WebSocketSession>> entry : USER_WEBSESSION.entrySet()){
	    		for(WebSocketSession session : entry.getValue()){
						session.sendMessage(message);
	    		}
	    	}
    	} catch (IOException e) {
    		logger.error("给所有在线用户发送信息失败,信息为：" + message);
    		e.printStackTrace();
    	}
    }
 
    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public static void sendMessageToUser(String userName, TextMessage message) {
    	List<WebSocketSession> sessionList = USER_WEBSESSION.get(userName);
    	if(!CollectionUtils.isEmpty(sessionList)){
    		for(WebSocketSession session:sessionList){
    			try {
					session.sendMessage(message);
				} catch (IOException e) {
					logger.info("发送消息失败");
				}
    		}
    	}
    }
	
    /**
     * 监听session,如果长时间没有接收到消息则关闭连接
     */
    public void listener(WebSocketSession session, String id){
    	ThreadUtil.getThreadWithOutmonitor(new Runnable() {
			public void run() {
				while(session.isOpen()){
					Long lastReceiveTime = timeMap.get(id);
					if(System.currentTimeMillis() - lastReceiveTime > receiveTimeDelay){
						if(session.isOpen()){
							try {
								session.sendMessage(new TextMessage("2"));
								session.close();
								logger.info("长时间未接收到消息,主动关闭连接");
							} catch (IOException e) {
								logger.info("关闭连接错误");
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
    }
    
}
