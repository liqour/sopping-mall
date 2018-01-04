package com.shop.annotation;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @Description: 缓存操作类
 * @Title: RedisHelper
 * @Date: 2017年10月11日14:19:15
 */
//@Component
@Deprecated
public class RedisHelper {

	/**
	 * @Description: 日志记录
	 */
	private static final Logger logger = Logger.getLogger(RedisHelper.class);
	
	/**
	 * @module: redisTemplate操作
	 */
	@Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
	
	
    /**
     * @Description: 批量删除对应的value
     * @param keys
     */
    public void remove(final String ...keys){
    	for(String key:keys){
    		remove(key);
    	}
    }
    
    /**
     * @Description: 删除对应的value
     * @param key
     */
    public void remove(final String key){
    	if(exists(key)){// 判断key是否存在
    		redisTemplate.delete(key);
    	}
    }
    
    /**
     * @Description: 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern){
    	Set<Serializable> keys = redisTemplate.keys(pattern);
    	if(CollectionUtils.isEmpty(keys)){
    		redisTemplate.delete(keys);
    	}
    }
    
    /**
     * @Description: 判断缓存中是否存在对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}
    
    /**
     * @Description: 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key){
    	logger.debug("读取缓存");
    	Object result = null;
    	ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
    	result = operations.get(key);
    	return result;
    }
    
    /**
     * @Description: 写入缓存(Object)
     * @param key
     * @param value
     * @return
     */
    public boolean setObject(final String key, Object value){
    	logger.debug("写入缓存");
    	boolean result = false;
    	try {
    		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        	operations.set(key, value);
        	result = true;
		} catch (Exception e) {
			logger.error("缓存写入错误");
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * @Description: 写入缓存带失效时间(Object)
     * @param key
     * @param value
     * @param expireTime 失效时间
     * @return
     */
    public boolean setObject(final String key, Object value, Long expireTime, TimeUnit timeUnit){
    	logger.debug("写入缓存");
    	boolean result = false;
    	try {
    		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        	operations.set(key, value);
        	// NANOSECONDS 毫微秒 十亿分之一秒（就是微秒/1000）
        	// MICROSECONDS 微秒 一百万分之一秒（就是毫秒/1000）
        	// MILLISECONDS 毫秒 千分之一秒
        	// SECONDS 秒
        	// MINUTES 分
        	// HOURS 时
        	// DAYS 天
        	redisTemplate.expire(key, expireTime, timeUnit);
        	result = true;
		} catch (Exception e) {
			logger.error("缓存写入错误");
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * @Description: 重新设置缓存操作模板
     * @param redisTemplate
     */
    public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate){
    	this.redisTemplate = redisTemplate;
    }
    
}
