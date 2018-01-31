package com.shop.redis;

public interface CacheService {

	// 从缓存里面获取数据
	public <V> V cacheResult(String key);
	
	// 从缓存里面删除数据
	public void cacheRemove(String key);
	
	// 增加一个缓存
	public <V> void cachePut(String key,V value);
}
