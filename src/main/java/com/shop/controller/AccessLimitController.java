package com.shop.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.util.concurrent.RateLimiter;
import com.shop.common.TimeUnit;
import com.shop.exception.BusinessRuntimeException;
import com.shop.util.ResultUtils;

/**
 * 
 * @Description: 限流
 * @ClassName: AccessLimitController 
 * @author liquor
 * @date 2017年12月11日 下午4:30:08 
 *
 */
@Controller
@RequestMapping("/access")
public class AccessLimitController {

	final RateLimiter rateLimiter = RateLimiter.create(5);
	
	/**
	 * 
	 * @Description: 尝试获取,没有获取到则直接返回,不等待
	 * @Title: access 
	 * @param @return    设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@RequestMapping("/limitTry")
	@ResponseBody
	public Map<String, Object> accessTry(){
		if(rateLimiter.tryAcquire())
			return ResultUtils.getSuccessResultData();
		else
			throw new BusinessRuntimeException();
//			return ResultUtils.getFaildResultData();
	}
	
	/**
	 * 
	 * @Description: 尝试获取指定数量的令牌,如果没有则返回false
	 * @Title: accessTryP 
	 * @param @param permits
	 * @param @return    设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@RequestMapping("/limitTryP")
	@ResponseBody
	public Map<String, Object> accessTryP(int permits){
		if(rateLimiter.tryAcquire(permits))
			return ResultUtils.getSuccessResultData();
		else
			return ResultUtils.getFaildResultData();
	}
	
	/**
	 * 
	 * @Description: 指定时间内获取指定数量许可,如果无法获取则返回false 
	 * @Title: accessTryPTU 
	 * @param @param permits
	 * @param @param timeout
	 * @param @param unit
	 * @param @return    设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@RequestMapping("/limitTryPTU")
	@ResponseBody
	public Map<String, Object> accessTryPTU(int permits, long timeout){
		if(rateLimiter.tryAcquire(permits,timeout,TimeUnit.NANOSECONDS.getTimeUnit()))
			return ResultUtils.getSuccessResultData();
		else
			return ResultUtils.getFaildResultData();
	}
	
	/**
	 * 
	 * @Description: 在指定时间未获取到许可则返回false 
	 * @Title: accessTryTU 
	 * @param @param timeout
	 * @param @return    设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@RequestMapping("/limitTryTU")
	@ResponseBody
	public Map<String, Object> accessTryTU(long timeout){
		if(rateLimiter.tryAcquire(timeout,TimeUnit.NANOSECONDS.getTimeUnit()))
			return ResultUtils.getSuccessResultData();
		else
			return ResultUtils.getFaildResultData();
	}
	
	/**
	 * 
	 * @Description: 阻塞,获取到再执行
	 * @Title: accessAcq 
	 * @param @return    设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws 
	 * @author liquor
	 */
	@RequestMapping("/limitAcq")
	@ResponseBody
	public Map<String, Object> accessAcq(){
		rateLimiter.acquire();
		return ResultUtils.getSuccessResultData();
	}
	
}
