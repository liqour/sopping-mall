package com.shop.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shop.annotation.ExcutionTime;
import com.shop.annotation.RedisCache;
import com.shop.common.UserConstant;
import com.shop.dao.UserInfoMapper;
import com.shop.entity.UserInfo;
import com.shop.entity.UserInfoExample;
import com.shop.exception.BusinessRuntimeException;
import com.shop.listener.SessionListener;
import com.shop.service.IUserInfoService;
import com.shop.util.EncryptionUtils;
import com.shop.util.ResultUtils;

@Service("userInfoServiceImpl")
public class UserInfoServiceImpl implements IUserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	/**
	 * 用户实体
	 */
	@Autowired
	private UserInfoMapper userInfoMapper;

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
	@RedisCache(prefix="user", expireTime=1000, params={"userName","password"} ,clazz = UserInfoServiceImpl.class)
	@ExcutionTime
	@Override
	public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response, String userName,
			String password, String remFlag) {
		// 设置响应编码
		response.setCharacterEncoding("UTF-8");
		// 校验必填字段
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			throw new BusinessRuntimeException("用户名密码不能为空");
		}
		// 查询用户是否存在
		UserInfoExample userExample = new UserInfoExample();
		userExample.createCriteria().andUserNameEqualTo(userName);
		List<UserInfo> userList = userInfoMapper.selectByExample(userExample);
		if (CollectionUtils.isEmpty(userList)) {
			throw new RuntimeException("用户不存在");
		}
		// 密码md5加密
		String newPassword = EncryptionUtils.EncoderByMd5(password);
		// 校验密码
		UserInfo userInfo = userList.get(0);
		if(!newPassword.equals(userInfo.getPassword())){
			throw new BusinessRuntimeException("密码错误");
		}
		// 判断用户是否已经在别处登录
		SessionListener.isAreadyLogin(request, userName);
		logger.info("登录成功");
		// 设置session
		request.getSession().setAttribute(UserConstant.SESSION_USERNAME, userInfo);
		// 是否记住密码
		if ("1".equals(remFlag)) {
			String content = userInfo.getUserName() + "," + password;
			Cookie cookie = new Cookie("loginInfo", content);
			cookie.setMaxAge(30 * 24 * 60 * 60);
			cookie.setPath("/");
			cookie.setSecure(false);
			response.addCookie(cookie);
		} else if (StringUtils.isEmpty(remFlag)) {
			Cookie cookie = new Cookie("loginInfo", null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return ResultUtils.getSuccessResultData(getIp(request));
	}

	/** 
	 * @Description: 获取IP
	 * @Title: getIp 
	 * @param @param request
	 * @param @return    设定文件 
	 * @return    返回类型 
	 * @throws 
	 * @author liquor
	 */
	public String getIp(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个IP值，第一个IP才是真实IP
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
	}
	
	/**
	 * 
	 * @Description: 退出登录 
	 * @Title: loginOut 
	 * @param @param request
	 * @param @return    设定文件 
	 * @return    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@Override
	public Map<String, Object> loginOut(HttpServletRequest request){
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(UserConstant.SESSION_USERNAME);
		String msg = "";
		if(userInfo!=null){
			msg = "用户退出登录："+userInfo.getUserName();
			logger.info("用户退出登录："+userInfo.getUserName());
			request.getSession().removeAttribute("loginUser");
			SessionListener.removeUser(request, userInfo.getUserName());
		}else{
			msg = "session丢失";
			logger.info("session丢失");
		}
		return ResultUtils.getSuccessResultData(msg);
	}
	
	/**
	 * 
	 * @Description: 获取用户信息
	 * @Title: getUserMsg 
	 * @param @param request
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Object    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@Override
	public Object getUserMsg(HttpServletRequest request){
		if(request == null){
			return "空";
		}
		return request.getSession().getAttribute("userMsg");
	}

}
