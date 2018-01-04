//package com.shop.test;
//
//import com.google.common.base.Function;
//import com.google.common.collect.Maps;
//import java.util.Collection;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.ConcurrentMap;
//import javax.annotation.Nullable;
//import org.apache.commons.collections.MapUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cache.Cache;
//import org.springframework.cache.support.AbstractCacheManager;
//
//public class EhCacheManager extends AbstractCacheManager
//		implements
//			CacheProviderLoader,
//			DisposableBean,
//			InitializingBean {
//	public final Logger a = LoggerFactory.getLogger(EhCacheManager.class);
//
//	@Autowired(required = false)
//	@Qualifier("cacheExpireProps")
//	private Properties b;
//	private Map<String, Integer> c;
//	private final Map<String, Cache> d = Maps.newConcurrentMap();
//	private final ConcurrentMap<String, CacheProvider> e = Maps.newConcurrentMap();
//	private EhCachedBuilder f;
//
//	protected Collection<? extends Cache> loadCaches() {
//		return this.d.values();
//	}
//
//	public final CacheProvider loadCacheProvider(String cacheName) {
//		if (!(this.e.containsKey(cacheName))) {
//			CacheProvider localCacheProvider = CacheProvider.newInstance(getCache(cacheName));
//			this.e.put(cacheName, localCacheProvider);
//			return localCacheProvider;
//		}
//		return ((CacheProvider) this.e.get(cacheName));
//	}
//
//	public final Cache getCache(String cacheName) {
//		if (!(this.d.containsKey(cacheName))) {
//			Cache localCache = this.f.newCache(cacheName, a(cacheName));
//			this.d.put(cacheName, localCache);
//		}
//		return ((Cache) this.d.get(cacheName));
//	}
//
//	private final int a(String paramString) {
//		return MapUtils.getInteger(this.c, paramString, Integer.valueOf(0)).intValue();
//	}
//
//	public void destroy() throws Exception {
//		for (CacheProvider localCacheProvider : this.e.values())
//			localCacheProvider.clear();
//	}
//
//	public void afterPropertiesSet() {
//		super.afterPropertiesSet();
//		a();
//	}
//
//	private void a() {
//		if ((this.b != null) && (!(this.b.isEmpty())))
//			this.c = Maps.transformValues(Maps.fromProperties(this.b), new Function() {
//				@Nullable
//				public Integer apply(String input) {
//					try {
//						return Integer.valueOf(input);
//					} catch (NumberFormatException localNumberFormatException) {
//						EhCacheManager.this.a.error("转换缓存过期时间失败，将不对该缓存进行过期策略预设。 转换时间只能设置成有效的整形值。 expireSecond : {}",
//								input);
//					}
//					return Integer.valueOf(0);
//				}
//			});
//	}
//
//	public EhCachedBuilder getEhCachedBuilder() {
//		return this.f;
//	}
//
//	public void setEhCachedBuilder(EhCachedBuilder ehCachedBuilder) {
//		this.f = ehCachedBuilder;
//	}
//}