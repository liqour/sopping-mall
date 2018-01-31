package com.shop.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description: 缓存处理类
 * @Title RedisHelper
 * @className com.shop.redis.RedisHelper
 * @author liquor
 * @date 2017年10月12日17:50:03
 */
@Component
@SuppressWarnings("deprecation")
public class RedisHelper {

	/**
	 * @module 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(RedisHelper.class);
	
	/**
	 * @module redis 连接池
	 */
	@Autowired
    private JedisPool jedisPool;
	
	/**
     * 
      * @Description: setVExpire(设置key值，同时设置失效时间 秒)
      * @Title: setVExpire
      * @param @param key
      * @param @param value
      * @param @param seconds
      * @param index 具体数据库
      * @return void
      * @throws
     */
    public <T> void setVExpire(String key, T value,int seconds,int index) {
        String json = JSON.toJSONString(value);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();// 获取jedis
//            jedis.select(index);
            jedis.set(key, json);
            jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("setV初始化jedis异常：" + e);
            e.printStackTrace();
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }

    }
    /**
     * 
      * @Description: (存入redis数据)
      * @Title: setV
      * @param @param key
      * @param @param value
      * @param index 具体数据库 
      * @return void
      * @throws
     */
    public <V> void setV(String key, V value,int index) {
        String json = JSON.toJSONString(value);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            jedis.set(key, json);
        } catch (Exception e) {
            logger.error("setV初始化jedis异常：" + e);
            if (jedis != null) {
                jedisPool.close();//returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }

    }

    /**
     * 
      * @Description: getV(获取redis数据信息)
      * @Title: getV
      * @param @param key
      * @param index 具体数据库 0:常用数据存储      3：session数据存储
      * @param @return
      * @return V
      * @throws
     */
    @SuppressWarnings("unchecked")
    public <V> V getV(String key,int index) {
        String value = "";
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
//            jedis.select(index);
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("getV初始化jedis异常：" + e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }
        return (V)JSONObject.parse(value);
    }
    
    /**
     * 
      * @Description: getVString(返回json字符串)
      * @Title: getVString
      * @param @param key
      * @param @param index
      * @param @return
      * @return String
      * @throws
     */
    public String getVStr(String key,int index) {
        String value = "";
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("getVString初始化jedis异常：" + e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 
     * @Description: Push(存入 数据到队列中)
     * @Title: Push
     * @param @param key
     * @param @param value
     * @return void
     * @throws
     */
    public <V> void Push(String key, V value) {
        String json = JSON.toJSONString(value);
        Jedis jedis = null;
        try {
            logger.info("存入 数据到队列中");
            jedis = jedisPool.getResource();
            jedis.select(15);
            jedis.lpush(key, json);
        } catch (Exception e) {
            logger.error("Push初始化jedis异常：" + e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }
    }
    /**
     * 
     * @Description: Push(存入 数据到队列中)
     * @Title: PushV
     * @param  key
     * @param value
     * @param dBNum
     * @return void
     * @throws
     */
    public <V> void PushV(String key, V value,int dBNum) {
        String json = JSON.toJSONString(value);
        Jedis jedis = null;
        try {
            logger.info("存入 数据到队列中");
            jedis = jedisPool.getResource();
            jedis.select(dBNum);
            jedis.lpush(key, json);
        } catch (Exception e) {
            logger.error("Push初始化jedis异常：" + e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * 
     * @Description: Push(存入 数据到队列中)
     * @Title: Push
     * @param @param key
     * @param @param value
     * @return void
     * @throws
     */
    public <V> void PushEmail(String key, V value) {
        String json = JSON.toJSONString(value);//JsonUtil.entity2Json(value);
        Jedis jedis = null;
        try {
            logger.info("存入 数据到队列中");
            jedis = jedisPool.getResource();
            jedis.select(15);
            jedis.lpush(key, json);
        } catch (Exception e) {
            logger.error("Push初始化jedis异常：" + e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * 
     * @Description: Pop(从队列中取值)
     * @Title: Pop
     * @param @param key
     * @param @return
     * @return V
     * @throws
     */
    @SuppressWarnings("unchecked")
    public <V> V Pop(String key) {
        String value = "";
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(15);
            value = jedis.rpop(key);
        } catch (Exception e) {
            logger.error("Pop初始化jedis异常：" + e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }
        return (V) value;
    }


    /**
     * 
     * @Description: expireKey(限时存入redis服务器)
     * @Title: expireKey
     * @param @param key
     * @param @param seconds
     * @return void
     * @throws
     */
    public void expireKey(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(3);
            jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("Pop初始化jedis异常：" + e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }

    }
    
    /**
     * @Description: 获取key 的过期时间
     * @param key
     * @return
     */
	public long getExpireTime(String key){
    	Long time = 0l;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
//            time = jedis.persist(key);
            time = jedis.ttl(key);
        } catch (Exception e) {
            logger.error("获取过期时间异常：" + e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            closeJedis(jedis);
        }
        return time;
    }

    /**
     * 
     * @Description: closeJedis(释放redis资源)
     * @Title: closeJedis
     * @param @param jedis
     * @return void
     * @throws
     */
    public void closeJedis(Jedis jedis) {
        try {
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        } catch (Exception e) {
            logger.error("释放资源异常：" + e);
        }
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

	
}
