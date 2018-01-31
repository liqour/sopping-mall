package com.shop.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.shop.service.IUserInfoService;
import com.shop.websocket.SystemWebSocktHandler;

@Controller
@Scope("prototype")
@RequestMapping("/userInfo")
public class UserInfoController {

	/** 
	 * 用户管理类
	 */ 
	@Autowired
	private IUserInfoService userInfoService;
	
	/**
	 * session
	 */
	@Autowired
	private HttpServletRequest request;
	
	/** 
	 * 
	 * @Description: 用户登录 
	 * @Title: login 
	 * @param userName 用户名
	 * @param password 密码
	 * @param remFlag 是否记住密码
	 * @return    设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@RequestMapping(value="/login",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(HttpServletResponse response, HttpServletRequest request, String userName, String password, String remFlag){
		return userInfoService.login(request, response, userName, password, remFlag);
	}
	
	/**
	 * 
	 * @Description: 退出登录 
	 * @Title: loginOut 
	 * @param  设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@RequestMapping(value="/loginOut",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loginOut(){
		return userInfoService.loginOut(request);
	}
	
	/**
	 * 
	 * @Description: 发送消息
	 * @Title: sendMsg 
	 * @param @param userName
	 * @param @param msg    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@RequestMapping(value="/sendMsg",method = RequestMethod.GET)
	@ResponseBody
	public void sendMsg(String userName, String msg){
		if(msg.getBytes().length>10 && msg.length()<10){
			msg=msg+"         ";
		}
		TextMessage message = new TextMessage(msg);
		SystemWebSocktHandler.sendMessageToUser(userName, message);
	}

}
