package com.shop.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @Description: 用户管理 
 * @ClassName: IUserInfoService 
 * @author liquor
 * @date 2017年12月11日 下午4:32:30 
 *
 */
public interface IUserInfoService {

	/** 
	 * 
	 * @Description: 用户登录
	 * @Title: queryUser 
	 * @param userName 用户名
	 * @param password 密码
	 * @param remFlag 是否记住密码
	 * @return    响应状态 
	 * @throws 
	 * @author liquor
	 */
	Map<String, Object> login(HttpServletRequest request, HttpServletResponse response, String userName,String password, String remFlag);
	
	/**
	 * 
	 * @Description: 退出登录 
	 * @Title: loginOut 
	 * @param @param request
	 * @param @return    设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws 
	 * @author liquor
	 */
	Map<String, Object> loginOut(HttpServletRequest request);
	
	/**
	 * 
	 * @Description: 获取用户消息 
	 * @Title: getUserMsg 
	 * @param @param request
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws 
	 * @author liquor
	 */
	Object getUserMsg(HttpServletRequest request);
	
}
