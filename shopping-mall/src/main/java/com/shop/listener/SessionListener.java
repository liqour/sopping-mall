package com.shop.listener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.shop.websocket.SystemWebSocktHandler;

/**
 * 
 * @Description: HttpSessionListener
 * @ClassName: SessionListener
 * @author liquor
 * @date 2017年9月11日 上午9:33:58
 *
 */
public class SessionListener implements HttpSessionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionListener.class);

	/**
	 * 用户和session绑定关系
	 */
	public static final Map<String, HttpSession> USER_SESSION = new HashMap<>();

	/**
	 * sessionId 和用户绑定关系
	 */
	public static final Map<String, String> SESSION_USER = new HashMap<>();

	/**
	 * session 创建
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		LOGGER.info("HttpSession对象创建");
	}

	/**
	 * session 销毁
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		LOGGER.info("HttpSession对象销毁");
		USER_SESSION.remove(USER_SESSION.remove(se.getSession().getId()));
	}

	/**
	 * 判断用户是否在线
	 */
	public static boolean isOnline(HttpSession session) {
		boolean flag = true;
		if (SESSION_USER.containsKey(session.getId())) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 判断用户是否在别处登录
	 */
	public static void isAreadyLogin(HttpServletRequest request, String userName) {
		// 已经登录再登录,并且不是同一浏览器登录
		if (USER_SESSION.containsKey(userName)
				&& !USER_SESSION.get(userName).getId().equals(request.getSession().getId())) {
			if(SystemWebSocktHandler.USER_WEBSESSION.containsKey(userName)){
				try {
					List<WebSocketSession> sessionList = SystemWebSocktHandler.USER_WEBSESSION.get(userName);
					for (WebSocketSession session : sessionList) {
						session.sendMessage(new TextMessage("1"));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			USER_SESSION.put(userName, request.getSession());
			SESSION_USER.put(request.getSession().getId(), userName);
			// 已经登录,同一浏览器再次登录
		} else if (USER_SESSION.containsKey(userName)
				&& USER_SESSION.get(userName).getId().equals(request.getSession().getId())) {
			LOGGER.info("同一用户同一浏览器登录两次");
		} else {// 正常登录
			USER_SESSION.put(userName, request.getSession());
			SESSION_USER.put(request.getSession().getId(), userName);
		}
	}

	/**
	 * 移除登录用户
	 */
	public static void removeUser(HttpServletRequest request, String userName) {
		// 当前sessionId
		String sessionId = request.getSession().getId();
		// 删除当前sessionId绑定的用户，用户--HttpSession
		USER_SESSION.remove(USER_SESSION.remove(sessionId));
		// 删除当前登录用户绑定的HttpSession
		HttpSession session = USER_SESSION.remove(userName);
		if (session != null) {
			USER_SESSION.remove(session.getId());
		}
	}
}
