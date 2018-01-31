//package com.shop.test;
//
//import net.sf.ehcache.CacheManager;
//import net.sf.ehcache.Element;
//import net.sf.ehcache.config.CacheConfiguration;
//import org.springframework.cache.Cache.ValueWrapper;
//import org.springframework.cache.support.SimpleValueWrapper;
//
//public class EhCachedBuilder {
//	private static final CacheConfiguration a = new CacheConfiguration("default", 30);
//
//	private static final CacheManager b = CacheManager.create();
//
//	public org.springframework.cache.Cache newCache(String cacheName) {
//		return new GillionEhCache(cacheName, 0);
//	}
//
//	public org.springframework.cache.Cache newCache(String cacheName, int expireSeconds) {
//		return new GillionEhCache(cacheName, expireSeconds);
//	}
//
//	static {
//		a.setMaxElementsInMemory(30000);
//		a.setMaxElementsOnDisk(100000);
//		a.setOverflowToDisk(true);
//		a.setTimeToIdleSeconds(0L);
//		a.setOverflowToOffHeap(false);
//		a.setDiskPersistent(false);
//		a.setDiskSpoolBufferSizeMB(50);
//		a.diskExpiryThreadIntervalSeconds(120L);
//		a.memoryStoreEvictionPolicy("LFU");
//		System.setProperty("net.sf.ehcache.skipUpdateCheck", "true");
//	}
//
//	public class GillionEhCache implements org.springframework.cache.Cache {
//		private String b;
//		private int c;
//		private net.sf.ehcache.Cache d;
//
//		public GillionEhCache(String name, int expireSeconds) {
//			CacheConfiguration localCacheConfiguration = EhCachedBuilder.a().clone();
//			if (expireSeconds == 0)
//				localCacheConfiguration.setEternal(true);
//			else {
//				localCacheConfiguration.setTimeToLiveSeconds(expireSeconds);
//			}
//			this.b = name;
//			localCacheConfiguration.setName(name);
//			this.d = new net.sf.ehcache.Cache(localCacheConfiguration);
//			this.d.setCacheManager(EhCachedBuilder.b());
//			this.d.initialise();
//
//			this.c = expireSeconds;
//		}
//
//		public String getName() {
//			return this.b;
//		}
//
//		public Object getNativeCache() {
//			throw new UnsupportedOperationException("缓存内部为自定义实现Memcached的读、写、删除、清空方法， 所以当前对象即为缓存实现。");
//		}
//
//		public Cache.ValueWrapper get(Object key) {
//			String str = a(key);
//			Element localElement = this.d.get(str);
//			Object localObject = null;
//			if (localElement != null) {
//				localObject = localElement.getObjectValue();
//			}
//			if (localObject != null) {
//				return new SimpleValueWrapper(localObject);
//			}
//			return null;
//		}
//
//		public void put(Object key, Object value) {
//			String str = a(key);
//			Element localElement = new Element(str, value);
//			this.d.put(localElement);
//		}
//
//		public void evict(Object key) {
//			String str = a(key);
//			this.d.remove(str);
//		}
//
//		public void clear() {
//			this.d.removeAll();
//		}
//
//		private final String a(Object paramObject) {
//			return this.b.concat("-").concat(paramObject.toString());
//		}
//	}
//}