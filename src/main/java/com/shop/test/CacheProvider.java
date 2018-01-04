//package com.shop.test;
//
//import com.google.common.base.Optional;
//import com.google.common.base.Supplier;
//import org.springframework.cache.Cache;
//import org.springframework.cache.Cache.ValueWrapper;
//
//public final class CacheProvider {
//	private final Cache a;
//
//	private CacheProvider(Cache cache) {
//		this.a = cache;
//	}
//
//	public static CacheProvider newInstance(Cache cache) {
//		return new CacheProvider(cache);
//	}
//
//	public final <T> T get(Object key, Supplier<T> supplier) {
//		Object localObject1 = get(key);
//		if (localObject1 != null) {
//			return localObject1;
//		}
//		Object localObject2 = supplier.get();
//		if (localObject2 != null) {
//			set(key, localObject2);
//		}
//		return localObject2;
//	}
//
//	public final <T> Optional<T> getOpt(Object key, Supplier<T> supplier) {
//		Object localObject = get(key, supplier);
//		return ((localObject == null) ? Optional.absent() : Optional.of(localObject));
//	}
//
//	public final <T> T get(Object key) {
//		if (key != null) {
//			Cache.ValueWrapper localValueWrapper = this.a.get(key);
//			if (localValueWrapper != null) {
//				return localValueWrapper.get();
//			}
//		}
//		return null;
//	}
//
//	public final <T> Optional<T> getOpt(Object key) {
//		Object localObject = get(key);
//		return ((localObject == null) ? Optional.absent() : Optional.of(localObject));
//	}
//
//	public final <T> T get(Object key, Class<T> cachingClazz) {
//		Object localObject = get(key);
//		if (localObject != null)
//			return cachingClazz.cast(localObject);
//		return null;
//	}
//
//	public final <T> Optional<T> getOpt(Object key, Class<T> cachingClazz) {
//		Object localObject = get(key, cachingClazz);
//		return ((localObject == null) ? Optional.absent() : Optional.of(localObject));
//	}
//
//	public final <T> T get(Object key, Class<T> cachingClazz, Supplier<T> supplier) {
//		Object localObject = get(key, supplier);
//		if (localObject != null)
//			return cachingClazz.cast(localObject);
//		return null;
//	}
//
//	public final <T> Optional<T> getOpt(Object key, Class<T> cachingClazz, Supplier<T> supplier) {
//		Object localObject = get(key, cachingClazz, supplier);
//		return ((localObject == null) ? Optional.absent() : Optional.of(localObject));
//	}
//
//	public final void set(Object key, Object value) {
//		if ((key != null) && (value != null))
//			this.a.put(key, value);
//	}
//
//	public final void clear() {
//		this.a.clear();
//	}
//
//	public final void remove(String key) {
//		this.a.evict(key);
//	}
//
//	public final String getName() {
//		return this.a.getName();
//	}
//}
